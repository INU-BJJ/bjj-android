package inu.appcenter.bjj_android.ui.menudetail.review

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.model.todaydiet.TodayDietRes


@Composable
fun ReviewHeaderInfo(
    menu: TodayDietRes,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = "리뷰",
            style = LocalTypography.current.semibold18.copy(
                letterSpacing = 0.13.sp,
                lineHeight = 17.sp
            ),
            color = Color.Black
        )
        Spacer(Modifier.width(3.dp))
        Text(
            text = "(${menu.reviewCount})",
            style = LocalTypography.current.regular13.copy(
                letterSpacing = 0.13.sp,
                lineHeight = 17.sp,
                color = Color(0xFF999999)
            )
        )
        Spacer(Modifier.width(16.dp))
        ReviewStarRating(menu.reviewRatingAverage)
    }
}