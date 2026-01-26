package inu.appcenter.bjj_android.shared.component.error.handlers

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import inu.appcenter.bjj_android.core.presentation.BaseViewModel
import kotlinx.coroutines.flow.collectLatest

/**
 * 토스트 메시지 처리 컴포넌트
 * BaseViewModel의 toastEvent를 구독하여 토스트 메시지 표시
 */
@Composable
internal fun ToastErrorHandler(viewModel: BaseViewModel) {
    val context = LocalContext.current
    var lastMessage by remember { mutableStateOf<String?>(null) }
    var lastMessageTime by remember { mutableStateOf(0L) }

    // 동일한 메시지를 특정 시간 내에 반복해서 표시하지 않기 위한 시간 간격 (밀리초)
    val MESSAGE_DISPLAY_COOLDOWN = 1000L

    // 토스트 메시지 처리
    LaunchedEffect(key1 = viewModel) {
        viewModel.toastEvent.collectLatest { message ->
            val currentTime = System.currentTimeMillis()
            if (message != lastMessage || currentTime - lastMessageTime > MESSAGE_DISPLAY_COOLDOWN) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                lastMessage = message
                lastMessageTime = currentTime
            }
        }
    }
}