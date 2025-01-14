package inu.appcenter.bjj_android.model.menu

import com.google.gson.annotations.SerializedName

data class LikedMenu(
    @SerializedName("menuId") val menuId: Long,
    @SerializedName("menuName") val menuName: String,
)
