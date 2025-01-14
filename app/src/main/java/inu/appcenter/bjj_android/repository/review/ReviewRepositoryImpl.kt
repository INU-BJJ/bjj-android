package inu.appcenter.bjj_android.repository.review

import inu.appcenter.bjj_android.model.review.MyReviewsGroupedRes
import inu.appcenter.bjj_android.model.review.MyReviewsPagedRes
import inu.appcenter.bjj_android.model.review.ReviewImageDetailList
import inu.appcenter.bjj_android.model.review.ReviewRes
import inu.appcenter.bjj_android.network.APIService
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    override suspend fun postReview(
        reviewPost: RequestBody,
        files: List<MultipartBody.Part>?
    ): Response<Unit> {
        return apiService.postReview(reviewPost = reviewPost, files = files)
    }

    override suspend fun deleteReview(reviewId: Long): Response<Unit> {
        return apiService.deleteReview(reviewId = reviewId)
    }

    override suspend fun getMyReviews(): Response<MyReviewsGroupedRes> {
        return apiService.getMyReviews()
    }

    override suspend fun getMyReviewsByCafeteria(
        cafeteriaName: String,
        pageNumber: Int,
        pageSize: Int
    ): Response<MyReviewsPagedRes> {
        return apiService.getMyReviewsByCafeteria(
            cafeteriaName = cafeteriaName,
            pageNumber = pageNumber,
            pageSize = pageSize
        )
    }

    override suspend fun toggleReviewLiked(reviewId: Long): Response<Boolean> {
        return apiService.toggleReviewLiked(reviewId = reviewId)
    }

    override suspend fun getReviewImages(
        menuPairId: Long,
        pageNumber: Int,
        pageSize: Int
    ): Response<ReviewImageDetailList> {
        return apiService.getReviewImages(menuPairId, pageNumber, pageSize)
    }
}