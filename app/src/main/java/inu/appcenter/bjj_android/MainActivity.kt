package inu.appcenter.bjj_android

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import inu.appcenter.bjj_android.fcm.FcmManager
import inu.appcenter.bjj_android.local.DataStoreManager
import inu.appcenter.bjj_android.notification.NotificationManager
import inu.appcenter.bjj_android.ui.login.AuthViewModel
import inu.appcenter.bjj_android.ui.main.MainViewModel
import inu.appcenter.bjj_android.ui.menudetail.MenuDetailViewModel
import inu.appcenter.bjj_android.ui.mypage.MyPageViewModel
import inu.appcenter.bjj_android.ui.mypage.setting.likedmenu.LikedMenuViewModel
import inu.appcenter.bjj_android.ui.mypage.setting.nickname.NicknameChangeViewModel
import inu.appcenter.bjj_android.ui.navigate.AppNavigation
import inu.appcenter.bjj_android.ui.ranking.RankingViewModel
import inu.appcenter.bjj_android.ui.review.ReviewViewModel
import inu.appcenter.bjj_android.ui.theme.AppTypography
import inu.appcenter.bjj_android.ui.theme.Bjj_androidTheme
import inu.appcenter.bjj_android.utils.PermissionManager
import inu.appcenter.bjj_android.utils.NotificationPermissionHandler
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.android.ext.android.inject

internal val LocalTypography = staticCompositionLocalOf { AppTypography() }

class MainActivity : ComponentActivity() {

    private val fcmManager: FcmManager by inject()
    private val notificationManager: NotificationManager by inject()
    private val permissionManager: PermissionManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // 앱 시작 시 알림 채널 생성
        notificationManager.createNotificationChannels()

        // 초기 권한 확인
        permissionManager.checkInitialPermissions()

        // FCM 관리자 초기화
        fcmManager.initialize()

        Log.d("MainActivity", "앱 초기화 완료")

        setContent {
            val authViewModel: AuthViewModel by viewModel()
            val mainViewModel: MainViewModel by viewModel()
            val menuDetailViewModel: MenuDetailViewModel by viewModel()
            val reviewViewModel: ReviewViewModel by viewModel()
            val rankingViewModel: RankingViewModel by viewModel()
            val likedMenuViewModel: LikedMenuViewModel by viewModel()
            val nicknameChangeViewModel: NicknameChangeViewModel by viewModel()
            val myPageViewModel: MyPageViewModel by viewModel()

            // 권한 관리자를 통한 알림 권한 처리
            NotificationPermissionHandler(permissionManager)

            // 인증 상태 구독
            val uiState by authViewModel.uiState.collectAsState()

            // 하단 바 제거
            val view = LocalView.current
            val insetsController = WindowCompat.getInsetsController(window, view)

            WindowCompat.setDecorFitsSystemWindows(window, false)
            insetsController.apply {
                hide(WindowInsetsCompat.Type.navigationBars())
                systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                isAppearanceLightStatusBars = true
            }

            Bjj_androidTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent)
                ) {
                    AppNavigation(
                        authViewModel = authViewModel,
                        mainViewModel = mainViewModel,
                        menuDetailViewModel = menuDetailViewModel,
                        reviewViewModel = reviewViewModel,
                        rankingViewModel = rankingViewModel,
                        likedMenuViewModel = likedMenuViewModel,
                        nicknameChangeViewModel = nicknameChangeViewModel,
                        myPageViewModel = myPageViewModel
                    )
                }
            }
        }
    }
}