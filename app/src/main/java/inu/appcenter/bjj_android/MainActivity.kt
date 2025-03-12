package inu.appcenter.bjj_android

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.firebase.messaging.FirebaseMessaging
import inu.appcenter.bjj_android.local.DataStoreManager
import inu.appcenter.bjj_android.model.fcm.FcmTokenRequest
import inu.appcenter.bjj_android.network.APIService
import inu.appcenter.bjj_android.ui.login.AuthViewModel
import inu.appcenter.bjj_android.ui.main.MainViewModel
import inu.appcenter.bjj_android.ui.menudetail.MenuDetailViewModel
import inu.appcenter.bjj_android.ui.mypage.setting.likedmenu.LikedMenuViewModel
import inu.appcenter.bjj_android.ui.mypage.setting.nickname.NicknameChangeViewModel
import inu.appcenter.bjj_android.ui.navigate.AppNavigation
import inu.appcenter.bjj_android.ui.ranking.RankingViewModel
import inu.appcenter.bjj_android.ui.review.ReviewViewModel
import inu.appcenter.bjj_android.ui.theme.AppTypography
import inu.appcenter.bjj_android.ui.theme.Bjj_androidTheme
import inu.appcenter.bjj_android.utils.NotificationPermissionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.android.ext.android.inject

internal val LocalTypography = staticCompositionLocalOf { AppTypography() }

class MainActivity : ComponentActivity() {

    private val apiService: APIService by inject()
    private val dataStoreManager: DataStoreManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // FCM토큰 가져오기 및 저장
        retrieveAndSaveFcmToken()

        setContent {
            val authViewModel : AuthViewModel by viewModel()
            val mainViewModel : MainViewModel by viewModel()
            val menuDetailViewModel : MenuDetailViewModel by viewModel()
            val reviewViewModel: ReviewViewModel by viewModel()
            val rankingViewModel: RankingViewModel by viewModel()
            val likedMenuViewModel: LikedMenuViewModel by viewModel()
            val nicknameChangeViewModel: NicknameChangeViewModel by viewModel()

            // Handle notification permissions
            NotificationPermissionHandler()

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

            val uiState by authViewModel.uiState.collectAsState()

            val coroutineScope = rememberCoroutineScope()

            LaunchedEffect(uiState.hasToken) {
                if (uiState.hasToken == true) {
                    coroutineScope.launch {
                        val fcmToken = dataStoreManager.fcmToken.first()
                        fcmToken?.let {
                            try {
                                Log.d("MainActivityFcmToken", fcmToken)
                                apiService.registerFcmToken(FcmTokenRequest(fcmToken))
                            } catch (e: Exception){
                                Log.e("MainActivityFcmToken", "Error registering FCM token", e)
                            }
                        }
                    }
                }
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
                        nicknameChangeViewModel = nicknameChangeViewModel
                    )
                }
            }
        }
    }

    private fun retrieveAndSaveFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("FCM", "Failed to get FCM token", task.exception)
                return@addOnCompleteListener
            }

            // 토큰 저장
            val token = task.result
            CoroutineScope(Dispatchers.IO).launch {
                dataStoreManager.saveFcmToken(token)
                Log.d("FCM", "Saved FCM token: $token")
            }
        }
    }
}