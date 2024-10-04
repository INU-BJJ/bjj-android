package inu.appcenter.bjj_android.model.review

import com.google.gson.annotations.SerializedName
import java.util.Date

data class ReviewDetailRes(
    @SerializedName("reviewId") val reviewId: Long,
    @SerializedName("comment") val comment: String,
    @SerializedName("rating") val rating: Int,
    @SerializedName("imagePaths") val imagePaths: List<String>,
    @SerializedName("likeCount") val likeCount: Long,
    @SerializedName("createdDate") val createdDate: Date,
    @SerializedName("menuPairId") val menuPairId: Long,
    @SerializedName("mainMenuId") val mainMenuId: Long,
    @SerializedName("subMenuId") val subMenuId: Long,
    @SerializedName("memberId") val memberId: Long,
    @SerializedName("memberNickname") val memberNickname: String,
    @SerializedName("memberImagePath") val memberImagePath: String,
    @SerializedName("likedMenu") val likedMenu: Boolean
)
