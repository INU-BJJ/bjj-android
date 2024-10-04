package inu.appcenter.bjj_android.ui.menudetail.review

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.ui.main.LocalTypography
import inu.appcenter.bjj_android.ui.main.MainMenu
import inu.appcenter.bjj_android.ui.menudetail.Review
import inu.appcenter.bjj_android.ui.theme.Gray_999999
import inu.appcenter.bjj_android.ui.theme.Gray_B9B9B9
import inu.appcenter.bjj_android.ui.theme.Gray_D9D9D9
import inu.appcenter.bjj_android.ui.theme.Gray_F6F6F8
import inu.appcenter.bjj_android.ui.theme.Orange_FF7800
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


fun LocalDateTime.format(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
    return this.format(formatter)
}

@Composable
fun ReviewItem(
    review: Review,
    menu: MainMenu
) {

    var isExpanded by remember { mutableStateOf(false) }
    var lineCount by remember { mutableIntStateOf(0) }


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
            if (review.profileImage != null) {
                Image(
                    painter = painterResource(review.profileImage),
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
                    text = review.userName,
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
                    StarRatingCalculator(rating = review.reviewStar)
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = review.writtenTime.format(),
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
                    painter = painterResource( if (review.isGood) R.drawable.filled_good else R.drawable.unfilled_good),
                    contentDescription = "리뷰 별 좋아요 수",
                    tint = Color.Unspecified
                )
                Text(
                    text = review.goodCount.toString(),
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
            text = review.description,
            maxLines = 3,
            style = LocalTypography.current.regular13.copy(
                letterSpacing = 0.13.sp,
                lineHeight = 17.sp,
                color = Color.Black
            )
        )


        Spacer(Modifier.height(12.dp))
        if (!review.reviewImages.isNullOrEmpty()){
            DynamicReviewImages(reviewImages = review.reviewImages)
            Spacer(Modifier.height(12.dp))
        }
        LazyRow(
            userScrollEnabled = false
        ) {
            items(review.tags) { tag ->
                Box(
                    modifier = Modifier
                        .background(
                            color = if (menu.menu.equals(tag) or menu.menuStructure.contains(
                                    tag
                                )
                            ) Orange_FF7800 else Gray_F6F6F8, shape = RoundedCornerShape(3.dp)
                        )
                        .padding(horizontal = 7.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = tag,
                        style = LocalTypography.current.regular11.copy(
                            letterSpacing = 0.13.sp,
                            lineHeight = 15.sp,
                        ),
                        color = Color.Black
                    )
                }
                Spacer(Modifier.width(5.dp))
            }
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


