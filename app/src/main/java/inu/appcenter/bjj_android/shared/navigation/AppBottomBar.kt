package inu.appcenter.bjj_android.shared.navigation

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.shared.theme.Gray_B9B9B9
import inu.appcenter.bjj_android.shared.theme.Orange_FF7800


@Composable
fun AppBottomBar(
    navController: NavHostController
) {
    val screens = listOf(AllDestination.Main, AllDestination.Ranking, AllDestination.Review, AllDestination.MyPage)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route

    // MyPage 화면인지 확인
    val isMyPageScreen = currentRoute == AllDestination.MyPage.route

    NavigationBar(
        containerColor = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .height(71.dp)
            .then(
                // 조건부 그림자 적용 - MyPageScreen이 아닐 때만 그림자 적용
                if (!isMyPageScreen) {
                    Modifier.shadowCustom(
                        color = Color(0xff190000).copy(alpha = 0.1f),
                        offsetX = (-3).dp,
                        offsetY = 0.dp,
                        blurRadius = 10.dp,
                    )
                } else {
                    Modifier // MyPageScreen일 때는 그림자 없음
                }
            )
    ) {
        screens.forEach { screen ->
            NavigationBarItem(
                icon = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            painter = painterResource(screen.icon),
                            contentDescription = screen.route,
                            modifier = Modifier.size(21.dp)
                        )
                        Text(
                            text = screen.label,
                            style = LocalTypography.current.medium10
                        )
                    }
                },
                selected = currentDestination?.route == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Orange_FF7800,
                    selectedTextColor = Orange_FF7800,
                    unselectedIconColor = Gray_B9B9B9,
                    unselectedTextColor = Gray_B9B9B9,
                    indicatorColor = Color.Transparent
                ),
                interactionSource = remember { MutableInteractionSource() },
                label = {},
            )
        }
    }
}

fun Modifier.shadowCustom(
    color: Color = Color.Black,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    blurRadius: Dp = 0.dp,
    shapeRadius: Dp = 0.dp,
) = composed {
    val paint: Paint = remember { Paint() }
    val blurRadiusPx = blurRadius.px(LocalDensity.current)
    val maskFilter = remember {
        BlurMaskFilter(blurRadiusPx, BlurMaskFilter.Blur.NORMAL)
    }
    drawBehind {
        drawIntoCanvas { canvas ->
            val frameworkPaint = paint.asFrameworkPaint()
            if (blurRadius != 0.dp) {
                frameworkPaint.maskFilter = maskFilter
            }
            frameworkPaint.color = color.toArgb()

            val leftPixel = offsetX.toPx()
            val topPixel = offsetY.toPx()
            val rightPixel = size.width + leftPixel
            val bottomPixel = size.height + topPixel

            if (shapeRadius > 0.dp) {
                val radiusPx = shapeRadius.toPx()
                canvas.drawRoundRect(
                    left = leftPixel,
                    top = topPixel,
                    right = rightPixel,
                    bottom = bottomPixel,
                    radiusX = radiusPx,
                    radiusY = radiusPx,
                    paint = paint,
                )
            } else {
                canvas.drawRect(
                    left = leftPixel,
                    top = topPixel,
                    right = rightPixel,
                    bottom = bottomPixel,
                    paint = paint,
                )
            }
        }
    }
}

private fun Dp.px(density: Density): Float =
    with(density) { toPx() }