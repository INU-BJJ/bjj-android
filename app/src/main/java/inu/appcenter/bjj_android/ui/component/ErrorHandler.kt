package inu.appcenter.bjj_android.ui.component

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import inu.appcenter.bjj_android.utils.ErrorHandler
import inu.appcenter.bjj_android.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.collectLatest

/**
 * 중복 토스트 표시를 방지하기 위한 컴포넌트
 * 마지막으로 표시된 에러 메시지를 추적하여 동일한 메시지가 여러 번 표시되는 것을 방지함
 */
@Composable
fun ErrorHandler(viewModel: BaseViewModel) {
    val context = LocalContext.current
    var lastErrorMessage by remember { mutableStateOf<String?>(null) }
    var lastErrorTime by remember { mutableStateOf(0L) }

    // 동일한 에러 메시지를 특정 시간 내에 반복해서 표시하지 않기 위한 시간 간격 (밀리초)
    val ERROR_DISPLAY_COOLDOWN = 1000L

    LaunchedEffect(key1 = true) {
        viewModel.errorEvent.collectLatest { error ->
            val errorMessage = ErrorHandler.getUserFriendlyMessage(error)
            val currentTime = System.currentTimeMillis()

            // 이전에 표시된 메시지와 동일하고, 일정 시간이 지나지 않았으면 표시하지 않음
            if (errorMessage != lastErrorMessage || currentTime - lastErrorTime > ERROR_DISPLAY_COOLDOWN) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                lastErrorMessage = errorMessage
                lastErrorTime = currentTime
            }
        }
    }
}