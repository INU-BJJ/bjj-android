package inu.appcenter.bjj_android.ui.menudetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import inu.appcenter.bjj_android.model.review.ReviewRes
import inu.appcenter.bjj_android.model.todaydiet.TodayDietRes
import inu.appcenter.bjj_android.repository.review.ReviewRepository
import inu.appcenter.bjj_android.repository.todaydiet.TodayDietRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MenuDetailUiState(
    val selectedMenu: TodayDietRes? = null,
    val reviews: ReviewRes? = null,
    val isWithImages: Boolean = false,
    val sort: SortingRules = SortingRules.BEST_MATCH,
    val isLoading: Boolean = false,
    val error: String? = null
)

enum class SortingRules {
    BEST_MATCH,
    MOST_LIKED,
    NEWEST_FIRST;

    override fun toString(): String {
        return name
    }
    fun toKorean(): String {
        return when(name){
            BEST_MATCH.name->{
                "메뉴일치순"
            }
            MOST_LIKED.name->{
                "좋아요순"
            }
            NEWEST_FIRST.name->{
                "최신순"
            }
            else->{
                ""
            }
        }
    }
}

class MenuDetailViewModel(private val todayDietRepository: TodayDietRepository, private val reviewRepository: ReviewRepository): ViewModel() {

    private val _uiState = MutableStateFlow(MenuDetailUiState())
    val uiState = _uiState.asStateFlow()

    fun selectMenu(menu: TodayDietRes){
        _uiState.update { it.copy(selectedMenu = menu)}
        getReviewsByMenu(menu.menuPairId)


    }

    fun selectIsWithImages(
        isWithImages: Boolean
    ) {
        _uiState.update {
            it.copy(isWithImages = isWithImages).also { newState ->
                getReviewsByMenu(
                    menuPairId = newState.selectedMenu?.menuPairId ?: return,
                    isWithImages = newState.isWithImages,
                    sort = newState.sort
                )
            }
        }
    }

    fun selectSortingRule(sort: SortingRules) {
        _uiState.update { currentState ->
            currentState.copy(sort = sort).also { newState ->
                getReviewsByMenu(
                    menuPairId = newState.selectedMenu?.menuPairId ?: return,
                    isWithImages = newState.isWithImages,
                    sort = sort
                )
            }
        }
    }

    fun getMoreReviewsByMenu(
        menuPairId : Long,
        pageNumber : Int = 0,
        pageSize: Int = 10,
        sort: SortingRules = SortingRules.BEST_MATCH,
        isWithImages: Boolean = false
    ){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val response = reviewRepository.getReviews(
                    menuPairId = menuPairId,
                    pageNumber = pageNumber,
                    pageSize = pageSize,
                    sort = sort.toString(),
                    isWithImages = isWithImages
                )

                if (response.isSuccessful){
                    val moreReviews = response.body() ?: throw Exception("더보기 리뷰 정보가 비어있습니다.")
                    _uiState.update { currentState ->
                        Log.d("currentState", currentState.reviews.toString())
                        currentState.copy(
                            reviews = ReviewRes(
                                reviewDetailList = (currentState.reviews?.reviewDetailList ?: emptyList()) + moreReviews.reviewDetailList,
                                lastPage = moreReviews.lastPage
                            ),
                            isLoading = moreReviews.lastPage
                        )
                    }
                } else {
                    throw Exception(response.errorBody()?.string())
                }
            } catch (e: Exception) {
                Log.e("getMoreReviewsByMenu 실패 원인", e.message.toString())
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "더보기 리뷰를 불러오는데 실패했습니다: ${e.message}"
                    )
                }
            }
        }
    }

    fun getReviewsByMenu(
        menuPairId : Long,
        pageNumber : Int = 0,
        pageSize: Int = 10,
        sort: SortingRules = SortingRules.BEST_MATCH,
        isWithImages: Boolean = false
    ){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val response = reviewRepository.getReviews(
                    menuPairId = menuPairId,
                    pageNumber = pageNumber,
                    pageSize = pageSize,
                    sort = sort.toString(),
                    isWithImages = isWithImages
                )

                if (response.isSuccessful){
                    val reviews = response.body() ?: throw Exception("리뷰 정보가 비어있습니다.")
                    _uiState.update { it.copy(reviews = reviews, isLoading = false) }
                } else {
                    throw Exception(response.errorBody()?.string())
                }
            } catch (e: Exception) {
                Log.e("getReviewsByMenu 실패 원인", e.message.toString())
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "리뷰를 불러오는데 실패했습니다: ${e.message}"
                    )
                }
            }
        }
    }




}