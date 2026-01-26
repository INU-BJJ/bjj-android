package inu.appcenter.bjj_android.feature.menudetail.presentation.review

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.feature.menudetail.presentation.SortingRules
import inu.appcenter.bjj_android.shared.theme.Background
import inu.appcenter.bjj_android.shared.theme.Gray_B9B9B9

@Composable
fun SortDropdownMenu(
    selectedSortingRule: SortingRules,
    onSortingRuleChanged: (SortingRules) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val items = listOf(SortingRules.BEST_MATCH, SortingRules.MOST_LIKED, SortingRules.NEWEST_FIRST)

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
                text = selectedSortingRule.toKorean(),
                style = LocalTypography.current.regular13.copy(
                    letterSpacing = 0.13.sp,
                    lineHeight = 17.sp,
                ),
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
                        modifier = Modifier
                            .height(32.dp),
                        text = {
                            Text(
                                text = item.toKorean(),
                                style = LocalTypography.current.regular13.copy(
                                    letterSpacing = 0.13.sp,
                                    lineHeight = 17.sp,
                                    textAlign = TextAlign.Start
                                ),
                                color = if (item == selectedSortingRule) Color.Black else Gray_B9B9B9
                            )
                        },
                        onClick = {
                            onSortingRuleChanged(item)
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