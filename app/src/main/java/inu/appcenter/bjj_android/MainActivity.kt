package inu.appcenter.bjj_android

import android.os.Bundle
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
import coil.Coil
import inu.appcenter.bjj_android.core.notification.FcmManager
import inu.appcenter.bjj_android.core.data.local.DataStoreManager
import inu.appcenter.bjj_android.core.notification.NotificationManager
import inu.appcenter.bjj_android.feature.auth.presentation.login.AuthViewModel
import inu.appcenter.bjj_android.feature.main.presentation.MainViewModel
import inu.appcenter.bjj_android.feature.menudetail.presentation.MenuDetailViewModel
import inu.appcenter.bjj_android.feature.profile.presentation.MyPageViewModel
import inu.appcenter.bjj_android.feature.profile.presentation.setting.likedmenu.LikedMenuViewModel
import inu.appcenter.bjj_android.feature.profile.presentation.setting.nickname.NicknameChangeViewModel
import inu.appcenter.bjj_android.shared.navigation.AppNavigation
import inu.appcenter.bjj_android.feature.ranking.presentation.RankingViewModel
import inu.appcenter.bjj_android.feature.review.presentation.ReviewViewModel
import inu.appcenter.bjj_android.shared.theme.AppTypography
import inu.appcenter.bjj_android.shared.theme.Bjj_androidTheme
import inu.appcenter.bjj_android.core.util.ImageLoader
import inu.appcenter.bjj_android.core.util.NotificationPermissionHandler
import inu.appcenter.bjj_android.core.util.PermissionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

internal val LocalTypography = staticCompositionLocalOf { AppTypography() }

class MainActivity : ComponentActivity() {

    private val fcmManager: FcmManager by inject()
    private val notificationManager: NotificationManager by inject()
    private val permissionManager: PermissionManager by inject()
    private val dataStoreManager: DataStoreManager by inject() // DataStoreManager 주입

    // 액티비티 코루틴 스코프
    private val activityScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Coil의 기본 ImageLoader 설정
        Coil.setImageLoader(
            ImageLoader.createImageLoader(this)
        )

        // 토큰 설정 초기화
        initializeImageLoaderWithToken()

        // 앱 시작 시 알림 채널 생성
        notificationManager.createNotificationChannels()

        // 초기 권한 확인
        permissionManager.checkInitialPermissions()

        // FCM 관리자 초기화
        fcmManager.initialize()

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

    // ImageLoader 토큰 초기화 함수
    private fun initializeImageLoaderWithToken() {
        activityScope.launch {
            try {
                // 데이터스토어에서 토큰 불러오기
                val token = dataStoreManager.token.first()
                // ImageLoader에 토큰 설정
                ImageLoader.setAuthToken(token)

                // 토큰 변경 감지하여 ImageLoader 업데이트
                launch {
                    dataStoreManager.token.collect { newToken ->
                        ImageLoader.setAuthToken(newToken)
                    }
                }
            } catch (e: Exception) {
                // 토큰 초기화 실패
            }
        }
    }
}