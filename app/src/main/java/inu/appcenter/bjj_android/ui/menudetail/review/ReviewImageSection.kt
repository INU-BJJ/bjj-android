package inu.appcenter.bjj_android.ui.menudetail.review

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
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
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
    ) {
        // 그룹화된 이미지 중 최대 3개만 표시
        val groupedImages = reviewImages.groupBy { it.reviewId }
        val displayGroups = groupedImages.entries.take(3)

        // 이미지 표시
        displayGroups.forEach { (reviewId, images) ->
            item {
                ReviewImageItem(
                    image = images.first(),
                    onClick = {
                        navController.navigate(
                            AllDestination.MenuDetailReviewDetailPush.createRoute(
                                imageList = images.map { it.imageName },
                                index = 0,
                                reviewId = reviewId,
                                fromReviewDetail = false
                            )
                        )
                    }
                )
            }
        }

        // 빈 슬롯 채우기 (필요한 경우만)
        val emptySlots = (3 - displayGroups.size).coerceAtLeast(0)
        items(emptySlots) {
            EmptyReviewSlot()
        }

        // 마지막 위치에 더보기 버튼 추가
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