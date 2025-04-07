package inu.appcenter.bjj_android.ui.component.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.ui.theme.Bjj_androidTheme
import inu.appcenter.bjj_android.ui.theme.Orange_FF7800
import inu.appcenter.bjj_android.ui.theme.Red_FF3916

/**
 * 리뷰 작성 완료 다이얼로그
 * "작성 완료" 메시지를 표시하고 1초 후 자동으로 닫힘
 */
@Composable
fun ReviewCompletedDialog(
    show: Boolean,
    onDismiss: () -> Unit
) {
    CustomAlertDialog(
        show = show,
        onDismiss = onDismiss,
        message = stringResource(R.string.review_write_complete),
        iconResId = R.drawable.checkcircle,
        autoDismissTime = 1000L
    )
}

/**
 * 일반 성공 다이얼로그
 * 커스텀 메시지와 함께 성공 아이콘을 표시
 */
@Composable
fun SuccessDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    message: String
) {
    CustomAlertDialog(
        show = show,
        onDismiss = onDismiss,
        message = message,
        iconResId = R.drawable.checkcircle,
        autoDismissTime = 1000L
    )
}

/**
 * 실패 다이얼로그
 * 실패 메시지와 X 아이콘을 표시
 */
@Composable
fun FailureDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    message: String = "실패하였습니다!"
) {
    CustomAlertDialog(
        show = show,
        onDismiss = onDismiss,
        message = message,
        iconResId = R.drawable.xmark_circle,
        iconTint = Orange_FF7800,
        autoDismissTime = 1000L
    )
}

/**
 * 정지 사유 다이얼로그
 * 정지 기간과 사유를 표시
 */
@Composable
fun SuspensionReasonDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    startDate: String,
    endDate: String
) {
    CustomAlertDialog(
        show = show,
        onDismiss = onDismiss,
        title = "정지 사유",
        message = "누적 5회 신고로 인해 리뷰 작성 제재\n$startDate ~ $endDate",
        iconResId = R.drawable.xmark_circle,
        iconTint = Orange_FF7800,
        autoDismissTime = null,
        isError = true,
        textColor = Red_FF3916
    )
}

// 프리뷰 섹션

@Preview(showBackground = true)
@Composable
fun ReviewCompletedDialogPreview() {
    Bjj_androidTheme {
        ReviewCompletedDialog(
            show = true,
            onDismiss = {} // 아무 동작도 하지 않는 함수
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FailureDialogPreview() {
    Bjj_androidTheme {
        FailureDialog(
            show = true,
            onDismiss = {},
            message = "실패하였습니다!"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SuspensionReasonDialogPreview() {
    Bjj_androidTheme {
        SuspensionReasonDialog(
            show = true,
            onDismiss = {},
            startDate = "2023.04.08",
            endDate = "2023.04.15"
        )
    }
}

@Preview(showBackground = true, name = "Success Dialog")
@Composable
fun SuccessDialogPreview() {
    Bjj_androidTheme {
        SuccessDialog(
            show = true,
            onDismiss = {},
            message = "완료되었습니다!"
        )
    }
}