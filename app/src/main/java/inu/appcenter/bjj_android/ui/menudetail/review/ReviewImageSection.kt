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
    val groupedImages = reviewImages.groupBy { it.reviewId }
    val filledSlots = groupedImages.size // 모든 리뷰 그룹 표시

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
    ) {
        groupedImages.keys.forEach { reviewId -> // 모든 그룹 순회
            val imagesInReview = groupedImages[reviewId] ?: emptyList()
            val reversedImagesInReview = imagesInReview.reversed()

            item {
                ReviewImageItem(
                    image = reversedImagesInReview.first(),
                    onClick = {
                        navController.navigate(
                            AllDestination.MenuDetailReviewDetailPush.createRoute(
                                imageList = reversedImagesInReview.map { it.imageName }, // 같은 리뷰의 모든 이미지 포함
                                index = 0, // 시작 인덱스는 0
                                reviewId = reviewId,
                                fromReviewDetail = false
                            )
                        )
                    }
                )
            }
        }

        items((totalSlots - filledSlots).coerceAtLeast(0)) {
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