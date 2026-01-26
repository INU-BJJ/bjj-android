package inu.appcenter.bjj_android.shared.navigation

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import inu.appcenter.bjj_android.shared.component.ReviewImageDetailScreen
import inu.appcenter.bjj_android.feature.auth.presentation.login.AuthState
import inu.appcenter.bjj_android.feature.auth.presentation.login.AuthViewModel
import inu.appcenter.bjj_android.feature.auth.presentation.login.LoginScreen
import inu.appcenter.bjj_android.feature.auth.presentation.login.SignupScreen
import inu.appcenter.bjj_android.feature.main.presentation.MainScreen
import inu.appcenter.bjj_android.feature.main.presentation.MainViewModel
import inu.appcenter.bjj_android.feature.main.presentation.common.WebViewScreen
import inu.appcenter.bjj_android.feature.menudetail.presentation.MenuDetailScreen
import inu.appcenter.bjj_android.feature.menudetail.presentation.MenuDetailViewModel
import inu.appcenter.bjj_android.feature.menudetail.presentation.moreimage.MoreImageScreen
import inu.appcenter.bjj_android.feature.menudetail.presentation.report.ReportScreen
import inu.appcenter.bjj_android.feature.profile.presentation.MyPageScreen
import inu.appcenter.bjj_android.feature.profile.presentation.MyPageViewModel
import inu.appcenter.bjj_android.feature.profile.presentation.setting.SettingScreen
import inu.appcenter.bjj_android.feature.profile.presentation.setting.likedmenu.LikedMenuScreen
import inu.appcenter.bjj_android.feature.profile.presentation.setting.likedmenu.LikedMenuViewModel
import inu.appcenter.bjj_android.feature.profile.presentation.setting.nickname.NicknameChangeScreen
import inu.appcenter.bjj_android.feature.profile.presentation.setting.nickname.NicknameChangeViewModel
import inu.appcenter.bjj_android.feature.profile.presentation.setting.privacy.PrivacyScreen
import inu.appcenter.bjj_android.feature.profile.presentation.setting.service.ServiceScreen
import inu.appcenter.bjj_android.feature.profile.presentation.shop.ItemDrawSuccessScreen
import inu.appcenter.bjj_android.feature.profile.presentation.shop.ShopScreen
import inu.appcenter.bjj_android.feature.ranking.presentation.RankingScreen
import inu.appcenter.bjj_android.feature.ranking.presentation.RankingViewModel
import inu.appcenter.bjj_android.feature.review.presentation.ReviewScreen
import inu.appcenter.bjj_android.feature.review.presentation.ReviewViewModel
import inu.appcenter.bjj_android.feature.review.presentation.page.MoreReadScreen
import inu.appcenter.bjj_android.feature.review.presentation.page.ReviewDetailScreen as ReviewDetailPage
import inu.appcenter.bjj_android.feature.review.presentation.page.ReviewWriteScreen
import inu.appcenter.bjj_android.feature.menudetail.presentation.common.ReviewDetailScreen


@Composable
fun AppNavigation(
    authViewModel: AuthViewModel,
    mainViewModel: MainViewModel,
    menuDetailViewModel: MenuDetailViewModel,
    reviewViewModel: ReviewViewModel,
    rankingViewModel: RankingViewModel,
    likedMenuViewModel: LikedMenuViewModel,
    nicknameChangeViewModel: NicknameChangeViewModel,
    myPageViewModel: MyPageViewModel
) {

    val navController = rememberNavController()
    val uiState by authViewModel.uiState.collectAsState()
    val context = LocalContext.current

    // 회원 탈퇴 상태 관찰
    LaunchedEffect(uiState.deleteAccountState) {
        if (uiState.deleteAccountState is AuthState.Success) {
            // 회원 탈퇴 성공 시 로그인 화면으로 이동
            navController.navigate(AllDestination.Login.route) {
                popUpTo(AllDestination.Main.route) { inclusive = true }
            }
            // 상태 초기화
            authViewModel.resetDeleteAccountState()
        }
    }

    // 로그아웃 상태 관찰 (현재는 SettingScreen에서 처리중이지만 일관성을 위해 여기로 이동할 수도 있음)
    LaunchedEffect(uiState.logoutState) {
        if (uiState.logoutState is AuthState.Success) {
            navController.navigate(AllDestination.Login.route) {
                popUpTo(AllDestination.Main.route) { inclusive = true }
            }
            // 상태 초기화
            authViewModel.resetState()
        }
    }

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
                MenuDetailScreen(
                    navController = navController,
                    menuDetailViewModel = menuDetailViewModel
                )
            }
            composable(AllDestination.Ranking.route) {
                RankingScreen(
                    navController,
                    rankingViewModel = rankingViewModel
                )
            }
            composable(AllDestination.Review.route) {
                ReviewScreen(
                    navController = navController,
                    reviewViewModel = reviewViewModel
                )
            }
            composable(AllDestination.ReviewMore.route) {
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
            composable(AllDestination.ReviewWrite.route) {
                ReviewWriteScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    reviewViewModel = reviewViewModel
                )
            }
            composable(AllDestination.ReviewDetail.route) {
                ReviewDetailPage(
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
                    reviewViewModel = reviewViewModel,
                    fromReviewDetail = true
                )
            }
            composable(AllDestination.MyPage.route) {
                MyPageScreen(
                    navController = navController,
                    authViewModel = authViewModel,
                    myPageViewModel = myPageViewModel,
                    navigateToShop = { navController.navigate(AllDestination.Shop.route) }
                )
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
                    onNavigateToService = {
                        navController.navigate(AllDestination.ServiceTerms.route)
                    },
                    onNavigateToPrivacy = {
                        navController.navigate(AllDestination.PrivacyPolicy.route)
                    },
                    onNavigateToOpenSourceLicense = {
                        val intent = Intent(context, OssLicensesMenuActivity::class.java)
                        context.startActivity(intent)
                    },
                    onNavigateToLogin = {
                        //logout만 호출하고 네비게이션은 LaunchedEffect에서 처리
                        authViewModel.logout()
                    },
                    onWithdrawalAccount = {
                        // 회원 탈퇴 기능 호출, 네비게이션은 LaunchedEffect에서 처리
                        authViewModel.deleteAccount()
                    }
                )
            }
            composable(
                route = AllDestination.WebView.route,
                arguments = listOf(navArgument("url") { type = NavType.StringType })
            ) { backStackEntry ->
                val url = backStackEntry.arguments?.getString("url") ?: ""
                WebViewScreen(url = url, navController = navController)
            }
            composable(AllDestination.PrivacyPolicy.route) {
                PrivacyScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
            composable(AllDestination.ServiceTerms.route) {
                ServiceScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
            composable(AllDestination.LikedMenu.route) {
                LikedMenuScreen(
                    likedMenuViewModel = likedMenuViewModel,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
//                    onNavigateToMenuDetail = { menuId ->
//                        // 메뉴 상세 페이지로 이동 (필요한 경우)
//                        // 예: navController.navigate("menuDetail/$menuId")
//                    }
                )
            }
            // 네비게이션 설정 코드에서:
            composable(AllDestination.Nickname.route) {
                NicknameChangeScreen(
                    onNavigateBack = {
                        nicknameChangeViewModel.resetState()
                        navController.popBackStack()
                    },
                    nicknameChangeViewModel = nicknameChangeViewModel,
                    successChange = {
                        // 성공 후 처리 (예: 토스트 메시지 표시 및 이전 화면으로 이동)
                        navController.popBackStack()
                    }
                )
            }
            composable(
                route = "moreImage/{menuPairId}",
                arguments = listOf(navArgument("menuPairId") { type = NavType.LongType })
            ) { backStackEntry ->
                val menuPairId =
                    backStackEntry.arguments?.getLong("menuPairId") ?: return@composable
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
                    navArgument("menuId") { type = NavType.LongType }
                )
            ) { backStackEntry ->
                val imageListString = backStackEntry.arguments?.getString("imageList") ?: ""
                // 쉼표로 구분된 문자열을 다시 리스트로 변환
                val imageList = imageListString.split(",").filter { it.isNotEmpty() }
                val index = backStackEntry.arguments?.getInt("index") ?: 0
                val reviewId = backStackEntry.arguments?.getLong("reviewId") ?: 0
                val fromReviewDetail = backStackEntry.arguments?.getBoolean("fromReviewDetail") ?: false
                val menuId = backStackEntry.arguments?.getLong("menuId") ?: -1L

                ReviewImageDetailScreen(
                    navController = navController,
                    imageList = imageList,
                    index = index,
                    reviewId = reviewId,
                    fromReviewDetail = fromReviewDetail,
                    menuId = menuId
                )
            }

            composable(
                route = AllDestination.MenuDetailReviewDetail.route,
                arguments = listOf(
                    navArgument("reviewId") { type = NavType.LongType },
                    navArgument("menuId") { type = NavType.LongType }
                )
            ) { backStackEntry ->
                val reviewId = backStackEntry.arguments?.getLong("reviewId") ?: 0
                val menuId = backStackEntry.arguments?.getLong("menuId") ?: -1

                // -1은 메뉴 ID가 없음을 의미
                val currentMenu = if (menuId != -1L) {
                    menuDetailViewModel.uiState.collectAsState().value.selectedMenu
                } else null

                ReviewDetailScreen(
                    navController = navController,
                    reviewId = reviewId,
                    reviewViewModel = reviewViewModel,
                    currentMenu = currentMenu
                )
            }

            composable(
                route = AllDestination.Shop.route
            ) {
                ShopScreen(
                    myPageViewModel = myPageViewModel,
                    popBackStack = {
                        navController.popBackStack()
                    },
                    navigateToDrawSuccess = {
                        navController.navigate(AllDestination.ItemDrawSuccess.route)
                    }
                )
            }
            composable(
                route = AllDestination.ItemDrawSuccess.route
            ) {
                ItemDrawSuccessScreen (
                    myPageViewModel = myPageViewModel,
                    popBackStack = {
                        navController.popBackStack()
                    },
                    onEquip = {
                        navController.navigate(AllDestination.MyPage.route) {
                            // Shop 화면으로 돌아가서 그 이후 백스택 제거 (뽑기 성공 화면 포함)
                            popUpTo(AllDestination.Shop.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(
                route = AllDestination.ReportReview.route,
                arguments = listOf(navArgument("reviewId") { type = NavType.LongType })
            ) { backStackEntry ->
                val reviewId = backStackEntry.arguments?.getLong("reviewId") ?: 0
                ReportScreen(
                    navController = navController,
                    reviewId = reviewId,
                    viewmodel = menuDetailViewModel,
                    onReportComplete = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}