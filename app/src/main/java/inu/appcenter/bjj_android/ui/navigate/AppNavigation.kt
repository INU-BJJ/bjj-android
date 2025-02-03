package inu.appcenter.bjj_android.ui.navigate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import inu.appcenter.bjj_android.ui.login.AuthViewModel
import inu.appcenter.bjj_android.ui.login.LoginScreen
import inu.appcenter.bjj_android.ui.login.SignupScreen
import inu.appcenter.bjj_android.ui.main.MainScreen
import inu.appcenter.bjj_android.ui.main.MainViewModel
import inu.appcenter.bjj_android.ui.menudetail.MenuDetailScreen
import inu.appcenter.bjj_android.ui.menudetail.MenuDetailViewModel
import inu.appcenter.bjj_android.ui.menudetail.moreimage.MoreImageScreen
import inu.appcenter.bjj_android.ui.mypage.MyPageScreen
import inu.appcenter.bjj_android.ui.mypage.setting.SettingScreen
import inu.appcenter.bjj_android.ui.mypage.setting.page.LikedMenuScreen
import inu.appcenter.bjj_android.ui.ranking.RankingScreen
import inu.appcenter.bjj_android.ui.review.ReviewScreen
import inu.appcenter.bjj_android.ui.review.ReviewViewModel
import inu.appcenter.bjj_android.ui.review.page.MoreReadScreen
import inu.appcenter.bjj_android.ui.review.page.PushReviewDetailScreen
import inu.appcenter.bjj_android.ui.review.page.ReviewDetailScreen
import inu.appcenter.bjj_android.ui.review.page.ReviewWriteScreen


@Composable
fun AppNavigation(
    authViewModel: AuthViewModel,
    mainViewModel: MainViewModel,
    menuDetailViewModel: MenuDetailViewModel,
    reviewViewModel: ReviewViewModel
) {

    val navController = rememberNavController()
    val uiState by authViewModel.uiState.collectAsState()


    if (uiState.hasToken == null) {
        LoadingScreen()
    } else {
        NavHost(
            navController = navController,
            startDestination = if (uiState.hasToken == true) AllDestination.Main.route else AllDestination.Login.route,
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
                    authViewModel = authViewModel,
                    successSignup = {
                        navController.navigate(AllDestination.Main.route) {
                            popUpTo(AllDestination.Login.route) { inclusive = true }
                        }
                    },
                    uiState = uiState
                )
            }
            composable(AllDestination.Main.route) {
                MainScreen(
                    navController = navController,
                    authViewModel = authViewModel,
                    mainViewModel = mainViewModel,
                    menuDetailViewModel = menuDetailViewModel,
                    onTokenExpired = {
                        navController.navigate(AllDestination.Login.route) {
                            popUpTo(AllDestination.Main.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(AllDestination.MenuDetail.route) {
                MenuDetailScreen(navController = navController, menuDetailViewModel = menuDetailViewModel)
            }
            composable(AllDestination.Ranking.route) {
                RankingScreen(navController)
            }
            composable(AllDestination.Review.route) {
                ReviewScreen(navController = navController, reviewViewModel = reviewViewModel)
            }
            composable(AllDestination.ReviewMore.route){
                MoreReadScreen(navController = navController, reviewViewModel = reviewViewModel)
            }
            composable(AllDestination.ReviewWrite.route){
                ReviewWriteScreen(navController = navController, reviewViewModel = reviewViewModel)
            }
            composable(AllDestination.ReviewDetail.route){
                ReviewDetailScreen(navController = navController, reviewViewModel = reviewViewModel)
            }
            composable(AllDestination.ReviewDetailPush.route) {
                PushReviewDetailScreen(navController, reviewViewModel = reviewViewModel)
            }
            composable(AllDestination.MyPage.route) {
                MyPageScreen(navController = navController, authViewModel = authViewModel)
            }
            composable(AllDestination.Setting.route) {
                SettingScreen(navController)
            }
            composable(AllDestination.LikedMenu.route) {
                LikedMenuScreen(navController)
            }
            composable(
                route = "moreImage/{menuPairId}",
                arguments = listOf(navArgument("menuPairId") { type = NavType.LongType })
            ) { backStackEntry ->
                val menuPairId = backStackEntry.arguments?.getLong("menuPairId") ?: return@composable
                MoreImageScreen(
                    navController = navController,
                    menuDetailViewModel = menuDetailViewModel,
                    menuPairId = menuPairId
                )
            }
        }
    }
}