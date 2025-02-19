package inu.appcenter.bjj_android.model.menu

import com.google.gson.annotations.SerializedName


data class MenuRankingDetail(
    @SerializedName("bestReviewId")
    val bestReviewId: Int,
    @SerializedName("cafeteriaCorner")
    val cafeteriaCorner: String,
    @SerializedName("cafeteriaName")
    val cafeteriaName: String,
    @SerializedName("menuId")
    val menuId: Int,
    @SerializedName("menuName")
    val menuName: String,
    @SerializedName("menuRating")
    val menuRating: Double,
    @SerializedName("reviewImageName")
    val reviewImageName: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)