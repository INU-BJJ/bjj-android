package inu.appcenter.bjj_android.model.fcm

import com.google.gson.annotations.SerializedName

data class FcmTokenRequest(
    @SerializedName("fcmToken") val fcmToken : String
)
