package inu.appcenter.bjj_android.ui.menudetail.review

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import inu.appcenter.bjj_android.ui.main.LocalTypography
import inu.appcenter.bjj_android.ui.theme.Gray_D9D9D9


@Composable
fun ReviewImagesSection(
    reviewImages: List<Int>,
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
            ReviewImageItem(image = reviewImages[index])
        }
        items(emptySlots) {
            EmptyReviewSlot()
        }
        item {
            MoreImagesButton()
        }
    }
}

@Composable
fun ReviewImageItem(image: Int) {
    Image(
        painter = painterResource(image),
        contentDescription = "리뷰 이미지",
        modifier = Modifier
            .size(68.dp)
            .clip(RoundedCornerShape(10.dp)),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun EmptyReviewSlot() {
    Box(
        modifier = Modifier
            .size(68.dp)
            .background(Gray_D9D9D9, RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
    }
}

@Composable
fun MoreImagesButton() {
    Box(
        modifier = Modifier
            .size(68.dp)
            .background(Gray_D9D9D9, RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "+",
            style = LocalTypography.current.menuDetail_reviewMoreImages,
            color = Color.Black
        )
    }
}