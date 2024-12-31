package inu.appcenter.bjj_android.ui.review.toolsAndUtils

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
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
    // 동일한 모서리 반경 사용
    val cornerRadius = RoundedCornerShape(3.dp)

    Box(
        modifier = Modifier
            .size(75.dp)
            .clip(cornerRadius) // 클리핑 적용
            .then(
                if (imageUri == null) {
                    Modifier.drawBehind {
                        val strokeWidth = 1.5.dp.toPx()
                        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(2.dp.toPx(), 2.dp.toPx()), 0f)

                        // 인셋을 적용하여 Stroke가 Box 내부에 완전히 그려지도록 함
                        drawRoundRect(
                            color = Gray_B9B9B9,
                            topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
                            size = Size(size.width - strokeWidth, size.height - strokeWidth),
                            cornerRadius = CornerRadius(3.dp.toPx()),
                            style = Stroke(
                                width = strokeWidth,
                                pathEffect = pathEffect,
                                cap = StrokeCap.Butt
                            )
                        )
                    }
                } else {
                    Modifier.border(
                        width = 1.5.dp,
                        color = Gray_B9B9B9,
                        shape = cornerRadius
                    )
                }
            ),
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
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(cornerRadius), // 이미지에도 클리핑 적용
                    contentScale = ContentScale.Crop
                )

                if (showRemoveButton) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.imagexbutton),
                            contentDescription = "이미지 제거",
                            modifier = Modifier
                                .padding(end = 5.dp, top = 3.73.dp)
                                .background(Color.Black, shape = CircleShape)
                                .clickable { onRemoveImage() }
                        )
                    }
                }
            }
        }
    }
}
