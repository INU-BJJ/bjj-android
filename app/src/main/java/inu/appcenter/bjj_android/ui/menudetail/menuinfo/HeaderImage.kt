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


@Composable
fun HeaderImage(
    menu: TodayDietRes
) {
    if (menu.reviewImageName != null){
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://bjj.inuappcenter.kr/images/review/${menu.reviewImageName}")
                .memoryCacheKey(menu.reviewImageName)
                .diskCacheKey(menu.reviewImageName)
                .crossfade(true)
                .listener(
                    onError = { _, result ->
                        Log.e("ImageLoading", "Error loading image: ${result.throwable.message}", result.throwable)
                    },
                    onSuccess = { _, _ ->
                        Log.d("ImageLoading", "Successfully loaded image")
                    }
                )
                .build(),
            placeholder = painterResource(R.drawable.big_placeholder),
            contentDescription = "상세 메뉴 대표사진",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(257.dp)
        )
    } else {
        Image(
            painter = painterResource(R.drawable.big_placeholder),
            contentDescription = "상세 메뉴 대표사진 대체 이미지",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(257.dp)
        )
    }
}