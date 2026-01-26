package inu.appcenter.bjj_android.feature.profile.presentation.shop

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.model.item.ItemType
import inu.appcenter.bjj_android.model.item.toKorean
import inu.appcenter.bjj_android.shared.theme.Orange_FF7800

@Composable
fun ItemCategory(
    modifier: Modifier = Modifier,
    selectedCategory: ItemType,
    selectCategory: (ItemType) -> Unit
) {
    // 로컬 밀도를 사용하여 픽셀을 dp로 변환
    val density = LocalDensity.current

    // 캐릭터 버튼의 측정된 너비를 저장
    var characterWidth by remember { mutableStateOf(0.dp) }
    var characterHeight by remember { mutableStateOf(0.dp) }

    Row(
        modifier = modifier.height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.End
    ) {
        // 캐릭터 카테고리 카드
        Card(
            shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (selectedCategory == ItemType.CHARACTER) Color.White else Orange_FF7800
            ),
            modifier = Modifier
                .onGloballyPositioned { coordinates ->
                    // 캐릭터 카드의 크기를 측정
                    characterWidth = with(density) { coordinates.size.width.toDp() }
                    characterHeight = with(density) { coordinates.size.height.toDp() }
                }
                .clickable { selectCategory(ItemType.CHARACTER) }
        ) {
            Box(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = ItemType.CHARACTER.toKorean(),
                    style = LocalTypography.current.bold15.copy(
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        letterSpacing = 0.13.sp,
                    ),
                    color = if (selectedCategory == ItemType.CHARACTER) Orange_FF7800 else Color.White,
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
        }

        Spacer(Modifier.width(6.dp))

        // 배경 카테고리 카드 (캐릭터 카드 크기에 맞춤)
        Card(
            shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (selectedCategory == ItemType.BACKGROUND) Color.White else Orange_FF7800
            ),
            modifier = Modifier
                .width(characterWidth) // 캐릭터 카드의 너비 적용
                .height(characterHeight) // 캐릭터 카드의 높이 적용
                .clickable { selectCategory(ItemType.BACKGROUND) }
        ) {
            Box(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = ItemType.BACKGROUND.toKorean(),
                        style = LocalTypography.current.bold15.copy(
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            letterSpacing = 0.13.sp,
                        ),
                        color = if (selectedCategory == ItemType.BACKGROUND) Orange_FF7800 else Color.White,
                        modifier = Modifier
                    )
                }

            }
        }
    }
}