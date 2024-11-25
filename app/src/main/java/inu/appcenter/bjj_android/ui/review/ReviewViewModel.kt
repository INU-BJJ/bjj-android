package inu.appcenter.bjj_android.ui.review

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import inu.appcenter.bjj_android.model.review.MyReviewRes
import inu.appcenter.bjj_android.model.review.ReviewRes
import inu.appcenter.bjj_android.model.todaydiet.TodayDietRes
import inu.appcenter.bjj_android.repository.cafeterias.CafeteriasRepository
import inu.appcenter.bjj_android.repository.review.ReviewRepository
import inu.appcenter.bjj_android.repository.todaydiet.TodayDietRepository
import inu.appcenter.bjj_android.ui.main.MainError
import inu.appcenter.bjj_android.ui.menudetail.ReviewError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Response

private const val DEFAULT_PAGE_SIZE = 10
private const val DEFAULT_PAGE_NUMBER = 0

data class ReviewUiState(
    val selectedRestaurant : String? = null,
    val reviews: MyReviewRes? = null,
    val reviewsChoiceByRestaurant: ReviewRes? = null,
    val selectedRestaurantAtReviewWrite : String? = null,
    val restaurants : List<String> = emptyList(),
    val selectedMenu: TodayDietRes? = null,
    val menus: List<TodayDietRes> = emptyList(),
    val isWithImages: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)


class ReviewViewModel(
    private val reviewRepository: ReviewRepository,
    private val cafeteriasRepository: CafeteriasRepository,
    private val todayDietRepository: TodayDietRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReviewUiState())
    val uiState = _uiState.asStateFlow()


    init {
        showAllRestaurant()
        getMyReviews()
    }

    fun setSelectedRestaurant(restaurant: String) {
        _uiState.update {
            it.copy(
                selectedRestaurant = restaurant,
                reviewsChoiceByRestaurant = null //
            )
        }
        getMoreReviewsByCafeteria(restaurant)
    }

    fun resetSelectedRestaurant() {
        _uiState.update {
            it.copy(
                selectedRestaurant = null
            )
        }
    }

    fun setSelectedReviewRestaurant(restaurant: String) {
        _uiState.update {
            it.copy(
                selectedRestaurantAtReviewWrite = restaurant,
            )
        }
    }

    fun resetSelectedReviewRestaurant() {
        _uiState.update {
            it.copy(
                selectedRestaurantAtReviewWrite = null
            )
        }
    }

    fun setSelectedMenu(menu: TodayDietRes) {
        _uiState.update {
            it.copy(
                selectedMenu = menu,
            )
        }
    }

    fun resetSelectedMenu() {
        _uiState.update {
            it.copy(
                selectedRestaurantAtReviewWrite = null
            )
        }
    }



    private fun getMyReviews() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val response = reviewRepository.getMyReviews()
                if (response.isSuccessful) {
                    val myReviews = response.body() ?: throw ReviewError.EmptyResponse()
                    _uiState.update { currentState ->
                        currentState.copy(
                            reviews = myReviews,
                            isLoading = false
                        )
                    }
                } else {
                    throw ReviewError.ApiError(response.errorBody()?.string() ?: "getMyReviews API Error")
                }
            } catch (e: Exception) {
                handleReviewError(e)
            }
        }
    }

    private suspend fun fetchReviewsByCafeteria(
        cafeteriaName: String,
        pageNumber: Int = DEFAULT_PAGE_NUMBER,
        pageSize: Int = DEFAULT_PAGE_SIZE,
    ): Response<ReviewRes> {
        return reviewRepository.getMyReviewsByCafeteria(
            cafeteriaName = cafeteriaName,
            pageNumber = pageNumber,
            pageSize = pageSize,
        )
    }

    fun getMoreReviewsByCafeteria(
        cafeteriaName: String,
        pageNumber: Int = DEFAULT_PAGE_NUMBER,
        pageSize: Int = DEFAULT_PAGE_SIZE,
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val response = fetchReviewsByCafeteria(cafeteriaName, pageNumber, pageSize)
                if (response.isSuccessful) {
                    val moreReadReviews = response.body() ?: throw ReviewError.EmptyResponse()
                    _uiState.update { currentState ->
                        currentState.copy(
                            reviewsChoiceByRestaurant = ReviewRes(
                                reviewDetailList = moreReadReviews.reviewDetailList,
                                lastPage = moreReadReviews.lastPage
                            ),
                            isLoading = false
                        )
                    }
                } else {
                    throw ReviewError.ApiError(response.errorBody()?.string() ?: "getMyReviewsByCafeteria API Error")
                }
            } catch (e: Exception) {
                handleReviewError(e)
            }
        }
    }

    private fun showAllRestaurant(){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try{
                val response = cafeteriasRepository.getCafeterias()
                if (response.isSuccessful) {
                    val restaurants = response.body() ?: throw ReviewError.EmptyResponse()
                    _uiState.update { currentState ->
                        currentState.copy(
                            restaurants = restaurants,
                            isLoading = false
                        )
                    }
                } else {
                    throw ReviewError.ApiError(response.errorBody()?.string() ?: "Unknown API Error")
                }
            }catch (e: Exception){
                handleReviewError(e)
            }
        }
    }


    fun getMenusByCafeteria(cafeteriaName: String) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = null) }

                val response = todayDietRepository.getTodayDiet(cafeteriaName)

                if (response.isSuccessful) {
                    val menus = response.body() ?: throw MainError.EmptyResponse("식당 메뉴 정보가 비어있습니다.")
                    _uiState.update { it.copy(menus = menus, isLoading = false) }
                } else {
                    throw MainError.ApiError(response.errorBody()?.string() ?: "Unknown API Error")
                }
            } catch (e: Exception) {
                handleReviewError(e)
            }
        }
    }


    private fun handleReviewError(e: Exception) {
        val errorMessage = when (e) {
            is ReviewError.EmptyResponse -> e.message
            is ReviewError.ApiError -> "API 오류: ${e.message}"
            is ReviewError.NetworkError -> "네트워크 오류: ${e.message}"
            else -> "알 수 없는 오류: ${e.message}"
        }
        Log.e("ReviewError", errorMessage)
        _uiState.update { it.copy(isLoading = false, error = errorMessage) }
    }



}