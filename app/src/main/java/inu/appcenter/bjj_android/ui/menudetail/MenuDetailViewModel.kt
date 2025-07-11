package inu.appcenter.bjj_android.ui.menudetail

import android.util.Log
import androidx.lifecycle.viewModelScope
import inu.appcenter.bjj_android.model.review.ReportRequest
import inu.appcenter.bjj_android.model.review.ReviewImageDetail
import inu.appcenter.bjj_android.model.review.ReviewRes
import inu.appcenter.bjj_android.model.todaydiet.TodayDietRes
import inu.appcenter.bjj_android.repository.menu.MenuRepository
import inu.appcenter.bjj_android.repository.review.ReviewRepository
import inu.appcenter.bjj_android.repository.todaydiet.TodayDietRepository
import inu.appcenter.bjj_android.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val DEFAULT_PAGE_SIZE = 10
private const val DEFAULT_PAGE_NUMBER = 0

// UiEvent sealed class
sealed class MenuDetailUiEvent {
    data class ShowToast(val message: String) : MenuDetailUiEvent()
}

data class MenuDetailUiState(
    val selectedMenu: TodayDietRes? = null,
    val reviews: ReviewRes? = null,
    val isWithImages: Boolean = false,
    val sort: SortingRules = SortingRules.BEST_MATCH,
    val reviewImages: List<ReviewImageDetail>? = null,
    val moreReviewImages: List<ReviewImageDetail>? = null,
    val isLoadingMoreReviews: Boolean = false // 추가된 부분

)

enum class SortingRules {
    BEST_MATCH,
    MOST_LIKED,
    NEWEST_FIRST;

    override fun toString() = name

    fun toKorean() = when(this) {
        BEST_MATCH -> "메뉴일치순"
        MOST_LIKED -> "좋아요순"
        NEWEST_FIRST -> "최신순"
    }
}

class MenuDetailViewModel(
    private val todayDietRepository: TodayDietRepository,
    private val reviewRepository: ReviewRepository,
    private val menuRepository: MenuRepository
) : BaseViewModel() {

    private val _eventFlow = MutableSharedFlow<MenuDetailUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _uiState = MutableStateFlow(MenuDetailUiState())
    val uiState = _uiState.asStateFlow()

    fun selectMenu(menu: TodayDietRes) {
        _uiState.update {
            it.copy(
                selectedMenu = menu,
                isWithImages = false,  // 초기화
                sort = SortingRules.BEST_MATCH  // 초기화
            )
        }
        getReviewsByMenu(menu.menuPairId)
    }

    fun selectIsWithImages(isWithImages: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                isWithImages = isWithImages
            )
        }
    }

    fun selectSortingRule(sort: SortingRules) {
        _uiState.update { currentState ->
            currentState.copy(
                sort = sort
            )
        }
    }

    fun getMoreReviewsByMenu(
        menuPairId: Long,
        pageNumber: Int = DEFAULT_PAGE_NUMBER,
        pageSize: Int = DEFAULT_PAGE_SIZE,
        sort: SortingRules = SortingRules.BEST_MATCH,
        isWithImages: Boolean = false
    ) {
        viewModelScope.launch {
            // 로딩 상태 시작
            _uiState.update { it.copy(isLoadingMoreReviews = true) }

            reviewRepository.getReviews(
                menuPairId = menuPairId,
                pageNumber = pageNumber,
                pageSize = pageSize,
                sort = sort.toString(),
                isWithImages = isWithImages
            ).handleResponse(
                onSuccess = { moreReviews ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            reviews = ReviewRes(
                                reviewDetailList = (currentState.reviews?.reviewDetailList ?: emptyList()) + moreReviews.reviewDetailList,
                                lastPage = moreReviews.lastPage
                            ),
                            isLoadingMoreReviews = false // 로딩 상태 종료
                        )
                    }
                },
                onError = { error ->
                    // 로딩 상태 종료 및 에러 처리
                    _uiState.update { it.copy(isLoadingMoreReviews = false) }
                    emitError(error)
                    viewModelScope.launch {
                        _eventFlow.emit(MenuDetailUiEvent.ShowToast(
                            error.message ?: "리뷰를 불러오는 중 오류가 발생했습니다."
                        ))
                    }
                }
            )
        }
    }

    fun getReviewsByMenu(
        menuPairId: Long,
        pageNumber: Int = DEFAULT_PAGE_NUMBER,
        pageSize: Int = DEFAULT_PAGE_SIZE,
        sort: SortingRules = SortingRules.BEST_MATCH,
        isWithImages: Boolean = false
    ) {
        viewModelScope.launch {
            reviewRepository.getReviews(
                menuPairId = menuPairId,
                pageNumber = pageNumber,
                pageSize = pageSize,
                sort = sort.toString(),
                isWithImages = isWithImages
            ).handleResponse(
                onSuccess = { reviews ->
                    _uiState.update {
                        it.copy(reviews = reviews)
                    }
                },
                onError = { error ->
                    emitError(error)
                    viewModelScope.launch {
                        _eventFlow.emit(MenuDetailUiEvent.ShowToast(
                            error.message ?: "리뷰를 불러오는 중 오류가 발생했습니다."
                        ))
                    }
                }
            )
        }
    }

    fun getReviewImages(
        menuPairId: Long,
        pageNumber: Int = DEFAULT_PAGE_NUMBER,
        pageSize: Int = DEFAULT_PAGE_SIZE
    ) {
        viewModelScope.launch {
            reviewRepository.getReviewImages(
                menuPairId = menuPairId,
                pageNumber = pageNumber,
                pageSize = pageSize
            ).handleResponse(
                onSuccess = { reviewImageList ->
                    _uiState.update {
                        it.copy(reviewImages = reviewImageList.reviewImageDetailList)
                    }
                },
                onError = { error ->
                    emitError(error)
                    viewModelScope.launch {
                        _eventFlow.emit(MenuDetailUiEvent.ShowToast(
                            error.message ?: "리뷰 이미지를 불러오는 중 오류가 발생했습니다."
                        ))
                    }
                }
            )
        }
    }

    fun getMoreReviewImages(
        menuPairId: Long,
        pageNumber: Int = 0,
        pageSize: Int = DEFAULT_PAGE_SIZE
    ) {
        viewModelScope.launch {
            reviewRepository.getReviewImages(
                menuPairId = menuPairId,
                pageNumber = pageNumber,
                pageSize = pageSize
            ).handleResponse(
                onSuccess = { reviewImageList ->
                    _uiState.update { currentState ->
                        val updatedImages = if (pageNumber == 0) {
                            reviewImageList.reviewImageDetailList
                        } else {
                            (currentState.reviewImages ?: emptyList()) + reviewImageList.reviewImageDetailList
                        }
                        currentState.copy(
                            reviewImages = updatedImages
                        )
                    }
                },
                onError = { error ->
                    emitError(error)
                    viewModelScope.launch {
                        _eventFlow.emit(MenuDetailUiEvent.ShowToast(
                            error.message ?: "리뷰 이미지를 불러오는 중 오류가 발생했습니다."
                        ))
                    }
                }
            )
        }
    }

    fun toggleReviewLiked(reviewId: Long) {
        viewModelScope.launch {
            reviewRepository.toggleReviewLiked(reviewId = reviewId).handleResponse(
                onSuccess = { isLiked ->
                    Log.d("toggleReviewLiked", isLiked.toString())
                    _uiState.update { currentState ->
                        val updatedReviews = currentState.reviews?.copy(
                            reviewDetailList = currentState.reviews.reviewDetailList.map { review ->
                                if (review.reviewId == reviewId) {
                                    // Toggle the isLiked status and update likeCount
                                    review.copy(
                                        liked = isLiked,
                                        likeCount = if (!review.liked) review.likeCount + 1 else review.likeCount - 1
                                    )
                                } else {
                                    review
                                }
                            }
                        )
                        currentState.copy(
                            reviews = updatedReviews
                        )
                    }
                },
                onError = { error ->
                    // 에러 발생 시 UI 상태를 업데이트하지 않고 오류 메시지만 표시
                    emitError(error)
                }
            )
        }
    }

    fun toggleMenuLiked(mainMenuId: Long) {
        viewModelScope.launch {
            setLoading(true)
            menuRepository.toggleMenuLiked(mainMenuId = mainMenuId).handleResponse(
                onSuccess = { isLiked ->
                    _uiState.update { currentState ->
                        val selectedMenu = currentState.selectedMenu?.copy(
                            likedMenu = isLiked
                        )
                        currentState.copy(
                            selectedMenu = selectedMenu
                        )
                    }
                    setLoading(false)
                },
                onError = { error ->
                    emitError(error)
                    viewModelScope.launch {
                        _eventFlow.emit(MenuDetailUiEvent.ShowToast(
                            error.message ?: "메뉴 좋아요 처리 중 오류가 발생했습니다."
                        ))
                    }
                    setLoading(false)
                }
            )
        }
    }

    fun postReport(reviewId: Long, reportRequest: ReportRequest) {
        viewModelScope.launch {
            reviewRepository.postReport(reviewId, reportRequest).handleResponse(
                onSuccess = {
                    // 성공 시 - 다이얼로그가 자동으로 표시됨
                    _eventFlow.emit(MenuDetailUiEvent.ShowToast("신고가 성공적으로 처리되었습니다"))
                },
                onError = { error ->
                    // 실패 시 - 구체적인 에러 메시지와 함께 실패 다이얼로그 표시
                    val errorMessage = when {
                        error.message?.contains("이미") == true -> "이미 신고한 리뷰입니다"
                        error.message?.contains("권한") == true -> "신고 권한이 없습니다"
                        error.message?.contains("네트워크") == true -> "네트워크 연결을 확인해주세요"
                        error.message?.contains("서버") == true -> "서버 오류가 발생했습니다"
                        else -> "신고 처리 중 오류가 발생했습니다"
                    }

                    _eventFlow.emit(MenuDetailUiEvent.ShowToast(errorMessage))
                }
            )
        }
    }
    fun resetState() {
        _uiState.update { MenuDetailUiState() }
    }
}