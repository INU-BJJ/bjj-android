package inu.appcenter.bjj_android.ui.menudetail.review

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.ui.main.LocalTypography
import inu.appcenter.bjj_android.ui.theme.Background
import inu.appcenter.bjj_android.ui.theme.Gray_B9B9B9

@Composable
fun SortDropdownMenu() {
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(0) }
    val items = listOf("메뉴일치순", "좋아요순", "최신순")

    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopStart)
    ) {
        Row(
            modifier = Modifier
                .clickable { expanded = true },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = items[selectedIndex],
                style = LocalTypography.current.menuDetail_sortingRule,
                color = Color.Black
            )
            Spacer(modifier = Modifier.width(5.dp))
            Icon(
                painter = painterResource(R.drawable.dropdown),
                contentDescription = "정렬 메뉴 더보기",
                tint = Color.Unspecified
            )
        }
        MaterialTheme(colorScheme = MaterialTheme.colorScheme.copy(surface = Background)) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .background(color = Background, shape = RoundedCornerShape(10.dp))
            ) {
                items.forEachIndexed { index, item ->
                    DropdownMenuItem(
                        text = { Text(item, style = LocalTypography.current.menuDetail_sortingRule, color = if (index == selectedIndex) Color.Black else Gray_B9B9B9) },
                        onClick = {
                            selectedIndex = index
                            expanded = false
                        }
                    )
                    if (index < items.size - 1) {
                        HorizontalDivider(color = Gray_B9B9B9, thickness = 0.5.dp)
                    }
                }
            }
        }

    }
}