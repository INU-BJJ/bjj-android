package inu.appcenter.bjj_android.ui.review.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.ui.review.component.CircleRectShape
import inu.appcenter.bjj_android.ui.theme.Bjj_androidTheme
import kotlinx.coroutines.delay

@Composable
fun ReviewCompletedDialog(
    show: Boolean,
    onDismiss: () -> Unit
) {
    if (show) {
        // 10초 후 자동 닫기
        LaunchedEffect(Unit) {
            delay(10000)
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
                // 전체 높이는 원의 보이는 부분(46dp) + 사각형 높이(65dp)에서 겹치는 부분 고려
                Box(
                    modifier = Modifier
                        .width(247.dp)
                        .height(111.dp) // 46dp(보이는 원 부분) + 65dp(사각형) = 111dp
                        .background(color = Color.White, shape = CircleRectShape)
                ) {
                    // (1) 체크 아이콘 - 상단 원 중앙에 배치
                    Image(
                        painter = painterResource(id = R.drawable.checkcircle),
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.TopCenter)
                            .offset(y = 11.dp) // 원 내부에 정중앙 배치되도록 오프셋 조정
                    )

                    // (2) 텍스트 - 사각형 부분에 중앙 배치
                    Text(
                        text = stringResource(R.string.review_write_complete),
                        style = LocalTypography.current.medium15.copy(
                            letterSpacing = 0.13.sp,
                            lineHeight = 18.sp,
                            color = Color.Black
                        ),
                        modifier = Modifier
                            .align(Alignment.BottomCenter) // 사각형 부분 중앙에 배치
                            .offset(y = (-24).dp) // 사각형 부분 중앙에 맞추기 위해 위로 오프셋
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