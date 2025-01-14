package inu.appcenter.bjj_android.ui.menudetail.review

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import inu.appcenter.bjj_android.model.review.ReviewImageDetail
import inu.appcenter.bjj_android.model.todaydiet.TodayDietRes
import inu.appcenter.bjj_android.ui.menudetail.SortingRules
import inu.appcenter.bjj_android.ui.menudetail.common.GrayHorizontalDivider


@Composable
fun ReviewHeaderSection(
    menu: TodayDietRes,
    onlyPhoto: Boolean,
    reviewImages: List<ReviewImageDetail>,
    onOnlyPhotoChanged: (Boolean) -> Unit,
    selectedSortingRule: SortingRules,
    onSortingRUleChanged: (SortingRules) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ReviewHeaderInfo(menu, modifier = Modifier.padding(horizontal = 29.5.dp))
        Spacer(Modifier.height(16.dp))
        ReviewImagesSection(
            reviewImages = reviewImages,
            modifier = Modifier.padding(horizontal = 29.5.dp)
        )
        Spacer(Modifier.height(16.8.dp))
        GrayHorizontalDivider(modifier = Modifier.padding(horizontal = 21.3.dp))
        ReviewFilterOptions(
            modifier = Modifier.padding(horizontal = 29.5.dp),
            onlyPhoto = onlyPhoto,
            onOnlyPhotoChanged = onOnlyPhotoChanged,
            selectedSortingRule = selectedSortingRule,
            onSortingRuleChanged = onSortingRUleChanged,
        )
        GrayHorizontalDivider(modifier = Modifier.padding(horizontal = 21.3.dp))
        Spacer(Modifier.height(20.dp))
    }
}