package inu.appcenter.bjj_android.feature.menudetail.presentation.review

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.feature.menudetail.presentation.SortingRules
import inu.appcenter.bjj_android.feature.menudetail.presentation.common.CheckBox


@Composable
fun ReviewFilterOptions(
    modifier: Modifier = Modifier,
    onlyPhoto: Boolean,
    onOnlyPhotoChanged: (Boolean) -> Unit,
    selectedSortingRule: SortingRules,
    onSortingRuleChanged: (SortingRules) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            CheckBox(
                checked = onlyPhoto,
                onCheckedChange = { onOnlyPhotoChanged(it) },
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = "포토 리뷰만",
                style = LocalTypography.current.regular13.copy(
                    letterSpacing = 0.13.sp,
                    lineHeight = 17.sp,
                    color = Color(0xFF999999)
                )
            )
        }
        SortDropdownMenu(
            selectedSortingRule = selectedSortingRule,
            onSortingRuleChanged = onSortingRuleChanged
        )
    }
}
