package inu.appcenter.bjj_android.ui.menudetail.menuinfo

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import inu.appcenter.bjj_android.R


@Composable
fun HeaderImage() {
    Image(
        painter = painterResource(R.drawable.example_menu_big_1),
        contentDescription = "상세 메뉴 대표사진",
        contentScale = ContentScale.FillHeight,
        modifier = Modifier
            .fillMaxWidth()
            .height(257.dp)
    )
}