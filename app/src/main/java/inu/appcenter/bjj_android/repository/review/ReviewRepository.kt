package inu.appcenter.bjj_android.repository.review

import inu.appcenter.bjj_android.model.review.ReviewPost
import inu.appcenter.bjj_android.model.review.ReviewRes
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
        memberId: Long,
        reviewPost: ReviewPost
    ) : Response<Void>

    suspend fun deleteReview(
        reviewId: Long
    ) : Response<Void>

    suspend fun getMyReviews(
        memberId: Long,
        pageNumber: Long,
        pageSize: Long
    ) : Response<ReviewRes>
}