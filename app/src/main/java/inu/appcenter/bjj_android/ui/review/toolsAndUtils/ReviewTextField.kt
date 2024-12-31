package inu.appcenter.bjj_android.ui.review.toolsAndUtils

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.ui.theme.Gray_B9B9B9

@Composable
fun ReviewTextField(
    text: String, // 상위 컴포저블에서 전달받은 상태
    onTextChange: (String) -> Unit
) {

    val maxCharCount = 500
    val scrollState = rememberScrollState()


    Box {
        // OutlinedTextField 테두리 생성
        OutlinedTextField(
            value = text,
            onValueChange = {
                if (it.length <= maxCharCount) onTextChange(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(158.dp)
                .border(1.dp, Gray_B9B9B9, shape = RoundedCornerShape(4.dp)),
            placeholder = {
                Text(
                    modifier = Modifier.padding(bottom = 8.dp, start = 1.dp),
                    text = "텍스트 리뷰는 000P, 포토리뷰는 000P 드려요.",
                    color = Gray_B9B9B9,
                    style = LocalTypography.current.regular13.copy(
                        letterSpacing = 0.13.sp,
                        lineHeight = 17.sp,
                    )
                )
            },
            maxLines = Int.MAX_VALUE,
            readOnly = true,  // 내부에서 BasicTextField를 사용할 것이므로 읽기 전용으로 설정
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Transparent,   // 배경을 투명하게 설정하여 겹침 방지
                unfocusedTextColor = Color.Transparent,
            )
        )

        // 내부에서 스크롤 가능한 텍스트 필드 구현
        BasicTextField(value = text,
            onValueChange = {
                if (it.length <= maxCharCount) onTextChange(it)
            },
            modifier = Modifier
                .padding(top = 16.dp, start = 17.dp)
                .height(103.7.dp)
                .width(285.dp)
                .verticalScroll(scrollState)
                .verticalScrollbar(scrollState),
            textStyle = LocalTypography.current.regular13.copy(
                letterSpacing = 0.13.sp,
                lineHeight = 17.sp,
            ),
            decorationBox = { innerTextField ->
                Box(modifier = Modifier) {
                    innerTextField() // 실제 텍스트를 내부에 표시
                }
            }
        )
        WriteMaxCount(text, 500)
    }
}

fun Modifier.verticalScrollbar(
    scrollState: ScrollState,
    scrollBarWidth: Dp = 3.dp,
    minScrollBarHeight: Dp = 51.dp,
    scrollBarColor: Color = Gray_B9B9B9,
    cornerRadius: Dp = 3.dp
): Modifier = composed {
    val targetAlpha = if (scrollState.isScrollInProgress) 1f else 0f
    val duration = if (scrollState.isScrollInProgress) 150 else 500

    val alpha by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = tween(durationMillis = duration),
        label = "Float Animation"
    )

    drawWithContent {
        drawContent()

        val needDrawScrollbar = scrollState.isScrollInProgress || alpha > 0.0f

        if (needDrawScrollbar && scrollState.maxValue > 0) {
            val visibleHeight: Float = this.size.height - scrollState.maxValue
            val scrollBarHeight: Float = minScrollBarHeight.toPx()
            val scrollPercent: Float = scrollState.value.toFloat() / scrollState.maxValue
            val scrollBarOffsetY: Float =
                scrollState.value + (visibleHeight - scrollBarHeight) * scrollPercent

            drawRoundRect(
                color = scrollBarColor,
                topLeft = Offset(this.size.width - scrollBarWidth.toPx(), scrollBarOffsetY),
                size = Size(scrollBarWidth.toPx(), scrollBarHeight),
                alpha = alpha,
                cornerRadius = CornerRadius(cornerRadius.toPx())
            )
        }
    }
}
