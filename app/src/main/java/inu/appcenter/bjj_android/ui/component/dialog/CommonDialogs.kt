package inu.appcenter.bjj_android.ui.component.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import inu.appcenter.bjj_android.LocalTypography
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
        messageStyle = LocalTypography.current.semibold15.copy(
            letterSpacing = 0.13.sp,
            lineHeight = 18.sp,
            textAlign = TextAlign.Center
        ),
        iconResId = R.drawable.checkcircle,
        autoDismissTime = 1000L,
        contentSize = DialogContentSize.Fixed
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
        messageStyle = LocalTypography.current.semibold15.copy(
            letterSpacing = 0.13.sp,
            lineHeight = 18.sp,
            textAlign = TextAlign.Center
        ),
        iconResId = R.drawable.checkcircle,
        autoDismissTime = 1000L,
        contentSize = DialogContentSize.Fixed
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
        messageStyle = LocalTypography.current.semibold15.copy(
            letterSpacing = 0.13.sp,
            lineHeight = 18.sp,
            textAlign = TextAlign.Center
        ),
        iconResId = R.drawable.xmark_circle,
        iconTint = Orange_FF7800,
        autoDismissTime = 1000L,
        contentSize = DialogContentSize.Fixed
    )
}

/**
 * 정지 사유 다이얼로그
 * 정지 기간과 사유를 표시, 피그마 스펙에 맞게 스타일링
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
        titleColor = Red_FF3916,
        titleStyle = LocalTypography.current.bold18.copy(
            letterSpacing = 0.13.sp,
            lineHeight = 15.sp,
            textAlign = TextAlign.Center
        ),
        content = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 사유 메시지 - 검은색, 15pt 미디엄
                Text(
                    text = "누적 5회 신고로 인해 리뷰 작성 제재",
                    style = LocalTypography.current.medium15.copy(
                        letterSpacing = 0.13.sp,
                        lineHeight = 15.sp
                    ),
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 기간 - 빨간색, 13pt 미디엄
                Text(
                    text = "$startDate ~ $endDate",
                    style = LocalTypography.current.medium13.copy(
                        letterSpacing = 0.13.sp,
                        lineHeight = 17.sp
                    ),
                    color = Red_FF3916,
                    textAlign = TextAlign.Center
                )
            }
        },
        iconResId = R.drawable.xmark_circle,
        iconTint = Orange_FF7800,
        autoDismissTime = null,
        contentSize = DialogContentSize.Dynamic,  // 동적 크기 사용
        isSuspensionDialog = true  // SuspensionReasonDialog임을 표시
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