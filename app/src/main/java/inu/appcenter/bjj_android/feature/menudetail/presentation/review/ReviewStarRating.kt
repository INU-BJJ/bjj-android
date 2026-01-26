package inu.appcenter.bjj_android.feature.menudetail.presentation.review

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.shared.theme.Orange_FFF4DF
import kotlin.math.round


@Composable
fun ReviewStarRating(rating: Float) {
    Row(
        modifier = Modifier
            .width(53.dp)
            .height(21.dp)
            .background(color = Orange_FFF4DF, shape = RoundedCornerShape(32.dp)),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.star),
            contentDescription = "별점",
            tint = Color.Unspecified
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = (round(rating*10)/10).toString(),
            style = LocalTypography.current.regular13.copy(
                letterSpacing = 0.13.sp,
                lineHeight = 17.sp,
            ),
            color = Color.Black
        )
    }
}