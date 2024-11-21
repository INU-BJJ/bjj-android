package inu.appcenter.bjj_android.model.review

import com.google.gson.annotations.SerializedName

data class MyReviewDetailRes(
    @SerializedName("reviewId") val reviewId: Long,
    @SerializedName("comment") val comment: String,
    @SerializedName("rating") val rating: Int,
    @SerializedName("imageNames") val imageNames: List<String>,
    @SerializedName("likeCount") val likeCount: Long,
    @SerializedName("createdDate") val createdDate: String,
    @SerializedName("menuPairId") val menuPairId: Long,
    @SerializedName("mainMenuName") val mainMenuName: String,
    @SerializedName("subMenuName") val subMenuName: String,
    @SerializedName("memberId") val memberId: Long,
    @SerializedName("memberNickname") val memberNickname: String,
    @SerializedName("memberImageName") val memberImageName: String?
)
