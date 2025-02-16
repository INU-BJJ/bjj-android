package inu.appcenter.bjj_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import inu.appcenter.bjj_android.ui.login.AuthViewModel
import inu.appcenter.bjj_android.ui.main.MainViewModel
import inu.appcenter.bjj_android.ui.menudetail.MenuDetailViewModel
import inu.appcenter.bjj_android.ui.navigate.AppNavigation
import inu.appcenter.bjj_android.ui.ranking.RankingViewModel
import inu.appcenter.bjj_android.ui.review.ReviewViewModel
import inu.appcenter.bjj_android.ui.theme.AppTypography
import inu.appcenter.bjj_android.ui.theme.Bjj_androidTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

internal val LocalTypography = staticCompositionLocalOf { AppTypography() }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val authViewModel : AuthViewModel by viewModel()
            val mainViewModel : MainViewModel by viewModel()
            val menuDetailViewModel : MenuDetailViewModel by viewModel()
            val reviewViewModel: ReviewViewModel by viewModel()
            val rankingViewModel: RankingViewModel by viewModel()

            //하단 바 제거
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
                        rankingViewModel = rankingViewModel)
                }
            }
        }
    }
}

