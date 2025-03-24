package inu.appcenter.bjj_android.ui.component

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import inu.appcenter.bjj_android.ui.navigate.AllDestination
import inu.appcenter.bjj_android.utils.AppError
import inu.appcenter.bjj_android.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.android.ext.android.inject
import inu.appcenter.bjj_android.local.DataStoreManager
import org.koin.compose.koinInject

/**
 * 중복 토스트 표시를 방지하기 위한 컴포넌트
 * 마지막으로 표시된 에러 메시지를 추적하여 동일한 메시지가 여러 번 표시되는 것을 방지함
 */
@Composable
fun ErrorHandler(viewModel: BaseViewModel, navController: NavHostController? = null) {
    val context = LocalContext.current
    var lastMessage by remember { mutableStateOf<String?>(null) }
    var lastMessageTime by remember { mutableStateOf(0L) }

    val dataStoreManager = koinInject<DataStoreManager>()

    // 동일한 메시지를 특정 시간 내에 반복해서 표시하지 않기 위한 시간 간격 (밀리초)
    val MESSAGE_DISPLAY_COOLDOWN = 1000L

    // 토스트 메시지 처리
    LaunchedEffect(key1 = Unit) {
        viewModel.toastEvent.collectLatest { message ->
            val currentTime = System.currentTimeMillis()
            if (message != lastMessage || currentTime - lastMessageTime > MESSAGE_DISPLAY_COOLDOWN) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                lastMessage = message
                lastMessageTime = currentTime
            }
        }
    }

    // 오류 이벤트 처리 (오류 추적을 위한 별도 로직이 필요한 경우)
    LaunchedEffect(key1 = Unit) {
        viewModel.errorEvent.collectLatest { error ->
            // 특별한 오류 로깅이나 분석이 필요한 경우 여기에 추가
            // 토스트 메시지는 이미 toastEvent로 처리됨
        }
    }

    // 인증 만료 이벤트 처리
    LaunchedEffect(key1 = Unit) {
        viewModel.authExpiredEvent.collectLatest {
            // DataStore에서 토큰 제거
            dataStoreManager.clearToken()

            // 로그인 화면으로 이동
            navController?.navigate(AllDestination.Login.route) {
                popUpTo(0) { inclusive = true }
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