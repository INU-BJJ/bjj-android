package inu.appcenter.bjj_android.model.todaydiet

import com.google.gson.annotations.SerializedName
import java.util.Date

data class TodayDietRes(
    @SerializedName("todayDietId") val todayDietId: Long,
    @SerializedName("price") val price: String,
    @SerializedName("kcal") val kcal: String,
    @SerializedName("date") val date: Date,
    @SerializedName("menuPairId") val menuPairId: Long,
    @SerializedName("mainMenuId") val mainMenuId: Long,
    @SerializedName("mainMenuName") val mainMenuName: String,
    @SerializedName("subMenuId") val subMenuId: Long,
    @SerializedName("restMenu") val restMenu: String,
    @SerializedName("cafeteriaName") val cafeteriaName: String,
    @SerializedName("cafeteriaCorner") val cafeteriaCorner: String,
    @SerializedName("reviewCount") val reviewCount: Int,
    @SerializedName("reviewRatingAverage") val reviewRatingAverage: Float,
    @SerializedName("reviewImageName") val reviewImageName: String,
    @SerializedName("likedMenu") val likedMenu: Boolean
)
