package inu.appcenter.bjj_android.model.review

import com.google.gson.annotations.SerializedName

data class ReviewImageDetailList(
    @SerializedName("reviewImageDetailList") val reviewImageDetailList: List<ReviewImageDetail>,
    @SerializedName("lastPage") val lastPage: Boolean,
)
