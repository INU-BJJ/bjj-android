package inu.appcenter.bjj_android.feature.review.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.shared.theme.Gray_B9B9B9
import inu.appcenter.bjj_android.shared.theme.Orange_FF7800
import kotlin.math.roundToInt

@Composable
fun StarRatingCalculatorBig(initialRating: Float,  onRatingChanged: (Int) -> Unit) {
    var currentRating by remember { mutableIntStateOf(initialRating.roundToInt()) }


    Row(horizontalArrangement = Arrangement.spacedBy(11.5.dp)) {
        for (i in 1..5) {
            Icon(
                painter = painterResource(R.drawable.bigstar),
                contentDescription = if (i <= currentRating) "Full Star" else "Empty Star",
                tint = if (i <= currentRating) Orange_FF7800 else Gray_B9B9B9,
                modifier = Modifier
                    .size(33.dp)
                    .clickable {
                        currentRating = i // 클릭한 별의 인덱스까지 MainColor로 설정
                        onRatingChanged(i)
                    }
            )
        }
    }
}