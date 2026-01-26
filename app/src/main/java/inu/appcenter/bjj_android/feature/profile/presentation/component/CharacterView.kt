package inu.appcenter.bjj_android.feature.profile.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import inu.appcenter.bjj_android.core.util.CharacterImageType
import inu.appcenter.bjj_android.core.util.ImageLoader

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
            type = CharacterImageType.MAIN,
            contentScale = ContentScale.Fit
        )
    }
}