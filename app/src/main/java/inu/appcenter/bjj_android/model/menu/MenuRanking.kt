package inu.appcenter.bjj_android.model.menu

import com.google.gson.annotations.SerializedName


data class MenuRanking(
    @SerializedName("lastPage")
    val lastPage: Boolean,
    @SerializedName("menuRankingDetailList")
    val menuRankingDetailList: List<MenuRankingDetail>
)