package inu.appcenter.bjj_android.network

import inu.appcenter.bjj_android.model.member.MemberResponseDTO
import inu.appcenter.bjj_android.model.member.SignupDTO
import inu.appcenter.bjj_android.model.member.SignupRes
import inu.appcenter.bjj_android.model.review.ReviewPost
import inu.appcenter.bjj_android.model.review.ReviewRes
import inu.appcenter.bjj_android.model.todaydiet.TodayDietRes
import inu.appcenter.bjj_android.model.todaydiet.TodayMenuRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
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

    @POST("/api/reviews")
    suspend fun postReview(
        @Query("memberId") memberId: Long,
        @Body reviewPost: ReviewPost
    ) : Response<Void>


    @DELETE("/api/reviews/{reviewId}")
    suspend fun deleteReview(
        @Path("reviewId") reviewId: Long
    ) : Response<Void>

    @GET("api/reviews/my")
    suspend fun getMyReviews(
        @Query("memberId") memberId: Long,
        @Query("pageNumber") pageNumber: Long,
        @Query("pageSize") pageSize: Long
    ) : Response<ReviewRes>

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

    //Member API
    @GET("/api/member")
    suspend fun getAllMembers() : Response<MemberResponseDTO>

    @POST("/api/member/sign-up")
    suspend fun signup(
        @Body signupDTO: SignupDTO
    ) : Response<SignupRes>

    @POST("/api/member/check-nickname")
    suspend fun checkNickname(
        @Query("nickname") nickname : String
    ) : Response<Boolean>

    @PATCH("/api/member/check-nickname")
    suspend fun modifyNickname(
        @Query("nickname") nickname : String
    ) : Response<Void>
}