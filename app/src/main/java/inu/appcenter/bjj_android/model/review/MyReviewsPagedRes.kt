package inu.appcenter.bjj_android.model.review

import com.google.gson.annotations.SerializedName

data class MyReviewsPagedRes(
    @SerializedName("myReviewDetailList") val myReviewDetailList: List<MyReviewDetailRes>,
    @SerializedName("lastPage") val lastPage: Boolean
)