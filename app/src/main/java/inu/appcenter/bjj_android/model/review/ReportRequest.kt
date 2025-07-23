package inu.appcenter.bjj_android.model.review

import com.google.gson.annotations.SerializedName

data class ReportRequest(
    @SerializedName("content") val content: List<String>,


)
