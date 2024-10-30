package inu.appcenter.bjj_android.repository.review

import inu.appcenter.bjj_android.model.review.ReviewPost
import inu.appcenter.bjj_android.model.review.ReviewRes
import inu.appcenter.bjj_android.network.APIService
import retrofit2.Response

class ReviewRepositoryImpl(private val apiService: APIService) : ReviewRepository {
    override suspend fun getReviews(
        menuPairId: Long,
        pageNumber: Int,
        pageSize: Int,
        sort: String,
        isWithImages: Boolean
    ): Response<ReviewRes> {
        return apiService.getReviews(
            menuPairId = menuPairId,
            pageNumber = pageNumber,
            pageSize = pageSize,
            sort = sort,
            isWithImages = isWithImages
        )
    }

    override suspend fun postReview(memberId: Long, reviewPost: ReviewPost): Response<Void> {
        return apiService.postReview(
            memberId = memberId,
            reviewPost = reviewPost
        )
    }

    override suspend fun deleteReview(reviewId: Long): Response<Void> {
        return apiService.deleteReview(reviewId = reviewId)
    }

    override suspend fun getMyReviews(
        memberId: Long,
        pageNumber: Long,
        pageSize: Long
    ): Response<ReviewRes> {
        return apiService.getMyReviews(memberId = memberId, pageNumber = pageNumber, pageSize = pageSize)
    }


}