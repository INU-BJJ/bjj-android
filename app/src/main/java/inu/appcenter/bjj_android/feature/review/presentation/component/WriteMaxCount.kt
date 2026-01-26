package inu.appcenter.bjj_android.feature.review.presentation.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.shared.theme.Gray_B9B9B9

@Composable
fun WriteMaxCount(text: String, maxCharCount: Int) {
    // AnnotatedString 빌더를 사용하여 스타일이 적용된 텍스트 생성
    val annotatedText = buildAnnotatedString {
        if (text.isNotEmpty()) {
            // 글자 수 부분을 검은색으로
            withStyle(style = SpanStyle(color = Color.Black)) {
                append("${text.length}")
            }
        } else {
            // 글자 수 부분을 기본 색상으로 (여기서는 회색)
            withStyle(style = SpanStyle(color = Gray_B9B9B9)) {
                append("${text.length}")
            }
        }

        // 나머지 "/ $maxCharCount" 부분
        append(" / $maxCharCount")
    }

    // BasicText 또는 Text로 스타일 적용된 텍스트 표시
    Text(
        modifier = Modifier.padding(top = 129.dp, start = 250.dp),
        text = annotatedText,
        style = LocalTypography.current.regular13.copy(
            letterSpacing = 0.13.sp,
            lineHeight = 17.sp,
        ),
        color = Gray_B9B9B9
    )
}
