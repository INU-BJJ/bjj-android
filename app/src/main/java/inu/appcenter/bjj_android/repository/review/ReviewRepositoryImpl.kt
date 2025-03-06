package inu.appcenter.bjj_android.repository.review


import inu.appcenter.bjj_android.model.review.MyReviewsGroupedRes
import inu.appcenter.bjj_android.model.review.MyReviewsPagedRes
import inu.appcenter.bjj_android.model.review.ReviewDetailRes
import inu.appcenter.bjj_android.model.review.ReviewImageDetailList
import inu.appcenter.bjj_android.model.review.ReviewRes
import inu.appcenter.bjj_android.network.APIService
import inu.appcenter.bjj_android.utils.CustomResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ReviewRepositoryImpl(private val apiService: APIService) : ReviewRepository {

    override suspend fun getReviews(
        menuPairId: Long,
        pageNumber: Int,
        pageSize: Int,
        sort: String,
        isWithImages: Boolean
    ): Flow<CustomResponse<ReviewRes>> = safeApiCall {
        apiService.getReviews(
            menuPairId = menuPairId,
            pageNumber = pageNumber,
            pageSize = pageSize,
            sort = sort,
            isWithImages = isWithImages
        )
    }

    override suspend fun postReview(
        reviewPost: RequestBody,
        files: List<MultipartBody.Part>?
    ): Flow<CustomResponse<Unit>> = safeApiCall {
        apiService.postReview(reviewPost = reviewPost, files = files)
    }

    override suspend fun deleteReview(reviewId: Long): Flow<CustomResponse<Unit>> = safeApiCall {
        apiService.deleteReview(reviewId = reviewId)
    }

    override suspend fun getMyReviews(): Flow<CustomResponse<MyReviewsGroupedRes>> = safeApiCall {
        apiService.getMyReviews()
    }

    override suspend fun getMyReviewsByCafeteria(
        cafeteriaName: String,
        pageNumber: Int,
        pageSize: Int
    ): Flow<CustomResponse<MyReviewsPagedRes>> = safeApiCall {
        apiService.getMyReviewsByCafeteria(
            cafeteriaName = cafeteriaName,
            pageNumber = pageNumber,
            pageSize = pageSize
        )
    }

    override suspend fun toggleReviewLiked(reviewId: Long): Flow<CustomResponse<Boolean>> = safeApiCall {
        apiService.toggleReviewLiked(reviewId = reviewId)
    }

    override suspend fun getReviewImages(
        menuPairId: Long,
        pageNumber: Int,
        pageSize: Int
    ): Flow<CustomResponse<ReviewImageDetailList>> = safeApiCall {
        apiService.getReviewImages(menuPairId, pageNumber, pageSize)
    }

    override suspend fun getReviewDetail(reviewId: Long): Flow<CustomResponse<ReviewDetailRes>> = safeApiCall {
        apiService.getReviewDetail(reviewId)
    }
}