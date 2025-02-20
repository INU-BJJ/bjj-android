package inu.appcenter.bjj_android.ui.review.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.ui.review.toolsAndUtils.CircleRectShape
import inu.appcenter.bjj_android.ui.theme.Bjj_androidTheme

@Composable
fun ReviewCompletedDialog(
    show: Boolean,
    onDismiss: () -> Unit
) {
    if (show) {
        // 1초 후 자동 닫기
        LaunchedEffect(Unit) {
            kotlinx.coroutines.delay(1000)
            onDismiss()
        }

        Dialog(onDismissRequest = onDismiss) {
            // 다이얼로그 내부 컨테이너
            Box(
                modifier = Modifier
                    .background(Color.Transparent)
                    .wrapContentSize() // 내용만큼 크기 잡기
            ) {
                // "원 + 사각형" 일체형 모양
                Box(
                    modifier = Modifier
                        // 원하는 전체 크기를 지정(너비, 높이)
                        .size(width = 240.dp, height = 65.dp)
                        .background(color = Color.White, shape = CircleRectShape)
                ) {
                    // (1) 체크 아이콘
                    Image(
                        painter = painterResource(id = R.drawable.checkcircle),
                        contentDescription = null,
                        modifier = Modifier
                            .size(32.dp)
                            .align(Alignment.TopCenter)
                            // 원이 상단 0~48dp(대략) 구간에 그려지므로,
                            // 아이콘을 약간 아래로 내려서 원 내부에 배치
                            .offset(y = 7.dp)
                    )

                    // (2) 텍스트
                    Text(
                        text = "완료되었습니다!",
                        style = LocalTypography.current.medium15.copy(
                            letterSpacing = 0.13.sp,
                            lineHeight = 18.sp
                        ),
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .offset(y = (-8).dp) // 아래로 8dp 여백

                    )
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun ReviewCompletedDialogPreview() {
    // 미리보기에서는 1초 후 자동 닫히지 않도록
    // LaunchedEffect 부분을 주석 처리하거나
    // onDismiss()를 빈 함수로 두면 계속 미리보기 가능
    Bjj_androidTheme {
        ReviewCompletedDialog(
            show = true,
            onDismiss = {} // 아무 동작도 하지 않는 함수
        )
    }
}
