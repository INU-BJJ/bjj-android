package inu.appcenter.bjj_android.ui.menudetail.review

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import inu.appcenter.bjj_android.ui.main.MainMenu
import inu.appcenter.bjj_android.ui.menudetail.common.GrayHorizontalDivider


@Composable
fun ReviewHeaderSection(menu: MainMenu, onlyPhoto: Boolean, onOnlyPhotoChanged: (Boolean) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ReviewHeaderInfo(menu, modifier = Modifier.padding(horizontal = 29.5.dp))
        Spacer(Modifier.height(16.dp))
        ReviewImagesSection(reviewImages = menu.reviewImages, modifier = Modifier.padding(horizontal = 29.5.dp))
        Spacer(Modifier.height(16.8.dp))
        GrayHorizontalDivider(modifier = Modifier.padding(horizontal = 21.3.dp))
        ReviewFilterOptions(onlyPhoto, onOnlyPhotoChanged, modifier = Modifier.padding(horizontal = 29.5.dp))
        GrayHorizontalDivider(modifier = Modifier.padding(horizontal = 21.3.dp))
        Spacer(Modifier.height(20.dp))
    }
}