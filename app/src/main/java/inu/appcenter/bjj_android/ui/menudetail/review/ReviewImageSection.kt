package inu.appcenter.bjj_android.ui.menudetail.review

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.model.review.ReviewImageDetail
import inu.appcenter.bjj_android.model.todaydiet.TodayDietRes
import inu.appcenter.bjj_android.ui.navigate.AllDestination
import inu.appcenter.bjj_android.ui.theme.Gray_D9D9D9
import inu.appcenter.bjj_android.utils.ImageLoader

@Composable
fun ReviewImagesSection(
    menu: TodayDietRes,
    reviewImages: List<ReviewImageDetail>,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val totalSlots = 3
    val filledSlots = reviewImages.size.coerceAtMost(totalSlots)
    val emptySlots = (totalSlots - filledSlots).coerceAtLeast(0)

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
    ) {
        items(filledSlots) { index ->
            ReviewImageItem(
                image = reviewImages[index],
                onClick = {
                    navController.navigate(
                        AllDestination.MenuDetailReviewDetailPush.createRoute(
                            imageList = listOf(reviewImages[index].imageName),
                            index = 0,
                            reviewId = reviewImages[index].reviewId,
                            fromReviewDetail = false
                        )
                    )
                }
            )
        }

        items(emptySlots) {
            EmptyReviewSlot()
        }

        item {
            MoreImagesButton(
                menu = menu,
                navController = navController
            )
        }
    }
}

@Composable
fun ReviewImageItem(image: ReviewImageDetail, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(68.dp)
    ) {
        ImageLoader.ReviewImage(
            imageName = image.imageName,
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(10.dp),
            clickable = true,
            onClick = onClick
        )
    }
}

@Composable
fun EmptyReviewSlot() {
    Box(
        modifier = Modifier
            .size(68.dp)
            .background(
                Gray_D9D9D9,
                RoundedCornerShape(10.dp)
            )
    )
}

@Composable
fun MoreImagesButton(
    menu: TodayDietRes,
    navController: NavHostController
) {
    Box(
        modifier = Modifier
            .size(68.dp)
            .background(
                Gray_D9D9D9,
                RoundedCornerShape(10.dp)
            )
            .clickable {
                navController.navigate(
                    AllDestination.MoreImage.createRoute(menuPairId = menu.menuPairId)
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "+",
            style = LocalTypography.current.semibold18,
            color = Color.Black
        )
    }
}