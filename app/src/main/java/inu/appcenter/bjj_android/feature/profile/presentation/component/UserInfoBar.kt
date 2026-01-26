package inu.appcenter.bjj_android.feature.profile.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.shared.theme.Orange_FF7800

@Composable
fun UserInfoBar(
    modifier: Modifier = Modifier,
    userName: String
){
    val density = LocalDensity.current
    var textWidth by remember { mutableStateOf(0.dp) }
    val dividerWidth = 10.dp
    val dividerHeight = 30.dp
    val dividerOffset = 0.dp

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .wrapContentWidth(),
            verticalAlignment = Alignment.Bottom
        ) {
            // 왼쪽 divider
            Box(
                modifier = Modifier
                    .width(dividerWidth)
                    .height(dividerHeight)
                    .offset(x = -(textWidth/3.5f + dividerOffset))
                    .background(Orange_FF7800)
            )

            // 오른쪽 divider
            Box(
                modifier = Modifier
                    .width(dividerWidth)
                    .height(dividerHeight)
                    .offset(x = (textWidth/3.5f + dividerOffset))
                    .background(Orange_FF7800)
            )
        }
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .background(Color.White, shape = RoundedCornerShape(20.dp))
                .border(width = 2.dp, color = Orange_FF7800, shape = RoundedCornerShape(20.dp))
                .padding(vertical = 10.dp, horizontal = 15.dp)
                .onGloballyPositioned { coordinates ->
                    // 텍스트 너비 측정
                    textWidth = with(density) { coordinates.size.width.toDp() }
                }
        ) {
            Text(
                text = "${userName}의 공간",
                style = LocalTypography.current.bold18.copy(color = Color.Black)
            )
        }
    }
}