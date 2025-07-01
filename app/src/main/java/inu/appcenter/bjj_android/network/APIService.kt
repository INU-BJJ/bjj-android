package inu.appcenter.bjj_android.network

import inu.appcenter.bjj_android.model.cafeteria.CafeteriaInfoResponse
import inu.appcenter.bjj_android.model.fcm.FcmTokenRequest
import inu.appcenter.bjj_android.model.item.ItemResponse
import inu.appcenter.bjj_android.model.item.ItemResponseItem
import inu.appcenter.bjj_android.model.item.ItemType
import inu.appcenter.bjj_android.model.item.MyPageResponse
import inu.appcenter.bjj_android.model.member.MemberResponseDTO
import inu.appcenter.bjj_android.model.member.SignupReq
import inu.appcenter.bjj_android.model.member.SignupRes
import inu.appcenter.bjj_android.model.menu.LikedMenu
import inu.appcenter.bjj_android.model.menu.MenuRanking
import inu.appcenter.bjj_android.model.review.MyReviewsGroupedRes
import inu.appcenter.bjj_android.model.review.MyReviewsPagedRes
import inu.appcenter.bjj_android.model.review.ReportRequest
import inu.appcenter.bjj_android.model.review.ReviewDetailRes
import inu.appcenter.bjj_android.model.review.ReviewImageDetailList
import inu.appcenter.bjj_android.model.review.ReviewRes
import inu.appcenter.bjj_android.model.todaydiet.TodayDietRes
import inu.appcenter.bjj_android.model.todaydiet.TodayMenuRes
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
interface APIService {

    // Review API
    @GET("/api/reviews")
    suspend fun getReviews(
        @Query("menuPairId") menuPairId: Long,
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int,
        @Query("sort") sort: String,
        @Query("isWithImages") isWithImages: Boolean
    ) : Response<ReviewRes>

    @Multipart
    @POST("/api/reviews")
    suspend fun postReview(
        @Part("reviewPost") reviewPost: RequestBody,
        @Part files: List<MultipartBody.Part>?
    ): Response<Unit>


    @DELETE("/api/reviews/{reviewId}")
    suspend fun deleteReview(
        @Path("reviewId") reviewId: Long
    ) : Response<Unit>

    @GET("api/reviews/my")
    suspend fun getMyReviews() : Response<MyReviewsGroupedRes>

    @GET("api/reviews/my/cafeteria")
    suspend fun getMyReviewsByCafeteria(
        @Query("cafeteriaName") cafeteriaName: String,
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int
    ) : Response<MyReviewsPagedRes>

    @POST("/api/reviews/{reviewId}/like")
    suspend fun toggleReviewLiked(
        @Path("reviewId") reviewId: Long
    ) : Response<Boolean>

    @GET("/api/reviews/images")
    suspend fun getReviewImages(
        @Query("menuPairId") menuPairId: Long,
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int
    ) : Response<ReviewImageDetailList>

    // 이 메서드는 Response로 감싸서 일관성 있게 변경
    @GET("/api/reviews/{reviewId}")
    suspend fun getReviewDetail(
        @Path("reviewId") reviewId: Long
    ) : Response<ReviewDetailRes>  // Response<ReviewDetailRes>로 변경

    @POST("/api/reviews/{reviewId}/report")
    suspend fun postReport(
        @Path("reviewId") reviewId: Long,
        @Body reportRequest: ReportRequest
    ) : Response<Unit>

    // Image API
    @GET("/api/images")
    suspend fun getImagePath(
        @Query("path") path: String
    ) : Response<String>

    //TodayDiet API
    @GET("/api/today-diets")
    suspend fun getTodayDiet(
        @Query("cafeteriaName") cafeteriaName: String
    ) : Response<List<TodayDietRes>>

    @GET("/api/today-diets/main-menus")
    suspend fun getTodayDietsMainMenus(
        @Query("cafeteriaName") cafeteriaName: String
    ) : Response<List<TodayMenuRes>>

    //Cafeteria API
    @GET("/api/cafeterias")
    suspend fun getCafeterias() : Response<List<String>>

    @GET("/api/cafeterias/{name}")
    suspend fun getCafeteriaInfo(
        @Path("name") name: String
    ) : Response<CafeteriaInfoResponse>

    //Member API
    @GET("/api/members")
    suspend fun getAllMembers() : Response<MemberResponseDTO>

    @DELETE("/api/members")
    suspend fun deleteAccount(): Response<Unit>

    @POST("/api/members/sign-up")
    suspend fun signup(
        @Body signupReq: SignupReq
    ) : Response<SignupRes>

    @POST("/api/members/check-nickname")
    suspend fun checkNickname(
        @Query("nickname") nickname : String
    ) : Response<Boolean>

    @PATCH("/api/members/nickname")
    suspend fun modifyNickname(
        @Query("nickname") nickname : String
    ) : Response<Unit>

    //Menu API
    @POST("/api/menus/{menuId}/like")
    suspend fun toggleMenuLiked(
        @Path("menuId") mainMenuId: Long
    ) : Response<Boolean>

    @GET("/api/menus/liked")
    suspend fun getLikedMenus() : Response<List<LikedMenu>>

    // 이 메서드도 Response로 감싸서 일관성 있게 변경
    @GET("/api/menus/ranking")
    suspend fun getMenusRanking(
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int
    ) : Response<MenuRanking>  // Response<MenuRanking>으로 변경



    //임시로 fcm 테스트
    @POST("/api/members/fcm-token")
    suspend fun registerFcmToken(
        @Body fcmTokenRequest: FcmTokenRequest
    ): Response<Unit>


    // Item API
    @GET("/api/items")
    suspend fun getAllItemsInfo(
        @Query("itemType") itemType: ItemType
    ) : Response<ItemResponse>

    @POST("/api/items")
    suspend fun drawItem(
        @Query("itemType") itemType: ItemType
    ): Response<ItemResponseItem>

    @GET("/api/items/{itemId}")
    suspend fun getItemInfo(
        @Path("itemId") itemId: Long,
        @Query("itemType") itemType: ItemType,
    ): Response<ItemResponseItem>

    @PATCH("/api/items/{itemId}")
    suspend fun wearItem(
        @Path("itemId") itemId: Long,
        @Query("itemType") itemType: ItemType
    ): Response<Unit>

    @GET("/api/items/my")
    suspend fun getMyPageInfo(): Response<MyPageResponse>
}