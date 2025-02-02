package inu.appcenter.bjj_android.ui.navigate

import inu.appcenter.bjj_android.R

sealed class AllDestination(val route: String, val icon: Int, val label: String) {
    data object Login : AllDestination("login", R.drawable.mypage, "login")
    data object Signup : AllDestination("signup", R.drawable.mypage, "signup")
    data object Main : AllDestination("main", R.drawable.home, "홈")
    data object MenuDetail : AllDestination("menuDetail", R.drawable.mypage, "메뉴 디테일")
    data object Ranking : AllDestination("ranking", R.drawable.tier, "랭킹")
    data object Review : AllDestination("review", R.drawable.review, "리뷰")
    data object ReviewMore : AllDestination("reviewMore", R.drawable.review, "리뷰 더보기")
    data object ReviewWrite : AllDestination("reviewWrite", R.drawable.review, "리뷰 작성")
    data object ReviewDetail : AllDestination("reviewDetail", R.drawable.review, "리뷰 디테일")
    data object ReviewDetailPush : AllDestination("reviewDetailPush", R.drawable.review, "리뷰 디테일 누르면")
    data object MyPage : AllDestination("myPage", R.drawable.mypage, "마이페이지")
    data object Setting : AllDestination("setting", R.drawable.setting, "설정")
}