package inu.appcenter.bjj_android.ui.review

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import inu.appcenter.bjj_android.model.review.MyReviewDetailRes
import inu.appcenter.bjj_android.model.review.MyReviewsGroupedRes
import inu.appcenter.bjj_android.model.review.MyReviewsPagedRes
import inu.appcenter.bjj_android.model.review.ReviewPost
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File

private const val DEFAULT_PAGE_SIZE = 10
private const val DEFAULT_PAGE_NUMBER = 0

data class ReviewUiState(
    val selectedRestaurant : String? = null,
    val reviews: MyReviewsGroupedRes? = null,
    val reviewsChoiceByRestaurant: MyReviewsPagedRes? = null,
    val selectedRestaurantAtReviewWrite : String? = null,
    val restaurants : List<String> = emptyList(),
    val selectedMenu: TodayDietRes? = null,
    val menus: List<TodayDietRes> = emptyList(),
    val selectedReviewDetail: MyReviewDetailRes? = null,
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

    // 더보기
    fun setSelectedRestaurant(restaurant: String) {
        _uiState.update {
            it.copy(
                selectedRestaurant = restaurant,
                reviewsChoiceByRestaurant = null
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

    // 리뷰 작성
    fun setSelectedReviewRestaurant(restaurant: String) {
        _uiState.update {
            it.copy(
                selectedRestaurantAtReviewWrite = restaurant,
                selectedMenu = null, // 선택된 메뉴 초기화
                menus = emptyList()  // 메뉴 리스트 초기화
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
                selectedMenu = null,
                menus = emptyList()
            )
        }
    }

    // 선택된 리뷰 상세 정보를 관리하는 변수 추가
    fun setSelectedReviewDetail(reviewDetail: MyReviewDetailRes) {
        _uiState.update { it.copy(selectedReviewDetail = reviewDetail) }
    }

    fun resetSelectedReviewDetail() {
        _uiState.update { it.copy(selectedReviewDetail = null) }
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
    ): Response<MyReviewsPagedRes> {
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
                            reviewsChoiceByRestaurant = MyReviewsPagedRes(
                                myReviewDetailList = moreReadReviews.myReviewDetailList,
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
                    throw ReviewError.ApiError(response.errorBody()?.string() ?: "Restaurant API Error")
                }
            }catch (e: Exception){
                handleReviewError(e)
            }
        }
    }

    // 식당에 대한 메뉴 불러오기(리뷰 작성 드롭다운 메뉴부분)
    fun getMenusByCafeteria(cafeteriaName: String) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = null) }

                val response = todayDietRepository.getTodayDiet(cafeteriaName)

                if (response.isSuccessful) {
                    val menus = response.body() ?: throw MainError.EmptyResponse("식당 메뉴 정보가 비어있습니다.")
                    _uiState.update { it.copy(menus = menus, isLoading = false) }
                } else {
                    throw ReviewError.ApiError(response.errorBody()?.string() ?: "Unknown API Error")
                }
            } catch (e: Exception) {
                handleReviewError(e)
            }
        }
    }

    // 리뷰 작성하기
    fun reviewComplete(reviewPost: ReviewPost, images: List<String>, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = null) }

                val gson = Gson()
                val reviewPostJson = gson.toJson(reviewPost)
                val reviewPostRequestBody = reviewPostJson.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

                val files: List<MultipartBody.Part>? = if (images.isNotEmpty()) {
                    images.map { imagePath ->
                        val imageFile = File(imagePath)
                        val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
                        MultipartBody.Part.createFormData("files", imageFile.name, requestFile)
                    }
                } else {
                    null
                }

                val response = reviewRepository.postReview(reviewPostRequestBody, files)

                if (response.isSuccessful) {
                    getMyReviews()
                    _uiState.update { it.copy(isLoading = false) }
                    onSuccess()
                } else {
                    throw ReviewError.ApiError(response.errorBody()?.string() ?: "reviewWrite API Error")
                }
            } catch (e: Exception) {
                handleReviewError(e)
            }
        }
    }

    // 리뷰 삭제하기
    fun deleteReview(reviewId: Long, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val response = reviewRepository.deleteReview(reviewId)
                if (response.isSuccessful) {
                    getMyReviews()
                    _uiState.update { it.copy(isLoading = false) }
                    onSuccess()
                } else {
                    throw ReviewError.ApiError(response.errorBody()?.string() ?: "deleteReview API Error")
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