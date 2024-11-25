package inu.appcenter.bjj_android.repository.review

import inu.appcenter.bjj_android.model.review.MyReviewRes
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

    override suspend fun postReview(reviewPost: ReviewPost): Response<Unit> {
        return apiService.postReview(
            reviewPost = reviewPost
        )
    }

    override suspend fun deleteReview(reviewId: Long): Response<Unit> {
        return apiService.deleteReview(reviewId = reviewId)
    }

    override suspend fun getMyReviews(): Response<MyReviewRes> {
        return apiService.getMyReviews()
    }

    override suspend fun getMyReviewsByCafeteria(
        cafeteriaName: String,
        pageNumber: Int,
        pageSize: Int
    ): Response<ReviewRes> {
        return apiService.getMyReviewsByCafeteria(
            cafeteriaName = cafeteriaName,
            pageNumber = pageNumber,
            pageSize = pageSize
        )
    }
}