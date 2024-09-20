package inu.appcenter.bjj_android.ui.menudetail

import android.graphics.BlurMaskFilter
import android.view.Menu
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.ui.common.GrayHorizontalDivider
import inu.appcenter.bjj_android.ui.common.ReviewImageItem
import inu.appcenter.bjj_android.ui.common.ReviewImagesSection
import inu.appcenter.bjj_android.ui.common.SortDropdownMenu
import inu.appcenter.bjj_android.ui.common.shadowCustom
import inu.appcenter.bjj_android.ui.main.LocalTypography
import inu.appcenter.bjj_android.ui.main.MainMenu
import inu.appcenter.bjj_android.ui.theme.Background
import inu.appcenter.bjj_android.ui.theme.Gray_999999
import inu.appcenter.bjj_android.ui.theme.Gray_A9A9A9
import inu.appcenter.bjj_android.ui.theme.Orange_FF7800
import inu.appcenter.bjj_android.ui.theme.Orange_FFF4DF

@Composable
fun MenuDetailScreen(
    menu: MainMenu,
    navController: NavHostController
) {
    var onlyPhoto by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(257.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.example_menu_big_1),
                    contentDescription = "상세 메뉴 대표사진",
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier.fillMaxSize()
                )
                Row(
                    modifier = Modifier
                        .padding(vertical = 48.dp, horizontal = 20.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        painter = painterResource(R.drawable.arrowback),
                        contentDescription = "뒤로가기",
                        modifier = Modifier.clickable { navController.popBackStack() }
                    )
                    Icon(
                        painter = painterResource(R.drawable.x),
                        contentDescription = "뒤로가기_x",
                        modifier = Modifier.clickable { navController.popBackStack() }
                    )
                }
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-38).dp)
                    .background(
                        color = Background,
                        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 39.dp, start = 29.5.dp, end = 29.5.dp)
                ) {
                    Text(
                        text = menu.menu,
                        style = LocalTypography.current.menuDetail_menuName,
                        color = Color.Black
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${menu.price}원",
                            style = LocalTypography.current.menuDetail_menuPrice,
                            color = Color.Black
                        )
                        PainterIconWithDirectionalShadow(
                            painter = painterResource(R.drawable.filled_heart),
                            modifier = Modifier.size(25.dp),
                            contentDescription = "상세 메뉴 좋아요",
                            tint = Color.Unspecified,
                            shadowOffset = Pair(4f, 4f),  // 오른쪽 아래로 4픽셀 이동
                            shadowRadius = 20f  // 그림자의 흐림 정도
                        )
//                        Icon(
//                            painter = painterResource(R.drawable.filled_heart),
//                            modifier = Modifier.size(25.dp),
//                            contentDescription = "상세 메뉴 좋아요",
//                            tint = Color.Unspecified
//                        )
                    }
                }
                Spacer(Modifier.height(22.8.dp))
                GrayHorizontalDivider()
                Spacer(Modifier.height(29.8.dp))
                //메뉴 구성
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 29.5.dp, end = 29.5.dp),
                ) {
                    Text(
                        text = "메뉴 구성",
                        style = LocalTypography.current.menuDetail_menuStructure,
                        color = Color.Black
                    )
                    Spacer(Modifier.height(22.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Orange_FFF4DF, shape = RoundedCornerShape(10.dp))
                            .padding(vertical = 10.dp, horizontal = 15.dp)
                    ) {
                        menu.menuStructure.forEach { menuDetail ->
                            Text(
                                text = menuDetail,
                                style = LocalTypography.current.menuDetail_menuStructureDetail,
                                color = Color.Black,
                            )
                        }
                    }
                    Spacer(Modifier.height(19.8.dp))
                }
                GrayHorizontalDivider()
                Spacer(Modifier.height(19.8.dp))
                //리뷰
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 29.5.dp, end = 29.5.dp),
                    verticalAlignment = Alignment.CenterVertically,
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
                            style = LocalTypography.current.menuDetail_reviewStar,
                            color = Color.Black
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
                ReviewImagesSection(reviewImages = menu.reviewImages)
                Spacer(Modifier.height(16.8.dp))
                GrayHorizontalDivider()
                Row(
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 8.dp, end = 36.8.dp, start = 29.3.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = onlyPhoto,
                            onCheckedChange = { onlyPhoto = !onlyPhoto },
                            modifier = Modifier.size(15.dp),
                            colors = CheckboxDefaults.colors(
                                checkedColor = Orange_FF7800,
                                checkmarkColor = Color.White,
                                uncheckedColor = Gray_999999,
                                disabledCheckedColor = Gray_999999,
                                disabledUncheckedColor = Gray_999999,
                                disabledIndeterminateColor = Gray_999999
                            )
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(text = "포토 리뷰만", style = LocalTypography.current.menuDetail_onlyPhotoReview)
                    }
                    SortDropdownMenu()
                }
                GrayHorizontalDivider()
            }
        }
    }
}


@Composable
fun PainterIconWithDirectionalShadow(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified,
    shadowColor: Color = Color.Black.copy(alpha = 0.25f),
    shadowOffset: Pair<Float, Float> = Pair(4f, 4f),  // x and y offset in pixels
    shadowRadius: Float = 8f
) {
    Canvas(modifier = modifier) {
        drawIntoCanvas { canvas ->
            val paint = Paint().asFrameworkPaint()
            paint.color = shadowColor.toArgb()
            paint.maskFilter = BlurMaskFilter(shadowRadius, BlurMaskFilter.Blur.NORMAL)

            // Draw shadow
            canvas.nativeCanvas.save()
            canvas.nativeCanvas.translate(shadowOffset.first, shadowOffset.second)
            with(painter) {
                draw(size = size, colorFilter = ColorFilter.tint(shadowColor))
            }
            canvas.nativeCanvas.restore()

            // Reset paint for icon
            paint.maskFilter = null
        }

        // Draw icon
        with(painter) {
            draw(
                size = size,
                colorFilter = if (tint == Color.Unspecified) null else ColorFilter.tint(tint)
            )
        }
    }
}