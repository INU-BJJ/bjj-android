package inu.appcenter.bjj_android.model.review

import com.google.gson.annotations.SerializedName

data class ReviewImageDetail(
    @SerializedName("reviewId") val reviewId: Long,
    @SerializedName("imageName") val imageName: String
)
