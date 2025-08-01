package inu.appcenter.bjj_android.model.item

enum class ItemLevel {
    DEFAULT,
    COMMON,
    NORMAL,
    RARE,
}

fun ItemLevel.toKorean(): String {
    return when (this) {
        ItemLevel.DEFAULT -> "기본"
        ItemLevel.COMMON -> "흔함"
        ItemLevel.NORMAL -> "보통"
        ItemLevel.RARE -> "희귀"
    }
}