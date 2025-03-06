package inu.appcenter.bjj_android.ui.menudetail

import androidx.lifecycle.viewModelScope
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
    val moreReviewImages: List<ReviewImageDetail>? = null
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
                            )
                        )
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
                onSuccess = { isSuccess ->
                    if (isSuccess) {
                        _uiState.update { currentState ->
                            val updatedReviews = currentState.reviews?.copy(
                                reviewDetailList = currentState.reviews.reviewDetailList.map { review ->
                                    if (review.reviewId == reviewId) {
                                        // Toggle the isLiked status and update likeCount
                                        review.copy(
                                            liked = !review.liked,
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
                    }
                },
                onError = { error ->
                    // 에러 발생 시 UI 상태를 업데이트하지 않고 오류 메시지만 표시
                    emitError(error)
                    // 단일 이벤트로 토스트 메시지 전달
                    viewModelScope.launch {
                        _eventFlow.emit(MenuDetailUiEvent.ShowToast(
                            error.message ?: "좋아요 처리 중 오류가 발생했습니다."
                        ))
                    }
                }
            )
        }
    }

    fun toggleMenuLiked(mainMenuId: Long) {
        viewModelScope.launch {
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
                },
                onError = { error ->
                    emitError(error)
                    viewModelScope.launch {
                        _eventFlow.emit(MenuDetailUiEvent.ShowToast(
                            error.message ?: "메뉴 좋아요 처리 중 오류가 발생했습니다."
                        ))
                    }
                }
            )
        }
    }

    fun resetState() {
        _uiState.update { MenuDetailUiState() }
    }
}