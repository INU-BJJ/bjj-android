package inu.appcenter.bjj_android.ui.mypage.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.utils.ImageLoader

@Composable
fun MyPageBackground(
    modifier: Modifier = Modifier,
    backgroundImageName: String?
){
    if (backgroundImageName.isNullOrBlank()){
        Image(
            painter = painterResource(R.drawable.default_background),
            contentDescription = stringResource(R.string.defaultBackground),
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    } else {
        ImageLoader.BackgroundItem(
            imageName = backgroundImageName,
            showLoading = false,
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    }
}