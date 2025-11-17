package inu.appcenter.bjj_android.ui.main.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import inu.appcenter.bjj_android.model.banner.Banner
import inu.appcenter.bjj_android.model.banner.BannerItem
import inu.appcenter.bjj_android.utils.ImageLoader

@Composable
fun MainCardNews(
    banner: BannerItem? = null,
    backgroundColor: Color = Color.Transparent,
    innerPadding: PaddingValues,
    onBannerClick: (BannerItem) -> Unit = {},
    content: @Composable (() -> Unit)? = null
) {
    val modifier = Modifier
        .fillMaxWidth()
        .height(148.dp)

    if (banner != null) {
        // 배너가 있는 경우 - 서버에서 받은 배너 이미지 표시
        ImageLoader.BannerImage(
            imageName = banner.imageName,
            modifier = modifier
                .fillMaxSize()
                .clickable { onBannerClick(banner) },
            contentScale = ContentScale.FillWidth,
            clickable = false // ImageLoader 내부 클릭 비활성화 (외부에서 처리)
        )
    } else {
        // 배너가 없는 경우 - 기존 방식 (더미 데이터용)

    }
}