package inu.appcenter.bjj_android.model.member

import com.google.gson.annotations.SerializedName

data class LoginReq(
    @SerializedName("providerId") val providerId: String,
    @SerializedName("provider") val provider: String,
)
