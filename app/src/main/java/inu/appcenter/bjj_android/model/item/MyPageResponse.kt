package inu.appcenter.bjj_android.model.item

data class MyPageResponse(
    val nickname: String,
    val characterId: Long,
    val characterImageName: String,
    val backgroundId: Long,
    val backgroundImageName: String,
    val point: Long
)