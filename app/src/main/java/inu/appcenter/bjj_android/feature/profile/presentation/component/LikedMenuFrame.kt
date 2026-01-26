package inu.appcenter.bjj_android.feature.profile.presentation.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.shared.theme.Gray_B9B9B9
import inu.appcenter.bjj_android.shared.theme.Orange_FF7800

@Composable
fun LikedMenuFrame(
    menu: String,
    onHeartClick: () -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
            .border(0.5.dp, Gray_B9B9B9, shape = RoundedCornerShape(3.dp))
            .padding(start = 9.dp, end = 14.dp, top = 12.dp, bottom = 12.dp)
            .clickable { onMenuClick() }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MainText(text = menu)
            Icon(
                painter = painterResource(R.drawable.filled_heart),
                contentDescription = "filled heart",
                tint = Orange_FF7800,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onHeartClick() }
            )
        }
    }
}