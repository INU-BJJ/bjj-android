package inu.appcenter.bjj_android.feature.profile.presentation.shop

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.model.item.ItemLevel
import inu.appcenter.bjj_android.model.item.ItemResponseItem
import inu.appcenter.bjj_android.shared.theme.Brown_523023
import inu.appcenter.bjj_android.shared.theme.Brown_C49A6C
import inu.appcenter.bjj_android.shared.theme.White_FFF8F0
import inu.appcenter.bjj_android.shared.theme.White_FFFFFF
import inu.appcenter.bjj_android.core.util.isValidItem

@Composable
fun ItemSection(
    levelTitle: String,
    items: List<ItemResponseItem>,
    onItemClick: (ItemResponseItem) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    // 수평 패딩 및 아이템 간 간격 정의
    val horizontalPadding = 15.dp
    val itemSpacing = 4.dp

    // 실제 사용 가능한 너비 계산 (패딩과 여백 제외)
    val availableWidth = screenWidth - ((horizontalPadding + 17.dp) * 2)

    // 4개의 아이템과 3개의 간격을 고려한 카드 1개의 너비 계산
    val cardWidth = (availableWidth - (itemSpacing * 3)) / 4

    // 전체 아이템의 개수에 따라 Row 개수 계산
    val rowCount = kotlin.math.ceil(items.size / 4.0).toInt()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 레벨 제목 박스
        Box(
            modifier = Modifier
                .background(
                    color = White_FFF8F0,
                    shape = RoundedCornerShape(5.dp)
                )
                .padding(horizontal = 22.dp, vertical = 3.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = levelTitle,
                style = LocalTypography.current.semibold15.copy(
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = Brown_523023
                ),
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 아이템이 없는 경우 메시지 표시

        Column(
            verticalArrangement = Arrangement.spacedBy(7.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // 동적으로 Row 생성
            for (rowIndex in 0 until rowCount) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(
                        itemSpacing,
                        Alignment.CenterHorizontally
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    repeat(4) { colIndex ->
                        val itemIndex = rowIndex * 4 + colIndex
                        if (itemIndex < items.size) {
                            val item = items[itemIndex]
                            if (item.isOwned && item.expiresAt != null && item.expiresAt.isValidItem()) {
                                // 유효한 아이템이면 정상 렌더링
                                ItemCard(
                                    item = item,
                                    cardWidth = cardWidth,
                                    onItemClick = onItemClick
                                )
                            } else {
                                // 소유했지만 유효기간이 없거나 만료된 아이템은 EmptyCard로
                                EmptyCard(
                                    level = ItemLevel.valueOf(item.itemLevel),
                                    cardWidth = cardWidth
                                )
                            }
                        } else {
                            // 아이템 개수보다 많아진 공간은 그냥 Spacer로 비워둔다
                            Spacer(modifier = Modifier.width(cardWidth))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyCard(
    level: ItemLevel,
    cardWidth: androidx.compose.ui.unit.Dp
) {
    Box(
        modifier = Modifier
            .width(cardWidth)
            .height((cardWidth.value * 92 / 71).dp)
    ){
        Image(
            painter = when(level){
                ItemLevel.DEFAULT -> painterResource(R.drawable.common_card)
                ItemLevel.COMMON -> painterResource(R.drawable.common_card)
                ItemLevel.NORMAL -> painterResource(R.drawable.normal_card)
                ItemLevel.RARE -> painterResource(R.drawable.rare_card)
            },
            contentDescription = "카드 뒷면",
            modifier = Modifier
                .fillMaxSize()
        )
    }
}