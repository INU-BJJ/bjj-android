package inu.appcenter.bjj_android.repository.review

import inu.appcenter.bjj_android.model.review.MyReviewsGroupedRes
import inu.appcenter.bjj_android.model.review.MyReviewsPagedRes
import inu.appcenter.bjj_android.model.review.ReviewDetailRes
import inu.appcenter.bjj_android.model.review.ReviewImageDetailList
import inu.appcenter.bjj_android.model.review.ReviewRes
import inu.appcenter.bjj_android.utils.CustomResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ReviewRepository {

    suspend fun getReviews(
        menuPairId: Long,
        pageNumber: Int,
        pageSize: Int,
        sort: String,
        isWithImages: Boolean
    ) : Response<ReviewRes>

    suspend fun postReview(
        reviewPost: RequestBody,
        files: List<MultipartBody.Part>?
    ): Response<Unit>

    suspend fun deleteReview(
        reviewId: Long
    ) : Response<Unit>

    suspend fun getMyReviews() : Response<MyReviewsGroupedRes>

    suspend fun getMyReviewsByCafeteria(
        cafeteriaName: String,
        pageNumber: Int,
        pageSize: Int
    ) : Response<MyReviewsPagedRes>

    suspend fun toggleReviewLiked(
        reviewId: Long
    ) : Response<Boolean>

    suspend fun getReviewImages(
        menuPairId: Long,
        pageNumber: Int,
        pageSize: Int
    ) : Response<ReviewImageDetailList>

    suspend fun getReviewDetail(
        reviewId: Long
    ) : Flow<CustomResponse<ReviewDetailRes>>
}