package inu.appcenter.bjj_android.feature.auth.presentation.login.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import inu.appcenter.bjj_android.LocalTypography

@Composable
fun SocialLoginButton(
    modifier: Modifier = Modifier,
    socialLogin: String,
    onClick: (String) -> Unit,
    background: Color,
    text: String,
    textColor: Color = Color.Black,
    icon: Painter,
    isBorderStroke: Boolean = false
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(color = background, shape = RoundedCornerShape(size = 10.dp))
            .border(
                width = if (isBorderStroke) 1.dp else 0.dp,
                color = if (isBorderStroke) Color(0xFFB9B9B9) else Color.Transparent,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable {
                onClick(socialLogin)
            },
    ){
        Icon(
            painter = icon,
            contentDescription = "kakao login",
            tint = Color.Unspecified,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 24.dp)
                .width(27.dp)
        )
        Text(
            text = text,
            style = LocalTypography.current.medium15.copy(
                letterSpacing = 0.13.sp,
                color = textColor
            ),
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}