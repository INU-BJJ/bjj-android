package inu.appcenter.bjj_android.ui.mypage.shop

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import inu.appcenter.bjj_android.model.item.ItemResponseItem
import inu.appcenter.bjj_android.model.item.ItemType

@Composable
fun ItemFrame(
    modifier: Modifier = Modifier,
    selectedCategory: ItemType,
    itemList: List<ItemResponseItem>,
    selectCategory: (ItemType) -> Unit,
    onItemClick: (ItemResponseItem) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        ItemCategory(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 35.dp),
            selectedCategory = selectedCategory,
            selectCategory = selectCategory
        )
        Crossfade(
            targetState = selectedCategory,
            animationSpec = tween(durationMillis = 1000)
        ) { category ->
            ItemGrid(
                onItemClick = onItemClick,
                selectedCategory = category,
                itemList = itemList
            )
        }
    }
}