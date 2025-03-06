package inu.appcenter.bjj_android.ui.ranking
import androidx.lifecycle.viewModelScope
import inu.appcenter.bjj_android.model.menu.MenuRankingDetail
import inu.appcenter.bjj_android.model.review.ReviewDetailRes
import inu.appcenter.bjj_android.repository.menu.MenuRepository
import inu.appcenter.bjj_android.repository.review.ReviewRepository
import inu.appcenter.bjj_android.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class RankingUiEvent {
    data class ShowToast(val message: String) : RankingUiEvent()
}

data class RankingUiState(
    val rankingList: List<MenuRankingDetail> = emptyList(),
    val bestReview: ReviewDetailRes? = null,
    val selectedReviewId: Long? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val lastUpdatedAt: String? = null
)

class RankingViewModel(
    private val menuRepository: MenuRepository,
    private val reviewRepository: ReviewRepository
) : BaseViewModel() {

    private val _eventFlow = MutableSharedFlow<RankingUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _uiState = MutableStateFlow(RankingUiState())
    val uiState = _uiState.asStateFlow()

    private var currentPage = 0
    private var isLastPage = false

    init {
        getMenuRankingList()
    }

    fun getMenuRankingList() = viewModelScope.launch {
        if (isLastPage || _uiState.value.isLoading) return@launch

        menuRepository.getMenusRanking(currentPage, 10).handleResponse(
            onSuccess = { menuRanking ->
                isLastPage = menuRanking.lastPage
                currentPage++

                _uiState.update { currentState ->
                    currentState.copy(
                        rankingList = currentState.rankingList + menuRanking.menuRankingDetailList,
                        lastUpdatedAt = if(currentPage == 1)
                            menuRanking.menuRankingDetailList.firstOrNull()?.updatedAt
                        else
                            currentState.lastUpdatedAt
                    )
                }
            },
            onError = { error ->
                emitError(error)
                viewModelScope.launch {
                    _eventFlow.emit(RankingUiEvent.ShowToast(
                        error.message ?: "랭킹 정보를 불러오는 중 오류가 발생했습니다."
                    ))
                }
            }
        )
    }

    fun getBestReviewDetail(reviewId: Long) = viewModelScope.launch {
        reviewRepository.getReviewDetail(reviewId).handleResponse(
            onSuccess = { reviewDetail ->
                _uiState.update { currentState ->
                    currentState.copy(
                        bestReview = reviewDetail
                    )
                }
            },
            onError = { error ->
                emitError(error)
                viewModelScope.launch {
                    _eventFlow.emit(RankingUiEvent.ShowToast(
                        error.message ?: "리뷰 상세 정보를 불러오는 중 오류가 발생했습니다."
                    ))
                }
            }
        )
    }

    fun selectBestReviewId(bestReviewId: Long) {
        _uiState.update {
            it.copy(
                selectedReviewId = bestReviewId
            )
        }
    }

    fun resetSelectedBestReviewId() {
        _uiState.update {
            it.copy(
                selectedReviewId = null
            )
        }
    }
}