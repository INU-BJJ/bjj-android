package inu.appcenter.bjj_android.ui.review.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.model.review.MyReviewDetailRes
import inu.appcenter.bjj_android.ui.theme.Gray_999999
import inu.appcenter.bjj_android.ui.theme.Gray_B9B9B9
import inu.appcenter.bjj_android.ui.theme.Orange_FF7800

/**
 * 리뷰 아이템 카드 컴포넌트
 *
 * @param reviewItem 리뷰 항목 데이터
 * @param onClick 아이템 클릭 시 실행할 콜백
 */
@Composable
fun ReviewItemCard(
    reviewItem: MyReviewDetailRes,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(63.dp)
            .border(0.5.dp, Gray_B9B9B9, shape = RoundedCornerShape(3.dp))
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 9.dp)
                .fillMaxWidth()
        ) {
            Spacer(Modifier.height(12.dp))
            Text(
                text = reviewItem.mainMenuName,
                style = LocalTypography.current.semibold15.copy(
                    letterSpacing = 0.13.sp,
                    lineHeight = 18.sp,
                ),
                color = Color.Black
            )
            Spacer(Modifier.height(5.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = reviewItem.createdDate.formatter(),
                    style = LocalTypography.current.regular13.copy(
                        letterSpacing = 0.13.sp,
                        lineHeight = 17.sp,
                    ),
                    color = Gray_999999
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.star),
                        contentDescription = stringResource(R.string.full_star_description),
                        tint = Orange_FF7800
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = reviewItem.rating.toString(),
                        style = LocalTypography.current.semibold15.copy(
                            letterSpacing = 0.13.sp,
                            lineHeight = 18.sp,
                        ),
                        color = Color.Black,
                        modifier = Modifier.width(13.dp), // 고정된 너비 설정해서 숫자에 따른 별 위치가 안 바뀌게 보이도록함
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}