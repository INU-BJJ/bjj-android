package inu.appcenter.bjj_android.ui.mypage.component

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import inu.appcenter.bjj_android.LocalTypography

@Composable
fun MainText(
    text: String,
    onClick: (() -> Unit)? = null
) {
    Text(
        text = text,
        style = LocalTypography.current.semibold15.copy(
            lineHeight = 18.sp,
            letterSpacing = 0.13.sp,
            fontWeight = FontWeight(600),
            color = Color.Black
        ),
        modifier = Modifier.clickable(enabled = onClick != null) {
            onClick?.invoke()
        }
    )
}