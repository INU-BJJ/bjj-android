package inu.appcenter.bjj_android.model.review

import com.google.gson.annotations.SerializedName

data class MyReviewsGroupedRes(
    @SerializedName("myReviewDetailList") val myReviewDetailList: Map<String, List<MyReviewDetailRes>>,
)
