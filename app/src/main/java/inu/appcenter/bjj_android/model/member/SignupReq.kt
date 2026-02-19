package inu.appcenter.bjj_android.model.member

import com.google.gson.annotations.SerializedName

data class SignupReq (
    @SerializedName("nickname") val nickname: String,
    @SerializedName("email") val email: String,
    @SerializedName("provider") val provider: String,
    @SerializedName("providerId") val providerId: String

)