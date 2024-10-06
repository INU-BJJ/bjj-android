package inu.appcenter.bjj_android.ui.navigate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.ui.login.AuthViewModel
import inu.appcenter.bjj_android.ui.login.LoginScreen
import inu.appcenter.bjj_android.ui.login.SignupScreen
import inu.appcenter.bjj_android.ui.main.MainMenu
import inu.appcenter.bjj_android.ui.main.MainScreen
import inu.appcenter.bjj_android.ui.main.MainViewModel
import inu.appcenter.bjj_android.ui.menudetail.MenuDetailScreen
import inu.appcenter.bjj_android.ui.mypage.MyPageScreen
import inu.appcenter.bjj_android.ui.review.ReviewScreen
import inu.appcenter.bjj_android.ui.tier.TierScreen



@Composable
fun AppNavigation(
    authViewModel: AuthViewModel,
    mainViewModel: MainViewModel
) {

    val navController = rememberNavController()
    val hasToken by authViewModel.hasToken.collectAsState()





    if (hasToken == null) {
        LoadingScreen()
    } else {
        NavHost(
            navController = navController,
            startDestination = if (hasToken == true) AllDestination.Main.route else AllDestination.Login.route,
        ) {
            composable(AllDestination.Login.route) {
                LoginScreen(
                    onLoginSuccessAlreadySignup = {
                        navController.navigate(AllDestination.Main.route) {
                            popUpTo(AllDestination.Login.route) { inclusive = true }
                        }
                    },
                    onLoginSuccessFirst = {
                        navController.navigate(AllDestination.Signup.route) {
                        }
                    },
                    onLoginFailure = {

                    },
                    authViewModel = authViewModel
                )
            }
            composable(AllDestination.Signup.route) {
                SignupScreen(
                    navController = navController,
                    authViewModel = authViewModel
                )
            }
            composable(AllDestination.Main.route) {
                MainScreen(
                    navController = navController,
                    authViewModel = authViewModel,
                    mainViewModel = mainViewModel,
                    onTokenExpired = {
                        navController.navigate(AllDestination.Login.route) {
                            popUpTo(AllDestination.Main.route) { inclusive = true }
                        }
                    }
                )
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
}