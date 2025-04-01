package inu.appcenter.bjj_android.ui.menudetail.menuinfo

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import inu.appcenter.bjj_android.model.todaydiet.TodayDietRes
import inu.appcenter.bjj_android.utils.ImageLoader

@Composable
fun HeaderImage(
    menu: TodayDietRes
) {
    // ImageLoader를 사용하여 이미지 로딩 최적화
    ImageLoader.ReviewImage(
        imageName = menu.reviewImageName,
        modifier = Modifier
            .fillMaxWidth()
            .height(257.dp),
        contentScale = ContentScale.Crop,
        showLoading = true,
        isHeaderImage = true
    )
}