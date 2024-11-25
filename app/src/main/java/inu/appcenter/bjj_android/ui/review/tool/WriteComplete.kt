package inu.appcenter.bjj_android.ui.review.tool

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.ui.theme.Gray_B9B9B9
import inu.appcenter.bjj_android.ui.theme.Orange_FF7800

@Composable
fun WriteComplete(text: String) {

    Box(
        modifier = Modifier
            .width(319.dp)
            .height(47.dp), // 아이콘 크기
        contentAlignment = Alignment.Center  // 텍스트를 중앙에 정렬
    ) {
        when (text.length) {
            0 -> Icon(
                modifier = Modifier
                    .clickable { }
                    .fillMaxSize(),
                painter = painterResource(R.drawable.writecomplete),
                contentDescription = "Uncomplete",
                tint = Gray_B9B9B9
            )

            else -> Icon(
                modifier = Modifier
                    .clickable { }
                    .fillMaxSize(),
                painter = painterResource(R.drawable.writecomplete),
                contentDescription = "Complete",
                tint = Orange_FF7800
            )
        }
        Text(
            text = "작성 완료",
            color = Color.White,
            style = LocalTypography.current.semibold15.copy(
                letterSpacing = 0.13.sp,
                lineHeight = 18.sp,
            )
        )
    }
}

