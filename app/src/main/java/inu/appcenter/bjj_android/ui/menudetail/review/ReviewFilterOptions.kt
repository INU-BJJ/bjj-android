package inu.appcenter.bjj_android.ui.menudetail.review

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
import androidx.compose.ui.unit.dp
import inu.appcenter.bjj_android.ui.main.LocalTypography
import inu.appcenter.bjj_android.ui.menudetail.common.CheckBox


@Composable
fun ReviewFilterOptions(onlyPhoto: Boolean, onOnlyPhotoChanged: (Boolean) -> Unit, modifier: Modifier = Modifier) {
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
                style = LocalTypography.current.menuDetail_onlyPhotoReview
            )
        }
        SortDropdownMenu()
    }
}
