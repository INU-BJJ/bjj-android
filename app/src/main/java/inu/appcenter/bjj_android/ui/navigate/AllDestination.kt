package inu.appcenter.bjj_android.ui.navigate

import inu.appcenter.bjj_android.R

sealed class AllDestination(val route: String, val icon: Int, val label: String) {
    data object Login : AllDestination("login", R.drawable.mypage, "login")
    data object Main : AllDestination("main", R.drawable.home, "홈")
    data object MenuDetail : AllDestination("menuDetail", R.drawable.mypage, "메뉴 디테일")
    data object Tier : AllDestination("tier", R.drawable.tier, "티어표")
    data object Review : AllDestination("review", R.drawable.review, "리뷰")
    data object MyPage : AllDestination("myPage", R.drawable.mypage, "마이페이지")
}