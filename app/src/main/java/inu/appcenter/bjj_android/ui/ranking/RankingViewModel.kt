package inu.appcenter.bjj_android.ui.ranking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import inu.appcenter.bjj_android.model.menu.MenuRankingDetail
import inu.appcenter.bjj_android.model.review.ReviewDetailRes
import inu.appcenter.bjj_android.repository.menu.MenuRepository
import inu.appcenter.bjj_android.repository.review.ReviewRepository
import inu.appcenter.bjj_android.ui.menudetail.ReviewError
import inu.appcenter.bjj_android.utils.collectAndHandle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
): ViewModel() {
    private val _uiState = MutableStateFlow(RankingUiState())
    val uiState = _uiState.asStateFlow()

    private var currentPage = 0
    private var isLastPage = false

    init {
        getMenuRankingList()
    }

    fun getMenuRankingList() = viewModelScope.launch {
        if (isLastPage || _uiState.value.isLoading) return@launch

        menuRepository.getMenusRanking(currentPage, 10).collectAndHandle(
            onError = { error ->
                _uiState.update {
                    it.copy(isLoading = false, error = error?.message)
                }
            },
            onLoading = {
                _uiState.update {
                    it.copy(isLoading = true, error = null)
                }
            },
            stateReducer = { menuRanking ->
                isLastPage = menuRanking.lastPage
                currentPage++

                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        error = null,
                        rankingList = currentState.rankingList + menuRanking.menuRankingDetailList,
                        // 첫 페이지일 경우에만 lastUpdatedAt 업데이트
                        lastUpdatedAt = if(currentPage == 1)
                            menuRanking.menuRankingDetailList.firstOrNull()?.updatedAt
                        else
                            currentState.lastUpdatedAt
                    )
                }
            }
        )
    }

    // 리뷰 상세 받기
    fun getBestReviewDetail(reviewId: Long) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, error = null) }

        reviewRepository.getReviewDetail(reviewId).collectAndHandle(
            onError = { error ->
                val errorMessage = when (error) {
                    is ReviewError.EmptyResponse -> error.message
                    is ReviewError.ApiError -> "API 오류: ${error.message}"
                    is ReviewError.NetworkError -> "네트워크 오류: ${error.message}"
                    else -> "알 수 없는 오류: ${error?.message ?: "null"}"
                }
                _uiState.update { it.copy(
                    isLoading = false,
                    error = errorMessage
                )}
            },
            onLoading = {
                _uiState.update { it.copy(isLoading = true) }
            },
            stateReducer = { reviewDetail ->
                _uiState.update { currentState ->
                    currentState.copy(
                        bestReview = reviewDetail,
                        isLoading = false,
                        error = null
                    )
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