package inu.appcenter.bjj_android.ui.mypage.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.utils.ImageLoader

@Composable
fun MyPageBackground(
    modifier: Modifier = Modifier,
    backgroundImageName: String?
){
    // Box를 사용하여 이미지가 배경 전체를 커버하도록 함
    Box(modifier = modifier) {
        if (backgroundImageName.isNullOrBlank()){
            Image(
                painter = painterResource(R.drawable.default_background),
                contentDescription = stringResource(R.string.defaultBackground),
                modifier = Modifier.fillMaxSize(), // fillMaxSize로 Box 전체를 채우도록 함
                contentScale = ContentScale.Crop
            )
        } else {
            ImageLoader.BackgroundItem(
                imageName = backgroundImageName,
                showLoading = false,
                modifier = Modifier.fillMaxSize(), // fillMaxSize로 Box 전체를 채우도록 함
                contentScale = ContentScale.Crop  // Crop으로 변경하여 비율 유지하면서 영역 채우기
            )
        }
    }
}