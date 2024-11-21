package inu.appcenter.bjj_android.model.review

import com.google.gson.annotations.SerializedName

data class MyReviewRes(
    @SerializedName("myReviewDetailList") val myReviewDetailList: Map<String, List<MyReviewDetailRes>>,
)
