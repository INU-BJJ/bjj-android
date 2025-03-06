package inu.appcenter.bjj_android.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import inu.appcenter.bjj_android.ui.theme.Gray_F6F6F8

@Composable
fun HorizontalDivider(
    modifier: Modifier = Modifier,
    color: Color = Gray_F6F6F8,
    height: Dp = 7.dp
) {
    Box(
        modifier = modifier
            .height(height)
            .background(color = color)
            .fillMaxWidth()
    )
}