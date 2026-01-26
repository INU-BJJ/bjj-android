package inu.appcenter.bjj_android.shared.component.error.handlers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.shared.component.dialog.CustomAlertDialog
import inu.appcenter.bjj_android.shared.component.dialog.DialogContentSize
import inu.appcenter.bjj_android.shared.component.dialog.FailureDialog
import inu.appcenter.bjj_android.shared.component.dialog.SuspensionReasonDialog
import inu.appcenter.bjj_android.shared.theme.Orange_FF7800
import inu.appcenter.bjj_android.core.util.AppError
import inu.appcenter.bjj_android.core.presentation.BaseViewModel
import kotlinx.coroutines.flow.collectLatest
import java.util.regex.Pattern

/**
 * 에러 다이얼로그 처리 컴포넌트
 * 에러 유형에 따른 적절한 다이얼로그 표시 담당
 */
@Composable
internal fun DialogErrorHandler(viewModel: BaseViewModel) {
    var currentError by remember { mutableStateOf<Throwable?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    // ViewModel의 errorEvent 구독
    LaunchedEffect(key1 = viewModel) {
        viewModel.errorEvent.collectLatest { error ->
            currentError = error
            showDialog = true
        }
    }

    // 에러 다이얼로그 표시
    if (currentError != null && showDialog) {
        DisplayErrorDialog(
            error = currentError!!,
            onDismiss = { showDialog = false }
        )
    }
}

/**
 * 에러 유형에 따른 적절한 다이얼로그 표시
 */
@Composable
private fun DisplayErrorDialog(
    error: Throwable,
    onDismiss: () -> Unit
) {
    // 구체적인 에러 타입별 처리
    when (error) {
        // 일시 정지 에러 처리 (정지 사유 다이얼로그)
        is AppError.ApiError -> {
            // 정지 기간이 포함된 에러 메시지 패턴 확인
            val suspensionPattern = Pattern.compile("누적.*신고.*제재.*([0-9]{4}.[0-9]{2}.[0-9]{2}).*([0-9]{4}.[0-9]{2}.[0-9]{2})")
            val matcher = suspensionPattern.matcher(error.message)

            if (matcher.find() && matcher.groupCount() >= 2) {
                // 정지 기간이 포함된 경우
                SuspensionReasonDialog(
                    show = true,
                    onDismiss = onDismiss,
                    startDate = matcher.group(1) ?: "",
                    endDate = matcher.group(2) ?: ""
                )
            } else if (error.statusCode == 403 && error.message.contains("정지")) {
                // 정지 기간은 없지만 정지 관련 메시지
                CustomAlertDialog(
                    show = true,
                    onDismiss = onDismiss,
                    title = "정지 사유",
                    message = error.message,
                    iconResId = R.drawable.xmark_circle,
                    iconTint = Orange_FF7800,  // isError 대신 명시적으로 오렌지 색상 지정
                    autoDismissTime = null,
                    contentSize = DialogContentSize.Dynamic  // 동적 컨텐츠 크기 사용
                )
            } else {
                // 일반 API 에러
                FailureDialog(
                    show = true,
                    onDismiss = onDismiss,
                    message = error.message
                )
            }
        }

        // 네트워크 에러
        is AppError.NetworkError -> {
            CustomAlertDialog(
                show = true,
                onDismiss = onDismiss,
                message = error.message,
                iconResId = R.drawable.xmark_circle,
                iconTint = Orange_FF7800,  // isError 대신 명시적으로 오렌지 색상 지정
                autoDismissTime = 2000L,
                contentSize = DialogContentSize.Fixed  // 고정 크기 사용
            )
        }

        // 인증 에러
        is AppError.AuthError -> {
            if (error.isExpired) {
                CustomAlertDialog(
                    show = true,
                    onDismiss = onDismiss,
                    message = "로그인이 만료되었습니다. 다시 로그인해주세요.",
                    iconResId = R.drawable.xmark_circle,
                    iconTint = Orange_FF7800,  // isError 대신 명시적으로 오렌지 색상 지정
                    autoDismissTime = 2000L,
                    contentSize = DialogContentSize.Fixed  // 고정 크기 사용
                )
            } else {
                CustomAlertDialog(
                    show = true,
                    onDismiss = onDismiss,
                    message = error.message,
                    iconResId = R.drawable.xmark_circle,
                    iconTint = Orange_FF7800,  // isError 대신 명시적으로 오렌지 색상 지정
                    autoDismissTime = 2000L,
                    contentSize = DialogContentSize.Fixed  // 고정 크기 사용
                )
            }
        }

        // 기타 모든 에러
        else -> {
            FailureDialog(
                show = true,
                onDismiss = onDismiss,
                message = AppError.getUserFriendlyMessage(error)
            )
        }
    }
}