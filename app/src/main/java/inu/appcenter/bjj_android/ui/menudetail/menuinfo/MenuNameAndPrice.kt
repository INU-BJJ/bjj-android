package inu.appcenter.bjj_android.ui.menudetail.menuinfo

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.model.todaydiet.TodayDietRes
import inu.appcenter.bjj_android.ui.menudetail.MenuDetailViewModel


@Composable
fun MenuNameAndPrice(menu: TodayDietRes, menuDetailViewModel: MenuDetailViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 39.dp,
                start = 29.5.dp,
                end = 29.5.dp
            )
    ) {
        Text(
            text = menu.mainMenuName,
            style = LocalTypography.current.semibold24.copy(letterSpacing = 0.13.sp),
            color = Color.Black
        )
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${menu.price}",
                style = LocalTypography.current.regular20.copy(
                    letterSpacing = 0.13.sp,
                ),
                color = Color.Black
            )
            Icon(
                modifier = Modifier
                    .size(25.dp)
                    .clickable { menuDetailViewModel.toggleMenuLiked(menu.mainMenuId) },
                painter = painterResource(if (menu.likedMenu) R.drawable.filled_heart else R.drawable.unfilled_heart),
                contentDescription = "상세 메뉴 좋아요",
                tint = Color.Unspecified,
            )
        }
    }
}

