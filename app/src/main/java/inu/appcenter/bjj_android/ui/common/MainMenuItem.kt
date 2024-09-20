package inu.appcenter.bjj_android.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.ui.main.LocalTypography
import inu.appcenter.bjj_android.ui.main.MainMenu
import inu.appcenter.bjj_android.ui.theme.Background
import inu.appcenter.bjj_android.ui.theme.Orange_FFF4DF

@Composable
fun MainMenuItem(
    menu: MainMenu,
    clickMenuDetail: () -> Unit
){


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
            .height(80.dp)
            .background(color = Color.White, shape = RoundedCornerShape(3.dp))
            .clickable {
                clickMenuDetail()
            },
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            painter = painterResource(menu.menuImage),
            contentDescription = "메뉴 이미지",
            modifier = Modifier
                .padding(start = 8.dp, top = 6.4.dp, bottom = 6.4.dp)
                .fillMaxHeight()
                .width(89.dp)
        )
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
                        text = menu.menu,
                        style = LocalTypography.current.main_menuName,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${menu.price}원",
                        style = LocalTypography.current.main_menuPrice,
                        color = Color.Black
                    )
                }
                Icon(
                    painter = painterResource(id = if (menu.isLiked) R.drawable.filled_heart else R.drawable.unfilled_heart),
                    contentDescription = "좋아요 유무",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .width(15.dp)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 13.dp),
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
                        text = menu.reviewStar.toString(),
                        style = LocalTypography.current.main_menuReviewStar,
                        color = Color.Black
                    )
                }
                Text(
                    text = menu.menuRestaurant,
                    style = LocalTypography.current.main_menuRestaurant,
                    color = Color.Black.copy(alpha = 0.5f)
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}