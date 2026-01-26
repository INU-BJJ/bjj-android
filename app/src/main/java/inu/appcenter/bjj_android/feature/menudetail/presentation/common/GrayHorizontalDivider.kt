package inu.appcenter.bjj_android.feature.menudetail.presentation.common

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import inu.appcenter.bjj_android.shared.theme.Gray_A9A9A9

@Composable
fun GrayHorizontalDivider(
    modifier : Modifier = Modifier
){
    HorizontalDivider(
        thickness = 0.5.dp,
        color = Gray_A9A9A9,
        modifier = modifier
    )
}