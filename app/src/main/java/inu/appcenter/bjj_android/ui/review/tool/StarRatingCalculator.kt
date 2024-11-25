package inu.appcenter.bjj_android.ui.review.tool

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.ui.theme.Gray_B9B9B9
import inu.appcenter.bjj_android.ui.theme.Orange_FF7800
import kotlin.math.roundToInt

@Composable
fun StarRatingCalculator(rating: Float, modifier: Modifier = Modifier) {
    val roundedRating = rating.roundToInt() // 가장 가까운 정수로 반올림
    val fullStars = roundedRating
    val emptyStars = 5 - fullStars

    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        repeat(fullStars) {
            Icon(
                painter = painterResource(R.drawable.star),
                contentDescription = "Full Star",
                tint = Orange_FF7800,
                modifier = Modifier.size(12.dp)
            )
        }
        repeat(emptyStars) {
            Icon(
                painter = painterResource(R.drawable.star),
                contentDescription = "Empty Star",
                tint = Gray_B9B9B9,
                modifier = Modifier.size(12.dp)
            )
        }
    }
}