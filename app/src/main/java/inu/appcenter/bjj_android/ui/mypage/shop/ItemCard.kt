package inu.appcenter.bjj_android.ui.mypage.shop

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.model.item.ItemResponseItem
import inu.appcenter.bjj_android.ui.theme.Orange_FF7800
import inu.appcenter.bjj_android.utils.ImageLoader
import inu.appcenter.bjj_android.utils.formatRemainingTime
import inu.appcenter.bjj_android.utils.isValidItem

@Composable
fun ItemCard(
    item: ItemResponseItem,
    onItemClick: (ItemResponseItem) -> Unit,
    cardWidth: Dp,
    modifier: Modifier = Modifier
) {
    // 아이템 카드의 높이 계산 (너비:높이 비율 = 71:92)
    val cardHeight = (cardWidth.value * 92 / 71).dp

    Box(
        modifier = modifier
            .width(cardWidth)
            .height(cardHeight)
            .border(
                width = 1.dp,
                color = Orange_FF7800,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable {
                onItemClick(item)
                Log.d("clickItem", item.toString())
            }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // 아이템 이미지 (상단 흰색 영역)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(cardHeight - 20.dp) // 하단 유효기간 영역 높이 제외
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                // 아이템 이미지가 있으면 ImageLoader로 로드하고, 없으면 기본 이미지 사용
                if (item.imageName.isNotEmpty()) {
                    ImageLoader.CharacterItem(
                        imageName = item.imageName,
                        showLoading = true,
                        modifier = Modifier
                            .width(cardWidth * 0.8f) // 이미지 크기는 카드 너비의 80%
                            .height(cardWidth * 0.8f), // 정사각형 비율
                        contentScale = ContentScale.Fit
                    )
                } else {
                    // 기본 이미지 (아이템 타입에 따라 다른 기본 이미지 사용)
                    val defaultImage = when (item.itemType.uppercase()) {
                        "CHARACTER" -> R.drawable.placeholder
                        "BACKGROUND" -> R.drawable.placeholder
                        else -> R.drawable.placeholder
                    }

                    Image(
                        painter = painterResource(id = defaultImage),
                        contentDescription = "아이템 이미지",
                        modifier = Modifier
                            .width(cardWidth * 0.8f)
                            .height(cardWidth * 0.8f)
                    )
                }

                // 착용 중인 아이템이면 표시
                if (item.isWearing) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Color.Black.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                            )
                            .padding(4.dp)
                            .align(Alignment.TopCenter),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "착용 중",
                            style = LocalTypography.current.regular11.copy(
                                color = Color.White,
                                fontSize = 9.sp
                            )
                        )
                    }
                }
            }

            // 유효 기간 (하단 오렌지색 영역)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .background(
                        color = Orange_FF7800,
                        shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                // 유효 기간 표시
                if (item.validPeriod != null && item.validPeriod.isValidItem()) {
                    Text(
                        text = item.validPeriod.formatRemainingTime(),
                        style = LocalTypography.current.regular11,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}