package inu.appcenter.bjj_android.ui.menudetail.common

fun Int.numberFormatter(): String {
    return String.format("%,d", this)
}