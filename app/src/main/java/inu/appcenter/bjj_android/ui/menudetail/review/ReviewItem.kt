package inu.appcenter.bjj_android.ui.menudetail.review

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.model.review.ReviewDetailRes
import inu.appcenter.bjj_android.model.todaydiet.TodayDietRes
import inu.appcenter.bjj_android.ui.menudetail.MenuDetailUiEvent
import inu.appcenter.bjj_android.ui.menudetail.MenuDetailViewModel
import inu.appcenter.bjj_android.ui.navigate.AllDestination
import inu.appcenter.bjj_android.ui.review.component.formatter
import inu.appcenter.bjj_android.ui.theme.Gray_999999
import inu.appcenter.bjj_android.ui.theme.Gray_D9D9D9
import inu.appcenter.bjj_android.ui.theme.Gray_F6F6F8
import inu.appcenter.bjj_android.ui.theme.Orange_FF7800
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ReviewItem(
    review: ReviewDetailRes,
    menu: TodayDietRes,
    menuDetailViewModel: MenuDetailViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var lastEventTime by remember { mutableStateOf(0L) }

    // 이벤트 토스트 메시지 처리
    LaunchedEffect(true) {
        menuDetailViewModel.eventFlow.collectLatest { event ->
            when (event) {
                is MenuDetailUiEvent.ShowToast -> {
                    val currentTime = System.currentTimeMillis()
                    // 토스트 중복 표시 방지 (1초 이내 중복 토스트 차단)
                    if (currentTime - lastEventTime > 1000) {
                        Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                        lastEventTime = currentTime
                    }
                }
            }
        }
    }

    Column(
        modifier = modifier
            .padding(horizontal = 29.5.dp)
            .clickable {
                // 리뷰 전체 클릭 시 리뷰 상세 화면으로 이동
                navController.navigate(
                    AllDestination.MenuDetailReviewDetail.createRoute(
                        reviewId = review.reviewId,
                        menuId = menu.menuPairId
                    )
                )
            },
    ) {
        // 프로필 및 유저 정보 섹션
        UserInfoSection(
            review = review,
            onLikeClick = {
                // 중복 클릭 방지 로직 추가
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastEventTime > 1000) {
                    menuDetailViewModel.toggleReviewLiked(reviewId = review.reviewId)
                    lastEventTime = currentTime
                }
            }
        )

        Spacer(Modifier.height(12.dp))

        // 리뷰 본문
        ExpandableText(
            text = review.comment,
            maxLines = 3,
            style = LocalTypography.current.regular13.copy(
                letterSpacing = 0.13.sp,
                lineHeight = 17.sp,
                color = Color.Black
            )
        )

        Spacer(Modifier.height(12.dp))

        // 리뷰 이미지 (이미지가 있는 경우에만 표시, 별도 클릭 처리)
        if (!review.imageNames.isNullOrEmpty()) {
            // 이미지 미리 로드하기 위한 지연 렌더링
            OptimizedReviewImages(
                reviewImages = review.imageNames,
                onClick = { imageList, index ->
                    // 이미지 클릭 시에는 이미지 상세 화면으로 이동 (기존 동작 유지)
                    navController.navigate(
                        AllDestination.MenuDetailReviewDetailPush.createRoute(
                            imageList = review.imageNames,
                            index = index,
                            reviewId = review.reviewId,
                            fromReviewDetail = false
                        )
                    )
                }
            )
            Spacer(Modifier.height(12.dp))
        }

        // 메뉴 태그
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            ReviewMenuText(
                text = review.mainMenuName,
                isSame = review.mainMenuName == menu.mainMenuName
            )
            Spacer(Modifier.width(5.dp))
            ReviewMenuText(
                text = review.subMenuName,
                isSame = review.subMenuName == menu.restMenu?.split(" ")?.firstOrNull()
            )
        }
    }
}

@Composable
private fun UserInfoSection(
    review: ReviewDetailRes,
    onLikeClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 프로필 이미지
        Box(
            modifier = Modifier
                .size(41.dp)
                .background(Gray_D9D9D9, CircleShape),
        )

        Spacer(Modifier.width(10.dp))

        // 유저 정보
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = review.memberNickname,
                style = LocalTypography.current.bold15.copy(
                    letterSpacing = 0.13.sp,
                    lineHeight = 15.sp,
                ),
                color = Color.Black
            )

            Spacer(Modifier.height(5.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                // 별점 표시
                StarRatingCalculator(rating = review.rating.toFloat())

                Spacer(Modifier.width(10.dp))

                // 날짜 표시
                Text(
                    text = review.createdDate.formatter(),
                    style = LocalTypography.current.regular13.copy(
                        letterSpacing = 0.13.sp,
                        lineHeight = 17.sp,
                        color = Gray_999999
                    )
                )
            }
        }

        // 좋아요 버튼 및 카운트
        LikeButton(
            isLiked = review.liked,
            likeCount = review.likeCount,
            onClick = onLikeClick
        )
    }
}

@Composable
private fun LikeButton(
    isLiked: Boolean,
    likeCount: Long,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .size(30.dp)
            .padding(end = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(
                if (isLiked) R.drawable.filled_good else R.drawable.unfilled_good
            ),
            contentDescription = "리뷰 좋아요",
            tint = Color.Unspecified,
            modifier = Modifier.clickable(onClick = onClick)
        )

        Text(
            text = likeCount.toString(),
            style = LocalTypography.current.regular11.copy(
                letterSpacing = 0.13.sp,
                lineHeight = 15.sp,
            ),
            color = Color.Black
        )
    }
}

@Composable
private fun OptimizedReviewImages(
    reviewImages: List<String>,
    onClick: (List<String>, Int) -> Unit
) {
    // 지연 로딩을 위한 플래그
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible = true
    }

    if (isVisible) {
        DynamicReviewImages(
            reviewImages = reviewImages,
            onClick = onClick
        )
    } else {
        // 이미지 로딩 전 플레이스홀더 표시
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(Gray_D9D9D9, RoundedCornerShape(10.dp))
        )
    }
}

@Composable
fun ExpandableText(
    text: String,
    maxLines: Int = 3,
    style: TextStyle = LocalTypography.current.regular13.copy(
        letterSpacing = 0.13.sp,
        lineHeight = 17.sp,
    )
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    var needsExpansion by rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            style = style,
            maxLines = if (isExpanded) Int.MAX_VALUE else maxLines,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { textLayoutResult ->
                needsExpansion = textLayoutResult.hasVisualOverflow
                if (textLayoutResult.lineCount > maxLines){
                    needsExpansion = true
                }
            },
            modifier = Modifier
                .weight(1f)
        )

        if (needsExpansion) {
            Text(
                text = if (isExpanded) "접기" else "더보기",
                style = style.copy(color = Gray_999999),
                modifier = Modifier.clickable { isExpanded = !isExpanded }
            )
        }
    }
}

@Composable
fun ReviewMenuText(
    text: String,
    isSame: Boolean = false
){
    Text(
        text = text,
        style = LocalTypography.current.medium11.copy(
            lineHeight = 15.sp,
            letterSpacing = 0.13.sp,
            color = Color.Black
        ),
        modifier = Modifier
            .background(
                color = if (isSame) Orange_FF7800 else Gray_F6F6F8,
                shape = RoundedCornerShape(3.dp)
            )
            .padding(horizontal = 7.dp, vertical = 5.dp)
    )
}