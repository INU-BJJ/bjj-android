package inu.appcenter.bjj_android.ui.menudetail.review

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import inu.appcenter.bjj_android.R


@Composable
fun DynamicReviewImages(reviewImages: List<String>) {
    when (reviewImages.size) {
        1 -> SingleImage(reviewImages.first())
        2 -> DoubleImages(reviewImages)
        else -> MultipleImages(reviewImages)
    }
}

@Composable
fun SingleImage(imageRes: String) {
    Image(
        painter = painterResource(R.drawable.example_menu_big_1),
        contentDescription = "단일 리뷰 이미지",
        modifier = Modifier
            .height(250.dp)
            .width(301.dp)
            .clip(RoundedCornerShape(10.dp)),
        contentScale = ContentScale.Crop
    )
}


@Composable
fun DoubleImages(images: List<String>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        userScrollEnabled = false
    ) {
        itemsIndexed(images) {index, imageRes ->
            ReviewImage(
                imageRes = R.drawable.example_menu_big_1,
                isFirst = index == 0,
                isLast = index == images.size - 1,
                width = 149.5.dp
            )
        }
    }
}

@Composable
fun MultipleImages(images: List<String>) {

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        itemsIndexed(images) { index, imageRes ->
            ReviewImage(
                imageRes = R.drawable.example_menu_big_1,
                isFirst = index == 0,
                isLast = index == images.size - 1,
                width = 140.7.dp
            )
        }
    }
}
@Composable
fun ReviewImage(
    imageRes: Int,
    isFirst: Boolean,
    isLast: Boolean,
    width: Dp,
) {
    Log.d("isFirst", isFirst.toString())
    Log.d("isLast", isLast.toString())

    // 먼저 이미지를 전체적으로 사각형으로 설정
    Box(
        modifier = Modifier
            .height(250.dp)
            .width(width)
            .clip( RoundedCornerShape(
                topStart = if (isFirst) 10.dp else 0.dp,    // 첫 번째 이미지의 왼쪽 상단
                bottomStart = if (isFirst) 10.dp else 0.dp, // 첫 번째 이미지의 왼쪽 하단
                topEnd = if (isLast) 10.dp else 0.dp,       // 마지막 이미지의 오른쪽 상단
                bottomEnd = if (isLast) 10.dp else 0.dp     // 마지막 이미지의 오른쪽 하단
            )
            )  // 모든 모서리를 0dp로 설정해서 먼저 사각형으로 처리
    ) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = "리뷰 이미지",
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}