package inu.appcenter.bjj_android.ui.mypage.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import inu.appcenter.bjj_android.utils.ImageLoader

@Composable
fun CharacterView(
    modifier: Modifier = Modifier,
    imageName: String?
){
    if (imageName.isNullOrBlank()){
        return
    } else {
        ImageLoader.CharacterItem(
            imageName = imageName,
            modifier = modifier,
            contentScale = ContentScale.Fit
        )
    }
}