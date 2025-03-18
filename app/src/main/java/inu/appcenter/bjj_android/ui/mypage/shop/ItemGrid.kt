package inu.appcenter.bjj_android.ui.mypage.shop

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import inu.appcenter.bjj_android.model.item.ItemLevel
import inu.appcenter.bjj_android.model.item.ItemResponseItem
import inu.appcenter.bjj_android.model.item.ItemType
import inu.appcenter.bjj_android.model.item.toKorean
import inu.appcenter.bjj_android.ui.theme.Brown_C49A6C
import inu.appcenter.bjj_android.ui.theme.Brown_F2D9AF

@Composable
fun ItemGrid(
    modifier: Modifier = Modifier,
    selectedCategory: ItemType,
    itemList: List<ItemResponseItem>,
    onItemClick: (ItemResponseItem) -> Unit
){
    val categorizedItems = itemList.filter { it.itemType == selectedCategory.name }

    // 아이템 레벨별로 분류
    val commonItems = categorizedItems.filter { it.itemLevel.uppercase() == "COMMON" }
    val normalItems = categorizedItems.filter { it.itemLevel.uppercase() == "NORMAL" }
    val rareItems = categorizedItems.filter { it.itemLevel.uppercase() == "RARE" }

    Column(
        modifier = modifier
            .padding(horizontal = 17.dp, vertical = 0.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Brown_F2D9AF, shape = RoundedCornerShape(10.dp))
                .border(width = 1.dp, color = Brown_C49A6C, shape = RoundedCornerShape(10.dp))
                .padding(vertical = 10.dp)
        ) {
            // 각 레벨별로 아이템 섹션 생성
            item {
                ItemSection(
                    levelTitle = ItemLevel.COMMON.toKorean(),
                    items = commonItems,
                    onItemClick = onItemClick
                )
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
                ItemSection(
                    levelTitle = ItemLevel.NORMAL.toKorean(),
                    items = normalItems,
                    onItemClick = onItemClick
                )
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
                ItemSection(
                    levelTitle = ItemLevel.RARE.toKorean(),
                    items = rareItems,
                    onItemClick = onItemClick
                )
            }
        }
    }
}

