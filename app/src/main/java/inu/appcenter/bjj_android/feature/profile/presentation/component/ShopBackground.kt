package inu.appcenter.bjj_android.feature.profile.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import inu.appcenter.bjj_android.R

@Composable
fun ShopBackground(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(R.drawable.shop_background),
        contentDescription = stringResource(R.string.shop_background),
        modifier = modifier,
        contentScale = ContentScale.FillWidth
    )
}