package inu.appcenter.bjj_android.feature.menudetail.presentation.common

fun Int.numberFormatter(): String {
    return String.format("%,d", this)
}