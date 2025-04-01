package inu.appcenter.bjj_android.ui.main.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.model.todaydiet.TodayDietRes
import inu.appcenter.bjj_android.ui.main.MainViewModel
import inu.appcenter.bjj_android.ui.navigate.shadowCustom
import inu.appcenter.bjj_android.ui.theme.Orange_FFF4DF
import inu.appcenter.bjj_android.utils.ImageLoader
import kotlin.math.round

@Composable
fun MainMenuItem(
    menu: TodayDietRes,
    clickMenuDetail: () -> Unit,
    mainViewModel: MainViewModel
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .shadowCustom(
                offsetX = 0.dp,
                offsetY = 1.dp,
                blurRadius = 4.dp,
                color = Color(0xFF0C0C0C).copy(alpha = 0.05f)
            )
            .fillMaxWidth()
            .background(color = Color.White, shape = RoundedCornerShape(3.dp))
            .clickable {
                clickMenuDetail()
            },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 이미지 로딩 부분을 ImageLoader로 대체
        Box(
            modifier = Modifier
                .padding(start = 8.dp, top = 6.4.dp, bottom = 6.4.dp)
                .width(89.dp)
                .height(67.dp)
        ) {
            ImageLoader.ReviewImage(
                imageName = menu.reviewImageName,
                shape = RoundedCornerShape(3.dp),
                showLoading = true
            )
        }

        Column(
            modifier = Modifier
                .padding(top = 9.dp, bottom = 9.dp, end = 11.dp)
                .fillMaxHeight()
                .weight(1f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 18.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = menu.mainMenuName,
                        style = LocalTypography.current.bold15.copy(
                            letterSpacing = 0.13.sp,
                            lineHeight = 15.sp
                        ),
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${menu.price}",
                        style = LocalTypography.current.regular13.copy(
                            letterSpacing = 0.13.sp,
                            lineHeight = 17.sp
                        ),
                        color = Color.Black
                    )
                }
                Box(
                    modifier = Modifier
                        .size(30.dp)  // Material Design의 최소 터치 타겟 사이즈
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(bounded = false),  // 물결 효과 추가
                        ) {
                            mainViewModel.toggleMenuLiked(mainMenuId = menu.mainMenuId)
                        },
                    contentAlignment = Alignment.TopEnd
                ) {
                    Icon(
                        painter = painterResource(id = if (menu.likedMenu) R.drawable.filled_heart else R.drawable.unfilled_heart),
                        contentDescription = "좋아요 유무",
                        tint = Color.Unspecified,
                        modifier = Modifier.width(15.dp)
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 13.dp, top = 7.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
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
                        text = (round(menu.reviewRatingAverage*10)/10).toString(),
                        style = LocalTypography.current.regular13.copy(
                            letterSpacing = 0.13.sp,
                            lineHeight = 17.sp
                        ),
                        color = Color.Black
                    )
                }
                Text(
                    text = "${menu.cafeteriaName} ${menu.cafeteriaCorner}",
                    style = LocalTypography.current.regular11.copy(
                        letterSpacing = 0.13.sp,
                        lineHeight = 15.sp
                    ),
                    color = Color.Black.copy(alpha = 0.5f)
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}