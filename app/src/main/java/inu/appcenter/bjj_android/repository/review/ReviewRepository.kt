package inu.appcenter.bjj_android.repository.review

import inu.appcenter.bjj_android.model.review.MyReviewRes
import inu.appcenter.bjj_android.model.review.ReviewPost
import inu.appcenter.bjj_android.model.review.ReviewRes
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

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

    suspend fun getMyReviews() : Response<MyReviewRes>

    suspend fun getMyReviewsByCafeteria(
        cafeteriaName: String,
        pageNumber: Int,
        pageSize: Int
    ) : Response<ReviewRes>
}