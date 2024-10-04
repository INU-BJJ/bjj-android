package inu.appcenter.bjj_android.model.review

import com.google.gson.annotations.SerializedName

data class ReviewPost(
    @SerializedName("comment") val comment: String,
    @SerializedName("rating") val rating: Int,
    @SerializedName("menuPairId") val menuPairId: Long
)
