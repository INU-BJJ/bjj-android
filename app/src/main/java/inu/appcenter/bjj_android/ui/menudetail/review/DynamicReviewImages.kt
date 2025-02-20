package inu.appcenter.bjj_android.ui.menudetail.review

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Scale
import inu.appcenter.bjj_android.R


@Composable
fun DynamicReviewImages(reviewImages: List<String>, onClick: (List<String>, Int) -> Unit) {
    when (reviewImages.size) {
        1 -> SingleImage(reviewImages.first(), onClick = {onClick(reviewImages, 0)})
        2 -> DoubleImages(reviewImages, onClick = onClick)
        else -> MultipleImages(reviewImages, onClick = onClick)
    }
}

@Composable
fun SingleImage(imageRes: String, onClick: () -> Unit) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data("https://bjj.inuappcenter.kr/images/review/${imageRes}")
            .size(500)
            .scale(Scale.FILL)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCacheKey(imageRes)
            .diskCacheKey(imageRes)
            .crossfade(true)
            .listener(
                onError = { _, result ->
                    Log.e("ImageLoading", "Error loading image: ${result.throwable.message}", result.throwable)
                },
                onSuccess = { _, _ ->
                    Log.d("ImageLoading", "Successfully loaded image")
                }
            )
            .build(),
        contentDescription = "메뉴 상세 리뷰 이미지",
        modifier = Modifier
            .height(250.dp)
            .width(301.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() },
        contentScale = ContentScale.Crop
    )
}


@Composable
fun DoubleImages(images: List<String>,onClick: (List<String>, Int) -> Unit) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        userScrollEnabled = false
    ) {
        itemsIndexed(images) {index, imageRes ->
            ReviewImage(
                imageRes = imageRes,
                isFirst = index == 0,
                isLast = index == images.size - 1,
                width = 149.5.dp,
                onClick = {onClick(images,index)}
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
                onClick = {onClick(images, index)}
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
    val context = LocalContext.current

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
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://bjj.inuappcenter.kr/images/review/${imageRes}")
                .size(500)
                .scale(Scale.FILL)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCacheKey(imageRes)
                .diskCacheKey(imageRes)
                .crossfade(true)
                .listener(
                    onError = { _, result ->
                        Log.e("ImageLoading", "Error loading image: ${result.throwable.message}", result.throwable)
                    },
                    onSuccess = { _, _ ->
                        Log.d("ImageLoading", "Successfully loaded image")
                    }
                )
                .build(),
            contentDescription = "메뉴 상세 리뷰 이미지",
            modifier = Modifier
                .fillMaxSize()
                .clickable { onClick() },
            contentScale = ContentScale.Crop
        )
    }
}