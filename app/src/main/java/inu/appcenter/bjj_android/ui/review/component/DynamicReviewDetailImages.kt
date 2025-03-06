package inu.appcenter.bjj_android.ui.review.component

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

/**
 * 서버에서 받아온 이미지 이름 리스트 [imageNames]를 기반으로
 * - 1장 -> Single
 * - 2장 -> Double
 * - 3장 이상 -> Multiple
 * 형태로 동적으로 이미지를 표시하는 Composable
 *
 * @param imageNames 서버 이미지 이름 리스트
 * @param onImageClick 이미지 클릭 시 호출되는 콜백 (index: Int)
 */
@Composable
fun DynamicReviewDetailImages(
    imageNames: List<String>,
    onImageClick: (index: Int) -> Unit
) {
    when (imageNames.size) {
        1 -> {
            SingleImage(
                imageName = imageNames.first(),
                onImageClick = onImageClick
            )
        }
        2 -> {
            DoubleImages(
                imageNames = imageNames,
                onImageClick = onImageClick
            )
        }
        else -> {
            MultipleImages(
                imageNames = imageNames,
                onImageClick = onImageClick
            )
        }
    }
}

/**
 * 1장만 있을 때
 */
@Composable
fun SingleImage(
    imageName: String,
    onImageClick: (index: Int) -> Unit
) {
    // index = 0으로 고정
    AsyncImage(
        model = "https://bjj.inuappcenter.kr/images/review/$imageName",
        contentDescription = "싱글 리뷰 이미지",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .height(250.dp)
            .width(301.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable { onImageClick(0) }
    )
}

/**
 * 2장일 때
 */
@Composable
fun DoubleImages(
    imageNames: List<String>,
    onImageClick: (index: Int) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        userScrollEnabled = false // 2장만 표시하므로 스크롤 비활성(원하는 경우 true)
    ) {
        itemsIndexed(imageNames) { index, name ->
            ReviewDetailImage(
                imageUrl = "https://bjj.inuappcenter.kr/images/review/$name",
                width = 149.5.dp,
                isFirst = index == 0,
                isLast = index == imageNames.size - 1,
                onImageClick = { onImageClick(index) }
            )
        }
    }
}

/**
 * 3장 이상일 때
 */
@Composable
fun MultipleImages(
    imageNames: List<String>,
    onImageClick: (index: Int) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        itemsIndexed(imageNames) { index, name ->
            ReviewDetailImage(
                imageUrl = "https://bjj.inuappcenter.kr/images/review/$name",
                width = 140.7.dp,
                isFirst = index == 0,
                isLast = index == imageNames.size - 1,
                onImageClick = { onImageClick(index) }
            )
        }
    }
}

/**
 * 실제 이미지를 그리는 Composable
 * - 모서리 둥글게 처리 (첫/마지막만)
 * - 클릭 이벤트
 */
@Composable
fun ReviewDetailImage(
    imageUrl: String,
    width: Dp,
    isFirst: Boolean,
    isLast: Boolean,
    onImageClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(250.dp)
            .width(width)
            .clip(
                RoundedCornerShape(
                    topStart = if (isFirst) 10.dp else 0.dp,
                    bottomStart = if (isFirst) 10.dp else 0.dp,
                    topEnd = if (isLast) 10.dp else 0.dp,
                    bottomEnd = if (isLast) 10.dp else 0.dp
                )
            )
            .clickable { onImageClick() }
    ) {
        // Coil AsyncImage로 서버 이미지 로드
        AsyncImage(
            model = imageUrl,
            contentDescription = "리뷰 이미지",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}
