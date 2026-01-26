package inu.appcenter.bjj_android.feature.review.presentation.component

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

fun String.formatter(): String {
    return try {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
        val date = LocalDate.parse(this, inputFormatter)
        date.format(outputFormatter)
    } catch (e: DateTimeParseException) {
        this // 파싱 실패 시 원본 문자열 반환
    }
}