package inu.appcenter.bjj_android.ui.navigate

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.ui.login.LoginScreen
import inu.appcenter.bjj_android.ui.main.MainMenu
import inu.appcenter.bjj_android.ui.main.MainScreen
import inu.appcenter.bjj_android.ui.menudetail.MenuDetailScreen
import inu.appcenter.bjj_android.ui.mypage.MyPageScreen
import inu.appcenter.bjj_android.ui.review.ReviewScreen
import inu.appcenter.bjj_android.ui.tier.TierScreen


@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AllDestination.Login.route,
    ) {
        composable(AllDestination.Login.route) {
            LoginScreen(onLoginSuccess = {
                navController.navigate(AllDestination.Main.route) {
                    popUpTo(AllDestination.Login.route) { inclusive = true }
                }
            })
        }
        composable(AllDestination.Main.route) {
            MainScreen(navController)
        }
        composable(AllDestination.MenuDetail.route) {
            val menu = navController.previousBackStackEntry?.savedStateHandle?.get<MainMenu>("menu")
            menu?.let {
                MenuDetailScreen(menu = it, navController = navController)
            }
        }
        composable(AllDestination.Tier.route) { TierScreen(navController) }
        composable(AllDestination.Review.route) { ReviewScreen(navController) }
        composable(AllDestination.MyPage.route) { MyPageScreen(navController) }
    }
}