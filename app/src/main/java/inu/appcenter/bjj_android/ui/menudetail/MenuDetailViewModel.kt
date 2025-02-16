package inu.appcenter.bjj_android.ui.menudetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import inu.appcenter.bjj_android.model.review.ReviewImageDetail
import inu.appcenter.bjj_android.model.review.ReviewRes
import inu.appcenter.bjj_android.model.todaydiet.TodayDietRes
import inu.appcenter.bjj_android.repository.menu.MenuRepository
import inu.appcenter.bjj_android.repository.review.ReviewRepository
import inu.appcenter.bjj_android.repository.todaydiet.TodayDietRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Response

private const val DEFAULT_PAGE_SIZE = 10
private const val DEFAULT_PAGE_NUMBER = 0

sealed class ReviewError : Exception() {
    data class EmptyResponse(override val message: String = "리뷰 정보가 비어있습니다.") : ReviewError()
    data class ApiError(override val message: String) : ReviewError()
    data class NetworkError(override val message: String) : ReviewError()
}

data class MenuDetailUiState(
    val selectedMenu: TodayDietRes? = null,
    val reviews: ReviewRes? = null,
    val isWithImages: Boolean = false,
    val sort: SortingRules = SortingRules.BEST_MATCH,
    val reviewImages: List<ReviewImageDetail>? = null,
    val moreReviewImages: List<ReviewImageDetail>? = null,
    val isLoading: Boolean = false,
    val error: String? = null
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

// UiEvent sealed class
sealed class MenuDetailUiEvent {
    data class ShowToast(val message: String) : MenuDetailUiEvent()
}

class MenuDetailViewModel(
    private val todayDietRepository: TodayDietRepository,
    private val reviewRepository: ReviewRepository,
    private val menuRepository: MenuRepository
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<MenuDetailUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _uiState = MutableStateFlow(MenuDetailUiState())
    val uiState = _uiState.asStateFlow()

    fun selectMenu(menu: TodayDietRes) {
        _uiState.update {
            it.copy(
                selectedMenu = menu,
                error = null,
                isWithImages = false,  // 초기화
                sort = SortingRules.BEST_MATCH  // 초기화
            )
        }
        getReviewsByMenu(menu.menuPairId)
    }

    fun selectIsWithImages(isWithImages: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                isWithImages = isWithImages,
                error = null
            )
        }
    }

    fun selectSortingRule(sort: SortingRules) {
        _uiState.update { currentState ->
            currentState.copy(
                sort = sort,
                error = null
            )
        }
    }

//    fun selectIsWithImages(
//        isWithImages: Boolean
//    ) {
//        _uiState.update { currentState ->
//            currentState.copy(
//                isWithImages = isWithImages,
//                error = null
//            ).also { newState ->
//                newState.selectedMenu?.menuPairId?.let { menuPairId ->
//                    getReviewsByMenu(
//                        menuPairId = menuPairId,
//                        isWithImages = newState.isWithImages,
//                        sort = newState.sort
//                    )
//                }
//            }
//        }
//    }
//
//    fun selectSortingRule(sort: SortingRules) {
//        _uiState.update { currentState ->
//            currentState.copy(
//                sort = sort,
//                error = null
//            ).also { newState ->
//                newState.selectedMenu?.menuPairId?.let { menuPairId ->
//                    getReviewsByMenu(
//                        menuPairId = menuPairId,
//                        isWithImages = newState.isWithImages,
//                        sort = sort
//                    )
//                }
//            }
//        }
//    }

    private suspend fun fetchReviews(
        menuPairId: Long,
        pageNumber: Int = DEFAULT_PAGE_NUMBER,
        pageSize: Int = DEFAULT_PAGE_SIZE,
        sort: SortingRules = SortingRules.BEST_MATCH,
        isWithImages: Boolean = false
    ): Response<ReviewRes> {
        return reviewRepository.getReviews(
            menuPairId = menuPairId,
            pageNumber = pageNumber,
            pageSize = pageSize,
            sort = sort.toString(),
            isWithImages = isWithImages
        )
    }



    fun getMoreReviewsByMenu(
        menuPairId: Long,
        pageNumber: Int = DEFAULT_PAGE_NUMBER,
        pageSize: Int = DEFAULT_PAGE_SIZE,
        sort: SortingRules = SortingRules.BEST_MATCH,
        isWithImages: Boolean = false
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val response = fetchReviews(menuPairId, pageNumber, pageSize, sort, isWithImages)
                if (response.isSuccessful) {
                    val moreReviews = response.body() ?: throw ReviewError.EmptyResponse()
                    _uiState.update { currentState ->
                        currentState.copy(
                            reviews = ReviewRes(
                                reviewDetailList = (currentState.reviews?.reviewDetailList ?: emptyList()) + moreReviews.reviewDetailList,
                                lastPage = moreReviews.lastPage
                            ),
                            isLoading = false
                        )
                    }
                } else {
                    throw ReviewError.ApiError(response.errorBody()?.string() ?: "Unknown API Error")
                }
            } catch (e: Exception) {
                handleReviewError(e)
            }
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
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val response = fetchReviews(menuPairId, pageNumber, pageSize, sort, isWithImages)
                if (response.isSuccessful) {
                    val reviews = response.body() ?: throw ReviewError.EmptyResponse()
                    _uiState.update { it.copy(reviews = reviews, isLoading = false) }
                } else {
                    throw ReviewError.ApiError(response.errorBody()?.string() ?: "Unknown API Error")
                }
            } catch (e: Exception) {
                handleReviewError(e)
            }
        }
    }

    fun getReviewImages(
        menuPairId: Long,
        pageNumber: Int = DEFAULT_PAGE_NUMBER,
        pageSize: Int = DEFAULT_PAGE_SIZE,
        sort: SortingRules = SortingRules.BEST_MATCH,
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val response = reviewRepository.getReviewImages(menuPairId, pageNumber, pageSize)
                if (response.isSuccessful) {
                    val reviewImages = response.body() ?: throw ReviewError.EmptyResponse()
                    _uiState.update { it.copy(reviewImages = reviewImages.reviewImageDetailList, isLoading = false) }
                } else {
                    throw ReviewError.ApiError(response.errorBody()?.string() ?: "Unknown API Error")
                }
            } catch (e: Exception) {
                handleReviewError(e)
            }
        }
    }

    fun getMoreReviewImages(
        menuPairId: Long,
        pageNumber: Int = 0,
        pageSize: Int = DEFAULT_PAGE_SIZE,
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val response = reviewRepository.getReviewImages(menuPairId, pageNumber, pageSize)
                if (response.isSuccessful) {
                    val newImages = response.body()?.reviewImageDetailList ?: emptyList()
                    _uiState.update { currentState ->
                        val updatedImages = if (pageNumber == 0) {
                            newImages
                        } else {
                            (currentState.reviewImages ?: emptyList()) + newImages
                        }
                        currentState.copy(
                            reviewImages = updatedImages,
                            isLoading = false,
                            error = null
                        )
                    }
                } else {
                    throw ReviewError.ApiError(response.errorBody()?.string() ?: "Unknown API Error")
                }
            } catch (e: Exception) {
                handleReviewError(e)
            }
        }
    }

    fun toggleReviewLiked(
        reviewId: Long
    ){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val response = reviewRepository.toggleReviewLiked(reviewId = reviewId)
                if (response.isSuccessful) {
                    val isSuccess = response.body() ?: throw ReviewError.EmptyResponse()
                    if (isSuccess){
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
                                    reviews = updatedReviews,
                                    isLoading = false
                                )
                            }
                        }
                    }
                } else {
                    throw ReviewError.ApiError(response.errorBody()?.string() ?: "Unknown API Error")
                }
            } catch (e: Exception) {
                handleReviewError(e)
            }
        }
    }

    fun toggleMenuLiked(
        mainMenuId: Long
    ){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val response = menuRepository.toggleMenuLiked(mainMenuId = mainMenuId)
                if (response.isSuccessful) {
                    val isLiked = response.body() ?: throw ReviewError.EmptyResponse()
                    _uiState.update { currentState ->
                        val selectedMenu = currentState.selectedMenu?.copy(
                            likedMenu = isLiked
                        )
                        currentState.copy(
                            selectedMenu = selectedMenu,
                            isLoading = false
                        )
                    }
                } else {
                    throw ReviewError.ApiError(response.errorBody()?.string() ?: "Unknown API Error")
                }
            } catch (e: Exception) {
                handleReviewError(e)
            }
        }
    }

    fun resetState() {
        _uiState.update { MenuDetailUiState() }
    }



    private fun handleReviewError(e: Exception) {
        val errorMessage = when (e) {
            is ReviewError.EmptyResponse -> e.message
            is ReviewError.ApiError -> {
                // 서버에서 온 에러 메시지를 파싱
                try {
                    val errorBody = e.message
                    if (errorBody.contains("msg")) {
                        // JSON 파싱하여 실제 메시지 추출
                        val regex = "\"msg\":\"(.*?)\"".toRegex()
                        val matchResult = regex.find(errorBody)
                        matchResult?.groupValues?.get(1) ?: "서버 오류가 발생했습니다"
                    } else {
                        "서버 오류가 발생했습니다"
                    }
                } catch (e: Exception) {
                    "서버 오류가 발생했습니다"
                }
            }
            is ReviewError.NetworkError -> "네트워크 연결을 확인해주세요"
            else -> "알 수 없는 오류가 발생했습니다"
        }

        viewModelScope.launch {
            _eventFlow.emit(MenuDetailUiEvent.ShowToast(errorMessage))
        }
        _uiState.update { it.copy(isLoading = false, error = errorMessage) }
    }
}