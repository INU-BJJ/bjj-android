package inu.appcenter.bjj_android.model.todaydiet

import com.google.gson.annotations.SerializedName

data class TodayMenuRes(
    @SerializedName("menuPairId") val menuPairId: Long,
    @SerializedName("mainMenuName") val mainMenuName: String,
    @SerializedName("cafeteriaId") val cafeteriaId: Long,
    @SerializedName("cafeteriaName") val cafeteriaName: String,
    @SerializedName("cafeteriaCorner") val cafeteriaCorner: String
)
