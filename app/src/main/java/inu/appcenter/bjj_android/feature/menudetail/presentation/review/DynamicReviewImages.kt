package inu.appcenter.bjj_android.feature.menudetail.presentation.review


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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import inu.appcenter.bjj_android.core.util.ImageLoader

@Composable
fun DynamicReviewImages(reviewImages: List<String>, onClick: (List<String>, Int) -> Unit) {
    when (reviewImages.size) {
        1 -> SingleImage(reviewImages.first(), onClick = { onClick(reviewImages, 0) })
        2 -> DoubleImages(reviewImages, onClick = onClick)
        else -> MultipleImages(reviewImages, onClick = onClick)
    }
}

@Composable
fun SingleImage(imageRes: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .height(250.dp)
            .width(301.dp)
    ) {
        // ImageLoader 사용 - fillMaxSize() 추가
        ImageLoader.ReviewImage(
            imageName = imageRes,
            modifier = Modifier.fillMaxSize(), // 이 부분 추가
            shape = RoundedCornerShape(10.dp),
            clickable = true,
            onClick = onClick
        )
    }
}

@Composable
fun DoubleImages(images: List<String>, onClick: (List<String>, Int) -> Unit) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        userScrollEnabled = false
    ) {
        itemsIndexed(images) { index, imageRes ->
            ReviewImage(
                imageRes = imageRes,
                isFirst = index == 0,
                isLast = index == images.size - 1,
                width = 149.5.dp,
                onClick = { onClick(images, index) }
            )
        }
    }
}

@Composable
fun MultipleImages(images: List<String>, onClick: (List<String>, Int) -> Unit) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        itemsIndexed(images) { index, imageRes ->
            ReviewImage(
                imageRes = imageRes,
                isFirst = index == 0,
                isLast = index == images.size - 1,
                width = 140.7.dp,
                onClick = { onClick(images, index) }
            )
        }
    }
}


@Composable
fun ReviewImage(
    imageRes: String,
    isFirst: Boolean,
    isLast: Boolean,
    width: Dp,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(250.dp)
            .width(width)
    ) {
        // 모서리 처리를 위한 Shape 정의
        val shape = RoundedCornerShape(
            topStart = if (isFirst) 10.dp else 0.dp,
            bottomStart = if (isFirst) 10.dp else 0.dp,
            topEnd = if (isLast) 10.dp else 0.dp,
            bottomEnd = if (isLast) 10.dp else 0.dp
        )

        // ImageLoader 사용 - fillMaxSize() 추가하여 Box를 완전히 채우도록 함
        ImageLoader.ReviewImage(
            imageName = imageRes,
            modifier = Modifier.fillMaxSize(), // 이 부분 추가
            shape = shape,
            clickable = true,
            onClick = onClick
        )
    }
}