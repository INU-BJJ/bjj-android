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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.utils.CircleRectShape
import kotlinx.coroutines.delay

// 다이얼로그 컨텐츠 크기 정의
enum class DialogContentSize {
    Fixed,    // 고정된 기본 크기 (모든 다이얼로그에서 일관되게 사용)
    Dynamic   // 컨텐츠에 따라 동적으로 조절되는 크기
}

// 아이콘 관련 상수
object DialogDefaults {
    val IconSize = 48.dp
    val IconTopPadding = 11.dp
}

// 스타일별 수치 값
data class DialogStyleValues(
    val minWidth: Dp,
    val contentTopPadding: Dp,
    val additionalHeightPadding: Dp,
    val bottomContentPadding: Dp
)

/**
 * 커스터마이징 가능한 알림 다이얼로그
 * 텍스트 내용에 따라 크기가 자동 조절되며 여러 유형의 메시지를 표시할 수 있음
 */
@Composable
fun CustomAlertDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    title: String? = null,
    titleColor: Color = Color.Black,
    titleStyle: TextStyle = LocalTypography.current.semibold15.copy(
        letterSpacing = 0.13.sp,
        lineHeight = 18.sp
    ),
    content: @Composable () -> Unit,
    iconResId: Int,
    iconTint: Color? = null,
    autoDismissTime: Long? = 1000L,
    contentSize: DialogContentSize = DialogContentSize.Fixed,
    // SuspensionReasonDialog 여부를 나타내는 파라미터 추가
    isSuspensionDialog: Boolean = false
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
        var contentWidth by remember { mutableStateOf(0.dp) }
        var contentHeight by remember { mutableStateOf(0.dp) }

        // 유형별 스타일 정의 적용
        val style = when (contentSize) {
            DialogContentSize.Fixed -> DialogStyleValues(
                minWidth = 240.dp,
                contentTopPadding = if (title != null) 14.dp else 16.dp,
                additionalHeightPadding = if (title != null) 35.dp else 30.dp,
                bottomContentPadding = 24.dp
            )
            DialogContentSize.Dynamic -> DialogStyleValues(
                minWidth = 220.dp,
                contentTopPadding = if (title != null) 8.dp else 10.dp,
                additionalHeightPadding = if (title != null) 18.dp else 14.dp,
                bottomContentPadding = 20.dp
            )
        }

        // CircleRectShape 파라미터 - 고정 값 유지
        val circleDiameter = 79.dp
        val visibleCircleHeight = 46.dp

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
                // 컨텐츠 크기에 맞게 다이얼로그 크기 계산
                val dialogWidth = maxOf(style.minWidth, contentWidth + 40.dp)

                // 컨텐츠 길이에 따른 동적 계산
                val contentScale = (contentHeight / 18.dp).coerceIn(1f, 2.5f)
                val contentAreaHeight = if (title != null) {
                    contentHeight + (40.dp * contentScale).coerceAtMost(60.dp)
                } else {
                    contentHeight + style.bottomContentPadding
                }

                // SuspensionReasonDialog용 특별 오프셋 조정
                val contentOverlap = if (isSuspensionDialog) 12.dp else 0.dp

                // 원과 콘텐츠 영역이 겹치는 부분을 고려하여 전체 높이 계산
                val dialogHeight = visibleCircleHeight + contentAreaHeight + style.additionalHeightPadding - contentOverlap

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
                    // 아이콘 - 크기 고정
                    Image(
                        painter = painterResource(id = iconResId),
                        contentDescription = null,
                        modifier = Modifier
                            .size(DialogDefaults.IconSize)
                            .align(Alignment.TopCenter)
                            .offset(y = DialogDefaults.IconTopPadding),
                        colorFilter = iconTint?.let { androidx.compose.ui.graphics.ColorFilter.tint(it) }
                    )

                    // 컨텐츠 컨테이너 - SuspensionReasonDialog인 경우만 위로 올림
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .offset(y = if (isSuspensionDialog) -contentOverlap else 0.dp)
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                                bottom = style.bottomContentPadding,
                                top = style.contentTopPadding
                            )
                            .onGloballyPositioned { coordinates ->
                                contentWidth = with(localDensity) { coordinates.size.width.toDp() }
                                contentHeight = with(localDensity) { coordinates.size.height.toDp() }
                            },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        title?.let {
                            Text(
                                text = it,
                                style = titleStyle,
                                color = titleColor,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            // 제목과 컨텐츠 사이 간격 - 컨텐츠 길이에 따라 동적 조정
                            Spacer(modifier = Modifier.height((4 + contentScale * 2).dp))
                        }

                        content()
                    }
                }
            }
        }
    }
}

/**
 * 단순 문자열 메시지를 표시하는 다이얼로그 간편 버전
 */
@Composable
fun CustomAlertDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    title: String? = null,
    titleColor: Color = Color.Black,
    titleStyle: TextStyle = LocalTypography.current.semibold15.copy(
        letterSpacing = 0.13.sp,
        lineHeight = 18.sp
    ),
    message: String,
    messageColor: Color = Color.Black,
    messageStyle: TextStyle = LocalTypography.current.semibold15.copy(
        letterSpacing = 0.13.sp,
        lineHeight = 18.sp,
        textAlign = TextAlign.Center
    ),
    iconResId: Int,
    iconTint: Color? = null,
    autoDismissTime: Long? = 1000L,
    contentSize: DialogContentSize = DialogContentSize.Fixed,
    isSuspensionDialog: Boolean = false
) {
    CustomAlertDialog(
        show = show,
        onDismiss = onDismiss,
        title = title,
        titleColor = titleColor,
        titleStyle = titleStyle,
        content = {
            Text(
                text = message,
                style = messageStyle,
                color = messageColor,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
            )
        },
        iconResId = iconResId,
        iconTint = iconTint,
        autoDismissTime = autoDismissTime,
        contentSize = contentSize,
        isSuspensionDialog = isSuspensionDialog
    )
}

/**
 * 스타일이 적용된 메시지를 표시하는 다이얼로그 (AnnotatedString 지원)
 */
@Composable
fun CustomAlertDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    title: String? = null,
    titleColor: Color = Color.Black,
    titleStyle: TextStyle = LocalTypography.current.semibold15.copy(
        letterSpacing = 0.13.sp,
        lineHeight = 18.sp
    ),
    message: AnnotatedString,
    messageStyle: TextStyle = LocalTypography.current.semibold15.copy(
        letterSpacing = 0.13.sp,
        lineHeight = 18.sp,
        textAlign = TextAlign.Center
    ),
    iconResId: Int,
    iconTint: Color? = null,
    autoDismissTime: Long? = 1000L,
    contentSize: DialogContentSize = DialogContentSize.Fixed,
    isSuspensionDialog: Boolean = false
) {
    CustomAlertDialog(
        show = show,
        onDismiss = onDismiss,
        title = title,
        titleColor = titleColor,
        titleStyle = titleStyle,
        content = {
            Text(
                text = message,
                style = messageStyle,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
            )
        },
        iconResId = iconResId,
        iconTint = iconTint,
        autoDismissTime = autoDismissTime,
        contentSize = contentSize,
        isSuspensionDialog = isSuspensionDialog
    )
}