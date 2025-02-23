package inu.appcenter.bjj_android.ui.review

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import inu.appcenter.bjj_android.model.review.MyReviewDetailRes
import inu.appcenter.bjj_android.model.review.MyReviewsGroupedRes
import inu.appcenter.bjj_android.model.review.MyReviewsPagedRes
import inu.appcenter.bjj_android.model.review.ReviewDetailRes
import inu.appcenter.bjj_android.model.review.ReviewPost
import inu.appcenter.bjj_android.model.todaydiet.TodayDietRes
import inu.appcenter.bjj_android.repository.cafeterias.CafeteriasRepository
import inu.appcenter.bjj_android.repository.review.ReviewRepository
import inu.appcenter.bjj_android.repository.todaydiet.TodayDietRepository
import inu.appcenter.bjj_android.ui.main.MainError
import inu.appcenter.bjj_android.ui.menudetail.ReviewError
import inu.appcenter.bjj_android.utils.collectAndHandle
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

private const val PAGE_SIZE = 10
// private const val DEFAULT_PAGE_NUMBER = 0

data class ReviewUiState(
    val selectedRestaurant: String? = null,
    val reviews: MyReviewsGroupedRes? = null,
    val reviewsChoiceByRestaurant: MyReviewsPagedRes? = null,
    val selectedRestaurantAtReviewWrite: String? = null,
    val restaurants: List<String> = emptyList(),
    val selectedMenu: TodayDietRes? = null,
    val menus: List<TodayDietRes> = emptyList(),
    val selectedReviewDetail: MyReviewDetailRes? = null,
    val imageNames: List<String> = emptyList(),
    val selectedImageIndex: Int = 0,
    val isWithImages: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

data class ReviewDetailUiState(
    val reviewDetail: ReviewDetailRes? = null,
    val imageNames: List<String> = emptyList(),
    val selectedImageIndex: Int = 0,
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

    private val _reviewDetailUiState = MutableStateFlow(ReviewDetailUiState())
    val reviewDetailUiState = _reviewDetailUiState.asStateFlow()

    private var currentPageNumber = 0

    init {
        showAllRestaurant()
        getMyReviews()
    }

    // 더보기
    fun setSelectedRestaurant(restaurant: String) {
        currentPageNumber = 0

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
        _uiState.update {
            it.copy(
                selectedReviewDetail = reviewDetail
            )
        }
    }

//    fun resetSelectedReviewDetail() {
//        _uiState.update {
//            it.copy(
//                selectedReviewDetail = null
//            )
//        }
//    }

    // 이미지 목록 설정
    fun setImageNames(imageNames: List<String>) {
        _uiState.update {
            it.copy(
                imageNames = imageNames,
                selectedImageIndex = 0 // 첫 번째 이미지를 디폴트로 설정
            )
        }
    }

    // 특정 이미지를 선택했을 때 인덱스를 업데이트
    fun selectImageIndex(index: Int) {
        val totalImages = _uiState.value.imageNames.size
        if (index in 0 until totalImages) {
            _uiState.update { it.copy(selectedImageIndex = index) }
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
                    throw ReviewError.ApiError(
                        response.errorBody()?.string() ?: "getMyReviews API Error"
                    )
                }
            } catch (e: Exception) {
                handleReviewError(e)
            }
        }
    }

    private suspend fun fetchReviewsByCafeteria(
        cafeteriaName: String,
        pageNumber: Int,
        pageSize: Int = PAGE_SIZE,
    ): Response<MyReviewsPagedRes> {
        // 서버에서 pageNumber(몇 번째 페이지), pageSize(한 페이지 크기)로 데이터를 주는 구조
        return reviewRepository.getMyReviewsByCafeteria(
            cafeteriaName = cafeteriaName,
            pageNumber = pageNumber,
            pageSize = pageSize,
        )
    }

    fun getMoreReviewsByCafeteria(cafeteriaName: String) {
        viewModelScope.launch {
            // 이미 로딩중이거나 마지막 페이지면 중복 호출 방지
            if (_uiState.value.isLoading) return@launch
            val currentState = _uiState.value.reviewsChoiceByRestaurant
            if (currentState?.lastPage == true) {
                // 이미 마지막 페이지
                return@launch
            }

            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val response = fetchReviewsByCafeteria(cafeteriaName, currentPageNumber, PAGE_SIZE)
                if (response.isSuccessful) {
                    val newPageData = response.body() ?: throw ReviewError.EmptyResponse()

                    _uiState.update { state ->
                        val oldList = state.reviewsChoiceByRestaurant?.myReviewDetailList ?: emptyList()
                        val newList = newPageData.myReviewDetailList
                        val combinedList = oldList + newList

                        state.copy(
                            reviewsChoiceByRestaurant = MyReviewsPagedRes(
                                myReviewDetailList = combinedList,
                                lastPage = newPageData.lastPage
                            ),
                            isLoading = false
                        )
                    }

                    // 한 번 호출 끝났으니 페이지 번호 +1
                    currentPageNumber++

                } else {
                    throw ReviewError.ApiError(
                        response.errorBody()?.string() ?: "getMyReviewsByCafeteria API Error"
                    )
                }
            } catch (e: Exception) {
                handleReviewError(e)
            }
        }
    }

    private fun showAllRestaurant() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
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
                    throw ReviewError.ApiError(
                        response.errorBody()?.string() ?: "Restaurant API Error"
                    )
                }
            } catch (e: Exception) {
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
                    val menus =
                        response.body() ?: throw MainError.EmptyResponse("식당 메뉴 정보가 비어있습니다.")
                    _uiState.update { it.copy(menus = menus, isLoading = false) }
                } else {
                    throw ReviewError.ApiError(
                        response.errorBody()?.string() ?: "Unknown API Error"
                    )
                }
            } catch (e: Exception) {
                handleReviewError(e)
            }
        }
    }

    // 리뷰 작성하기
    fun reviewComplete(reviewPost: ReviewPost, images: List<String?>, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = null) }

                val gson = Gson()
                val reviewPostJson = gson.toJson(reviewPost)
                Log.e("ReviewViewModel", "ReviewPost JSON: $reviewPostJson")

                // reviewPost를 RequestBody로 생성
                val reviewPostRequestBody = reviewPostJson.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

                Log.d("ReviewViewModel", "Number of images: ${images.size}")
                images.forEach { imagePath ->
                    Log.d("ReviewViewModel", "Image path: $imagePath")
                }

                // files 파트 생성
                val files: List<MultipartBody.Part>? = if (images.isNotEmpty()) {
                    images.mapNotNull { imagePath ->
                        val imageFile = File(imagePath)
                        if (imageFile.exists()) {
                            val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
                            MultipartBody.Part.createFormData("files", imageFile.name, requestFile)
                        } else {
                            Log.e("ReviewViewModel", "Image file does not exist: $imagePath")
                            null
                        }
                    }
                } else {
                    null
                }

                Log.d("ReviewViewModel", "Posting review with files: $files")

                // 수정된 Retrofit 인터페이스에 맞게 요청
                val response = reviewRepository.postReview(reviewPostRequestBody, files)

                if (response.isSuccessful) {
                    // 이미지 목록 설정 및 인덱스 초기화
                    setImageNames(images.filterNotNull())
                    getMyReviews()
                    _uiState.update { it.copy(isLoading = false) }
                    onSuccess()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("ReviewViewModel", "API Error: $errorBody")
                    throw ReviewError.ApiError(errorBody ?: "reviewWrite API Error")
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
                    throw ReviewError.ApiError(
                        response.errorBody()?.string() ?: "deleteReview API Error"
                    )
                }
            } catch (e: Exception) {
                handleReviewError(e)
            }
        }
    }

    // 리뷰 상세 받기
    fun getReviewDetail(reviewId: Long) = viewModelScope.launch {
        _reviewDetailUiState.update { it.copy(isLoading = true, error = null) }

        reviewRepository.getReviewDetail(reviewId).collectAndHandle(
            onError = { error ->
                val errorMessage = when (error) {
                    is ReviewError.EmptyResponse -> error.message
                    is ReviewError.ApiError -> "API 오류: ${error.message}"
                    is ReviewError.NetworkError -> "네트워크 오류: ${error.message}"
                    else -> "알 수 없는 오류: ${error?.message ?: "null"}"
                }
                _reviewDetailUiState.update { it.copy(
                    isLoading = false,
                    error = errorMessage
                )}
            },
            onLoading = {
                _reviewDetailUiState.update { it.copy(isLoading = true) }
            },
            stateReducer = { reviewDetail ->
                _reviewDetailUiState.update { currentState ->
                    currentState.copy(
                        reviewDetail = reviewDetail,
                        imageNames = reviewDetail.imageNames,
                        selectedImageIndex = 0,
                        isLoading = false,
                        error = null
                    )
                }
            }
        )
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