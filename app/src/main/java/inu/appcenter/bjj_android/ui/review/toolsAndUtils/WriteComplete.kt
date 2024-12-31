package inu.appcenter.bjj_android.ui.review.toolsAndUtils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.model.review.ReviewPost
import inu.appcenter.bjj_android.ui.review.ReviewViewModel
import inu.appcenter.bjj_android.ui.theme.Gray_B9B9B9
import inu.appcenter.bjj_android.ui.theme.Orange_FF7800

@Composable
fun WriteComplete(
    reviewComment: String,
    currentRating: Int,
    reviewViewModel: ReviewViewModel,
    selectedImages: List<String?>, // 추가된 파라미터
    onSuccess: () -> Unit
) {
    val reviewUiState by reviewViewModel.uiState.collectAsState()

    // "작성 완료" 버튼 활성화 조건
    val isCompleteEnabled = reviewComment.isNotBlank() &&
            reviewUiState.selectedRestaurantAtReviewWrite != null &&
            reviewUiState.selectedMenu != null

    Box(
        modifier = Modifier
            .width(319.dp)
            .height(47.dp), // 아이콘 크기
        contentAlignment = Alignment.Center  // 텍스트를 중앙에 정렬
    ) {
        Icon(
            modifier = Modifier
                .clickable(enabled = isCompleteEnabled) {
                    if (isCompleteEnabled) {
                        // 리뷰 작성 완료 동작 수행
                        reviewViewModel.reviewComplete(
                            reviewPost = ReviewPost(
                                comment = reviewComment,
                                rating = currentRating,
                                menuPairId = reviewUiState.selectedMenu?.menuPairId ?: -1
                            ),
                            images = selectedImages,
                            onSuccess = onSuccess
                        )
                    }
                }
                .fillMaxSize(),
            painter = painterResource(R.drawable.writecomplete),
            contentDescription = if (isCompleteEnabled) "Complete" else "Uncomplete",
            tint = if (isCompleteEnabled) Orange_FF7800 else Gray_B9B9B9
        )
        Text(
            text = "작성 완료",
            color = Color.White,
            style = LocalTypography.current.semibold15.copy(
                letterSpacing = 0.13.sp,
                lineHeight = 18.sp,
            )
        )
    }
}

