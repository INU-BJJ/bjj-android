package inu.appcenter.bjj_android.ui.review.tool

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.ui.theme.Black_1E1E1E
import inu.appcenter.bjj_android.ui.theme.Gray_B9B9B9

@Composable
fun DashedBorderBox(
    imageUri: Uri?,
    onClickAddImage: () -> Unit,
    showRemoveButton: Boolean,
    onRemoveImage: () -> Unit,
    currentCounting: Int
) {
    Box(
        modifier = Modifier
            .size(75.dp) // 크기 설정
            .drawBehind {
                val dashWidth = 2.dp.toPx() // 점선 길이
                val dashGap = 2.dp.toPx() // 점선 간격
                val pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashWidth, dashGap), 0f)

                drawRoundRect(
                    color = Gray_B9B9B9,
                    style = Stroke(
                        width = 1.5.dp.toPx(),
                        pathEffect = pathEffect,
                        cap = StrokeCap.Butt
                    ),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(3.dp.toPx()) // 모서리 둥글게
                )
            },
        contentAlignment = Alignment.Center
    ) {
        if (imageUri == null) {
            // 이미지가 없을 때 기존 디자인 유지
            Column(
                modifier = Modifier
                    .padding(top = 18.dp, bottom = 13.dp)
                    .clickable { onClickAddImage() },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.camera),
                    contentDescription = "이미지 넣기",
                    tint = Black_1E1E1E
                )
                Spacer(modifier = Modifier.height(5.dp))
                PhotoCountingCalculator(photoCounting = currentCounting)
            }
        } else {
            // 이미지가 있을 경우 이미지 꽉 채우기
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = rememberAsyncImagePainter(model = imageUri),
                    contentDescription = "선택된 이미지",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                if (showRemoveButton) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        IconButton(
                            onClick = { onRemoveImage() },
                            modifier = Modifier
                                .padding(3.dp)
                                .size(18.dp)
                                .background(Color.Transparent, shape = CircleShape)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.xbutton), // X 아이콘 리소스
                                contentDescription = "이미지 제거",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}