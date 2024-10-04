package inu.appcenter.bjj_android.model.review

import com.google.gson.annotations.SerializedName

data class ReviewRes(
    @SerializedName("reviewDetailList") val reviewDetailList: List<ReviewDetailRes>,
    @SerializedName("lastPage") val lastPage: Boolean
)
