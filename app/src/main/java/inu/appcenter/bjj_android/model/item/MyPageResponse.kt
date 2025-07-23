package inu.appcenter.bjj_android.model.item

data class MyPageResponse(
    val nickname: String,
    val characterIdx: Long,
    val characterImageName: String,
    val backgroundIdx: Long,
    val backgroundImageName: String,
    val point: Long
)