package inu.appcenter.bjj_android.ui.menudetail.menuinfo

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import inu.appcenter.bjj_android.R
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