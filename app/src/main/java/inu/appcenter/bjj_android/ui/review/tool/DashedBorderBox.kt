package inu.appcenter.bjj_android.ui.review.tool

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.ui.theme.Black_1E1E1E
import inu.appcenter.bjj_android.ui.theme.Gray_B9B9B9

@Composable
fun DashedBorderBox() {

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // 갤러리에서 이미지 선택을 처리하는 런처
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // 선택된 이미지 URI 가져오기
            val uri: Uri? = result.data?.data
            if (uri != null) {
                imageUri = uri
            }
        }
    }

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
        Column(
            modifier = Modifier.padding(top = 18.dp, bottom = 13.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.clickable {
                    val intent = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                    intent.type = "image/*"
                    galleryLauncher.launch(intent)
                },
                painter = painterResource(id = R.drawable.camera),
                contentDescription = "뒤로 가기",
                tint = Black_1E1E1E
            )
            Spacer(modifier = Modifier.height(5.dp))

            PhotoCountingCalculator(0)
        }
    }
    imageUri?.let {
        // 이미지 URI가 있을 경우에 대한 추가 처리
    }
}