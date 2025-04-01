package inu.appcenter.bjj_android.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun String.formatRemainingTime(): String {
    val targetDateTime = LocalDateTime.parse(this, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    val currentDateTime = LocalDateTime.now()

    val duration = java.time.Duration.between(currentDateTime, targetDateTime)

    return when {
        duration.toDays() > 0 -> "${duration.toDays()}d"
        duration.toHours() > 0 -> "${duration.toHours()}h"
        duration.toMinutes() > 0 -> "${duration.toMinutes()}m"
        duration.toSeconds() > 0 -> "${duration.toSeconds()}s"
        else -> "만료됨"
    }
}

// 아이템이 유효한지 확인하는 확장 함수
fun String?.isValidItem(): Boolean {
    if (this == null) return false

    val targetDateTime = LocalDateTime.parse(this, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    val currentDateTime = LocalDateTime.now()

    return targetDateTime.isAfter(currentDateTime)
}