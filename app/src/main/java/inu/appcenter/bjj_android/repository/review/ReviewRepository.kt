package inu.appcenter.bjj_android.repository.review

import inu.appcenter.bjj_android.model.review.MyReviewsGroupedRes
import inu.appcenter.bjj_android.model.review.MyReviewsPagedRes
import inu.appcenter.bjj_android.model.review.ReportRequest
import inu.appcenter.bjj_android.model.review.ReviewDetailRes
import inu.appcenter.bjj_android.model.review.ReviewImageDetailList
import inu.appcenter.bjj_android.model.review.ReviewRes
import inu.appcenter.bjj_android.repository.base.BaseRepository
import inu.appcenter.bjj_android.utils.CustomResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Path

interface ReviewRepository : BaseRepository {

    suspend fun getReviews(
        menuPairId: Long,
        pageNumber: Int,
        pageSize: Int,
        sort: String,
        isWithImages: Boolean
    ): Flow<CustomResponse<ReviewRes>>

    suspend fun postReview(
        reviewPost: RequestBody,
        files: List<MultipartBody.Part>?
    ): Flow<CustomResponse<Unit>>

    suspend fun deleteReview(
        reviewId: Long
    ): Flow<CustomResponse<Unit>>

    suspend fun getMyReviews(): Flow<CustomResponse<MyReviewsGroupedRes>>

    suspend fun getMyReviewsByCafeteria(
        cafeteriaName: String,
        pageNumber: Int,
        pageSize: Int
    ): Flow<CustomResponse<MyReviewsPagedRes>>

    suspend fun toggleReviewLiked(
        reviewId: Long
    ): Flow<CustomResponse<Boolean>>

    suspend fun getReviewImages(
        menuPairId: Long,
        pageNumber: Int,
        pageSize: Int
    ): Flow<CustomResponse<ReviewImageDetailList>>

    suspend fun getReviewDetail(
        reviewId: Long
    ): Flow<CustomResponse<ReviewDetailRes>>

    suspend fun postReport(
        reviewId: Long,
        reportRequest: ReportRequest
    ) : Flow<CustomResponse<Unit>>
}