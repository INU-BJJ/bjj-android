package inu.appcenter.bjj_android.shared.component.error.handlers

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import inu.appcenter.bjj_android.core.data.local.DataStoreManager
import inu.appcenter.bjj_android.shared.navigation.AllDestination
import inu.appcenter.bjj_android.core.presentation.BaseViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.koinInject

/**
 * 인증 만료 처리 컴포넌트
 * BaseViewModel의 authExpiredEvent를 구독하여 인증 만료 시 처리
 */
@Composable
internal fun AuthErrorHandler(
    viewModel: BaseViewModel,
    navController: NavHostController?
) {
    val context = LocalContext.current
    val dataStoreManager = koinInject<DataStoreManager>()

    // 인증 만료 이벤트 처리
    LaunchedEffect(key1 = viewModel) {
        viewModel.authExpiredEvent.collectLatest {
            // DataStore에서 토큰 제거
            dataStoreManager.clearToken()

            // 로그인 화면으로 이동 (NavController가 제공된 경우)
            navController?.navigate(AllDestination.Login.route) {
                popUpTo(0) { inclusive = true }  // 백스택 완전히 비우기
            }

            // 사용자에게 알림
            Toast.makeText(
                context,
                "로그인이 만료되었습니다. 다시 로그인해주세요.",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}