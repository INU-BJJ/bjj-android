package inu.appcenter.bjj_android.ui.component.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.utils.CircleRectShape
import kotlinx.coroutines.delay

/**
 * 커스터마이징 가능한 알림 다이얼로그
 * 텍스트 내용에 따라 크기가 자동 조절되며 여러 유형의 메시지를 표시할 수 있음
 *
 * @param show 다이얼로그 표시 여부
 * @param onDismiss 다이얼로그 닫기 콜백
 * @param title 다이얼로그 제목 (옵션)
 * @param message 다이얼로그 메시지
 * @param iconResId 아이콘 리소스 ID (기본값: 체크 아이콘)
 * @param iconTint 아이콘 색상 (기본값: 지정 안함 - 원본 색상 사용)
 * @param autoDismissTime 자동 닫기 시간(ms) (기본값: 1000ms, null이면 자동 닫기 비활성화)
 * @param textColor 텍스트 색상 (기본값: 검정)
 * @param isError 에러 메시지 여부 (기본값: false)
 */
@Composable
fun CustomAlertDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    title: String? = null,
    message: String,
    iconResId: Int,
    iconTint: Color? = null,
    autoDismissTime: Long? = 1000L,
    textColor: Color = Color.Black,
    isError: Boolean = false,
    dialogStyle: DialogStyle = DialogStyle.Standard
) {
    if (show) {
        // 자동 닫기 설정
        autoDismissTime?.let {
            LaunchedEffect(Unit) {
                delay(it)
                onDismiss()
            }
        }

        val localDensity = LocalDensity.current
        var textWidth by remember { mutableStateOf(0.dp) }
        var textHeight by remember { mutableStateOf(0.dp) }

        // 유형별 스타일 정의 적용
        val style = when (dialogStyle) {
            DialogStyle.Standard -> DialogStyleValues(
                minWidth = 240.dp,
                iconSize = 48.dp,
                iconTopPadding = 11.dp,
                textTopPadding = if (title != null) 14.dp else 16.dp,
                additionalHeightPadding = if (title != null) 35.dp else 30.dp
            )
            DialogStyle.Compact -> DialogStyleValues(
                minWidth = 220.dp,
                iconSize = 44.dp,
                iconTopPadding = 9.dp,
                textTopPadding = if (title != null) 12.dp else 14.dp,
                additionalHeightPadding = if (title != null) 25.dp else 20.dp
            )
            DialogStyle.Expanded -> DialogStyleValues(
                minWidth = 260.dp,
                iconSize = 52.dp,
                iconTopPadding = 13.dp,
                textTopPadding = if (title != null) 18.dp else 20.dp,
                additionalHeightPadding = if (title != null) 45.dp else 40.dp
            )
        }

        // CircleRectShape 파라미터
        val circleDiameter = 79.dp
        val visibleCircleHeight = 46.dp
        val bottomTextPadding = 24.dp

        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Box(
                modifier = Modifier
                    .background(Color.Transparent)
                    .wrapContentSize()
            ) {
                // 텍스트 크기에 맞게 다이얼로그 크기 계산
                val dialogWidth = maxOf(style.minWidth, textWidth + 40.dp)

                // 텍스트 길이에 따른 동적 계산
                val contentScale = (textHeight / 18.dp).coerceIn(1f, 2.5f) // 텍스트 높이 비율 계산
                val textAreaHeight = if (title != null) {
                    textHeight + (40.dp * contentScale).coerceAtMost(60.dp)
                } else {
                    textHeight + bottomTextPadding
                }

                val dialogHeight = visibleCircleHeight + textAreaHeight + style.additionalHeightPadding

                Box(
                    modifier = Modifier
                        .widthIn(min = dialogWidth)
                        .height(dialogHeight)
                        .background(
                            color = Color.White,
                            shape = CircleRectShape(
                                circleDiameter = circleDiameter,
                                visibleCircleHeight = visibleCircleHeight
                            )
                        )
                ) {
                    // 아이콘 - 동적으로 위치 조정
                    Image(
                        painter = painterResource(id = iconResId),
                        contentDescription = null,
                        modifier = Modifier
                            .size(style.iconSize)
                            .align(Alignment.TopCenter)
                            .offset(y = style.iconTopPadding),
                        colorFilter = iconTint?.let { androidx.compose.ui.graphics.ColorFilter.tint(it) }
                    )

                    // 텍스트 컨테이너 - 동적으로 간격 조정
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 24.dp,
                                top = style.textTopPadding
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        title?.let {
                            Text(
                                text = it,
                                style = LocalTypography.current.semibold15.copy(
                                    letterSpacing = 0.13.sp,
                                    lineHeight = 18.sp
                                ),
                                color = if (isError) textColor else textColor,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            // 제목과 메시지 사이 간격 - 메시지 길이에 따라 동적 조정
                            Spacer(modifier = Modifier.height((4 + contentScale * 2).dp))
                        }

                        Text(
                            text = message,
                            style = LocalTypography.current.medium15.copy(
                                letterSpacing = 0.13.sp,
                                lineHeight = 18.sp
                            ),
                            color = textColor,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .wrapContentWidth()
                                .wrapContentHeight()
                                .onGloballyPositioned { coordinates ->
                                    textWidth = with(localDensity) { coordinates.size.width.toDp() }
                                    textHeight = with(localDensity) { coordinates.size.height.toDp() }
                                }
                        )
                    }
                }
            }
        }
    }
}

// 다이얼로그 스타일 정의
enum class DialogStyle {
    Standard,  // 기본 스타일
    Compact,   // 간결한 스타일 (간격 축소)
    Expanded   // 확장 스타일 (간격 확대)
}

// 스타일별 수치 값
data class DialogStyleValues(
    val minWidth: Dp,
    val iconSize: Dp,
    val iconTopPadding: Dp,
    val textTopPadding: Dp,
    val additionalHeightPadding: Dp
)