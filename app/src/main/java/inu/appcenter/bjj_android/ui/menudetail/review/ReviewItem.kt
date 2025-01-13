package inu.appcenter.bjj_android.ui.menudetail.review

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.model.review.ReviewDetailRes
import inu.appcenter.bjj_android.model.todaydiet.TodayDietRes
import inu.appcenter.bjj_android.ui.menudetail.MenuDetailUiEvent
import inu.appcenter.bjj_android.ui.menudetail.MenuDetailViewModel
import inu.appcenter.bjj_android.ui.review.ReviewViewModel
import inu.appcenter.bjj_android.ui.theme.Gray_999999
import inu.appcenter.bjj_android.ui.theme.Gray_D9D9D9
import kotlinx.coroutines.flow.collectLatest


@Composable
fun ReviewItem(
    review: ReviewDetailRes,
    menu: TodayDietRes,
    menuDetailViewModel: MenuDetailViewModel
) {
    val context = LocalContext.current

    LaunchedEffect(true) {
        menuDetailViewModel.eventFlow.collectLatest { event ->
            when (event) {
                is MenuDetailUiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 29.5.dp)
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (review.memberImageName != null) {
                Image(
                    painter = painterResource(R.drawable.mypage),
                    contentDescription = "리뷰 이미지",
                    modifier = Modifier
                        .size(41.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(41.dp)
                        .background(Gray_D9D9D9, CircleShape),
                )
            }
            Spacer(Modifier.width(10.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = review.memberNickname ?: "null",
                    style = LocalTypography.current.bold15.copy(
                        letterSpacing = 0.13.sp,
                        lineHeight = 15.sp,
                    ),
                    color = Color.Black
                )
                Spacer(Modifier.height(5.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StarRatingCalculator(rating = review.rating.toFloat())
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = review.createdDate,
                        style = LocalTypography.current.regular13.copy(
                            letterSpacing = 0.13.sp,
                            lineHeight = 17.sp,
                            color = Color(0xFF999999)
                        )
                    )
                }
            }
            Column(
                modifier = Modifier
                    .padding(end = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource( if (review.liked) R.drawable.filled_good else R.drawable.unfilled_good),
                    contentDescription = "리뷰 별 좋아요 수",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .clickable {
                            menuDetailViewModel.toggleReviewLiked(reviewId = review.reviewId)
                        }
                )
                Text(
                    text = review.likeCount.toString(),
                    style = LocalTypography.current.regular11.copy(
                        letterSpacing = 0.13.sp,
                        lineHeight = 15.sp,
                    ),
                    color = Color.Black
                )
            }
        }
        Spacer(Modifier.height(12.dp))
        //리뷰 본문
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
        if (!review.imageNames.isNullOrEmpty()){
            DynamicReviewImages(reviewImages = review.imageNames)
            Spacer(Modifier.height(12.dp))
        }
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
                if (textLayoutResult.lineCount > 3){
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


