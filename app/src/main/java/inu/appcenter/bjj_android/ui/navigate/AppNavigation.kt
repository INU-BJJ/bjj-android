package inu.appcenter.bjj_android.ui.navigate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import inu.appcenter.bjj_android.ui.components.ReviewImageDetailScreen
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
import inu.appcenter.bjj_android.ui.mypage.setting.likedmenu.LikedMenuScreen
import inu.appcenter.bjj_android.ui.mypage.setting.likedmenu.LikedMenuViewModel
import inu.appcenter.bjj_android.ui.ranking.RankingScreen
import inu.appcenter.bjj_android.ui.ranking.RankingViewModel
import inu.appcenter.bjj_android.ui.review.ReviewScreen
import inu.appcenter.bjj_android.ui.review.ReviewViewModel
import inu.appcenter.bjj_android.ui.review.page.MoreReadScreen
import inu.appcenter.bjj_android.ui.review.page.ReviewDetailScreen
import inu.appcenter.bjj_android.ui.review.page.ReviewWriteScreen


@Composable
fun AppNavigation(
    authViewModel: AuthViewModel,
    mainViewModel: MainViewModel,
    menuDetailViewModel: MenuDetailViewModel,
    reviewViewModel: ReviewViewModel,
    rankingViewModel: RankingViewModel,
    likedMenuViewModel: LikedMenuViewModel
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
                RankingScreen(navController, rankingViewModel = rankingViewModel)
            }
            composable(AllDestination.Review.route) {
                ReviewScreen(navController = navController, reviewViewModel = reviewViewModel)
            }
            composable(AllDestination.ReviewMore.route){
                MoreReadScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToReviewDetail = { reviewDetail ->
                        reviewViewModel.setSelectedReviewDetail(reviewDetail)
                        navController.navigate(AllDestination.ReviewDetail.route)
                    },
                    reviewViewModel = reviewViewModel
                )
            }
            composable(AllDestination.ReviewWrite.route){
                ReviewWriteScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    reviewViewModel = reviewViewModel
                )
            }
            composable(AllDestination.ReviewDetail.route){
                ReviewDetailScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToImageDetail = { index ->
                        reviewViewModel.selectImageIndex(index)
                        navController.navigate(AllDestination.ReviewDetailPush.route)
                    },
                    reviewViewModel = reviewViewModel
                )
            }
            composable(AllDestination.ReviewDetailPush.route) {
                ReviewImageDetailScreen(
                    navController = navController,
                    reviewViewModel = reviewViewModel
                )
            }
            composable(AllDestination.MyPage.route) {
                MyPageScreen(navController = navController, authViewModel = authViewModel)
            }
            composable(AllDestination.Setting.route) {
                SettingScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigeteToNickname = {
                        navController.navigate(AllDestination.Nickname.route)
                    },
                    onNavigateToLikedMenu = {
                        navController.navigate(AllDestination.LikedMenu.route)
                    },
                    onNavigateToBlockedUser = {
                        navController.navigate(AllDestination.BlockedUser.route)
                    },
                    onWithdrawalAccount = {
                        //TODO: 탈퇴 로직 구현
                    }
                )
            }
            composable(AllDestination.LikedMenu.route) {
                LikedMenuScreen(
                    viewModel = likedMenuViewModel,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
//                    onNavigateToMenuDetail = { menuId ->
//                        // 메뉴 상세 페이지로 이동 (필요한 경우)
//                        // 예: navController.navigate("menuDetail/$menuId")
//                    }
                )
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
            composable(
                route = AllDestination.MenuDetailReviewDetailPush.route,
                arguments = listOf(
                    navArgument("imageList") {
                        type = NavType.StringType
                        // 쉼표로 구분된 문자열을 받음
                    },
                    navArgument("index") { type = NavType.IntType },
                    navArgument("reviewId") { type = NavType.LongType },
                    navArgument("fromReviewDetail") { type = NavType.BoolType },

                )
            ) { backStackEntry ->
                val imageListString = backStackEntry.arguments?.getString("imageList") ?: ""
                // 쉼표로 구분된 문자열을 다시 리스트로 변환
                val imageList = imageListString.split(",").filter { it.isNotEmpty() }
                val index = backStackEntry.arguments?.getInt("index") ?: 0
                val reviewId = backStackEntry.arguments?.getLong("reviewId") ?: 0
                val fromReviewDetail = backStackEntry.arguments?.getBoolean("fromReviewDetail") ?: false

                ReviewImageDetailScreen(
                    navController = navController,
                    imageList = imageList,
                    index = index,
                    reviewId = reviewId,
                    fromReviewDetail = fromReviewDetail
                )
            }

            composable(
                route = AllDestination.MenuDetailReviewDetail.route,
                arguments = listOf(
                    navArgument("reviewId") { type = NavType.LongType }
                )
            ) { backStackEntry ->
                val reviewId = backStackEntry.arguments?.getLong("reviewId") ?: 0

                inu.appcenter.bjj_android.ui.menudetail.common.ReviewDetailScreen(
                    navController = navController,
                    reviewId = reviewId,
                    reviewViewModel = reviewViewModel
                )
            }
        }
    }
}