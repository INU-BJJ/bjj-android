package inu.appcenter.bjj_android.model.item

enum class ItemType {
    CHARACTER,
    BACKGROUND
}

fun ItemType.toKorean(): String {
    return when (this) {
        ItemType.CHARACTER -> "캐릭터"
        ItemType.BACKGROUND -> "배경"
    }
}