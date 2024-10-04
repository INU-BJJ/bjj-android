package inu.appcenter.bjj_android.ui.navigate

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.ui.login.LoginScreen
import inu.appcenter.bjj_android.ui.main.MainMenu
import inu.appcenter.bjj_android.ui.main.MainScreen
import inu.appcenter.bjj_android.ui.main.MainViewModel
import inu.appcenter.bjj_android.ui.menudetail.MenuDetailScreen
import inu.appcenter.bjj_android.ui.mypage.MyPageScreen
import inu.appcenter.bjj_android.ui.review.ReviewScreen
import inu.appcenter.bjj_android.ui.tier.TierScreen


sealed class Screen(val route: String, val icon: Int, val label: String) {
    data object Login : Screen("login", R.drawable.mypage, "login")
    data object Main : Screen("main", R.drawable.home, "홈")
    data object MenuDetail : Screen("menuDetail", R.drawable.mypage, "메뉴 디테일")
    data object Tier : Screen("tier", R.drawable.tier, "티어표")
    data object Review : Screen("review", R.drawable.review, "리뷰")
    data object MyPage : Screen("mypage", R.drawable.mypage, "마이페이지")
}



@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
    ) {
        composable(Screen.Login.route) {
            LoginScreen(onLoginSuccess = {
                navController.navigate(Screen.Main.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            })
        }
        composable(Screen.Main.route) {
            MainScreen(navController)
        }
        composable(Screen.MenuDetail.route) {
            val menu = navController.previousBackStackEntry?.savedStateHandle?.get<MainMenu>("menu")
            menu?.let {
                MenuDetailScreen(menu = it, navController = navController)
            }
        }
        composable(Screen.Tier.route) { TierScreen(navController) }
        composable(Screen.Review.route) { ReviewScreen(navController) }
        composable(Screen.MyPage.route) { MyPageScreen(navController) }
    }
}