package inu.appcenter.bjj_android.model.member

import com.google.gson.annotations.SerializedName

data class MemberResponseDTO(
    @SerializedName("nickname") val nickname: String,
    @SerializedName("email") val email: String,
    @SerializedName("provider") val provider: String
)
