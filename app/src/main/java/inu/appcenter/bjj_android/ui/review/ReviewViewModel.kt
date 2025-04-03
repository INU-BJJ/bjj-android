package inu.appcenter.bjj_android.ui.review

import android.util.Log
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
import inu.appcenter.bjj_android.utils.AppError
import inu.appcenter.bjj_android.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

private const val PAGE_SIZE = 10

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
    val isWithImages: Boolean = false
)

data class ReviewDetailUiState(
    val reviewDetail: ReviewDetailRes? = null,
    val imageNames: List<String> = emptyList(),
    val selectedImageIndex: Int = 0
)

class ReviewViewModel(
    private val reviewRepository: ReviewRepository,
    private val cafeteriasRepository: CafeteriasRepository,
    private val todayDietRepository: TodayDietRepository
) : BaseViewModel() {

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
                selectedMenu = null,
                menus = emptyList()
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

    fun getMyReviews() {
        viewModelScope.launch {
            reviewRepository.getMyReviews().handleResponse(
                onSuccess = { myReviews ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            reviews = myReviews
                        )
                    }
                }
            )
        }
    }

    fun getMoreReviewsByCafeteria(cafeteriaName: String) {
        if (_uiState.value.reviewsChoiceByRestaurant?.lastPage == true) return

        viewModelScope.launch {
            reviewRepository.getMyReviewsByCafeteria(
                cafeteriaName = cafeteriaName,
                pageNumber = currentPageNumber,
                pageSize = PAGE_SIZE
            ).handleResponse(
                onSuccess = { pagedReviews ->
                    _uiState.update { state ->
                        val oldList = state.reviewsChoiceByRestaurant?.myReviewDetailList ?: emptyList()
                        val newList = oldList + pagedReviews.myReviewDetailList

                        state.copy(
                            reviewsChoiceByRestaurant = MyReviewsPagedRes(
                                myReviewDetailList = newList,
                                lastPage = pagedReviews.lastPage
                            )
                        )
                    }
                    currentPageNumber++
                }
            )
        }
    }

    private fun showAllRestaurant() {
        viewModelScope.launch {
            cafeteriasRepository.getCafeterias().handleResponse(
                onSuccess = { restaurants ->
                    _uiState.update {
                        it.copy(restaurants = restaurants)
                    }
                }
            )
        }
    }

    // 식당에 대한 메뉴 불러오기(리뷰 작성 드롭다운 메뉴부분)
    fun getMenusByCafeteria(cafeteriaName: String) {
        viewModelScope.launch {
            todayDietRepository.getTodayDiet(cafeteriaName).handleResponse(
                onSuccess = { menus ->
                    _uiState.update {
                        it.copy(menus = menus)
                    }
                }
            )
        }
    }

    // 리뷰 작성하기
    fun reviewComplete(reviewPost: ReviewPost, images: List<String?>, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                setLoading(true)

                val gson = Gson()
                val reviewPostJson = gson.toJson(reviewPost)
                Log.d("ReviewViewModel", "ReviewPost JSON: $reviewPostJson")

                // reviewPost를 RequestBody로 생성
                val reviewPostRequestBody = reviewPostJson.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

                // files 파트 생성
                val files: List<MultipartBody.Part>? = if (images.isNotEmpty()) {
                    images.mapNotNull { imagePath ->
                        imagePath?.let {
                            val imageFile = File(it)
                            if (imageFile.exists()) {
                                val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
                                MultipartBody.Part.createFormData("files", imageFile.name, requestFile)
                            } else {
                                Log.e("ReviewViewModel", "Image file does not exist: $imagePath")
                                null
                            }
                        }
                    }
                } else {
                    null
                }

                reviewRepository.postReview(reviewPostRequestBody, files).handleResponse(
                    onSuccess = {
                        setImageNames(images.filterNotNull())
                        getMyReviews()
                        onSuccess()
                    },
                    onError = { error ->
                        emitError(error)
                    }
                )
            } catch (e: Exception) {
                setLoading(false)
                emitError(AppError.NotFoundError(e.message ?: "리뷰 작성 중 오류가 발생했습니다."))
            }
        }
    }

    // 리뷰 삭제하기
    fun deleteReview(reviewId: Long, onSuccess: () -> Unit) {
        viewModelScope.launch {
            reviewRepository.deleteReview(reviewId).handleResponse(
                onSuccess = {
                    Log.d("deleteReviewSuccess", "deleteReviewSuccess")
                    getMyReviews()
                    onSuccess()
                }
            )
        }
    }

    // 리뷰 상세 받기
    fun getReviewDetail(reviewId: Long) {
        viewModelScope.launch {
            reviewRepository.getReviewDetail(reviewId).handleResponse(
                onSuccess = { reviewDetail ->
                    _reviewDetailUiState.update {
                        it.copy(
                            reviewDetail = reviewDetail,
                            imageNames = reviewDetail.imageNames,
                            selectedImageIndex = 0
                        )
                    }
                }
            )
        }
    }

    fun resetState(){
        _uiState.update { ReviewUiState() }
    }
}