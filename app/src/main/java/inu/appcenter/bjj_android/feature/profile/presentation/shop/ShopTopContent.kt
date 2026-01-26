package inu.appcenter.bjj_android.feature.profile.presentation.shop

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.shared.theme.Orange_F15A29
import inu.appcenter.bjj_android.shared.theme.Orange_FF7800

@Composable
fun ShopTopContent(
    modifier: Modifier = Modifier,
    point: Long,
    popBackStack: () -> Unit,
    shopInfoDialog: () -> Unit
) {
    Row(
        modifier = modifier
            .statusBarsPadding(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.arrowback),
                contentDescription = stringResource(R.string.back_description),
                tint = Color.Black,
                modifier = Modifier
                    .height(18.dp)
                    .clickable {
                        popBackStack()
                    }
            )
            Spacer(Modifier.width(18.dp))
            Icon(
                painter = painterResource(R.drawable.info),
                contentDescription = "뽑기 정보",
                tint = Orange_FF7800,
                modifier = Modifier
                    .size(25.dp)
                    .clickable {
                        shopInfoDialog()
                    }
            )
        }
        Box() {
            Image(
                painter = painterResource(R.drawable.point),
                contentDescription = stringResource(R.string.point),
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .zIndex(1f)
                    .offset(x = (-7.38).dp),
            )
            Row(
                modifier = Modifier
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .border(
                        width = 1.dp,
                        shape = RoundedCornerShape(20.dp),
                        color = Orange_F15A29
                    )
                    .padding(
                        start = 33.dp,
                        end = 12.dp,
                        top = 5.dp,
                        bottom = 5.dp
                    )
                    .align(Alignment.CenterEnd)

            ) {
                Text(
                    text = "$point p",
                    style = LocalTypography.current.semibold15.copy(
                        fontSize = 12.sp,
                        letterSpacing = 0.13.sp,
                        color = Color.Black,
                        lineHeight = 18.sp
                    )
                )
            }
        }
    }
}