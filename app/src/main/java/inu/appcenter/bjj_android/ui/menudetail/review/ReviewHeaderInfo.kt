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
import inu.appcenter.bjj_android.ui.main.LocalTypography
import inu.appcenter.bjj_android.ui.main.MainMenu


@Composable
fun ReviewHeaderInfo(
    menu: MainMenu,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = "리뷰",
            style = LocalTypography.current.menuDetail_review,
            color = Color.Black
        )
        Spacer(Modifier.width(3.dp))
        Text(
            text = "(605)",
            style = LocalTypography.current.menuDetail_reviewCount
        )
        Spacer(Modifier.width(16.dp))
        ReviewStarRating(menu.reviewStar)
    }
}