package inu.appcenter.bjj_android.ui.review.toolsAndUtils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.ui.review.ReviewViewModel
import inu.appcenter.bjj_android.ui.theme.Gray_999999
import inu.appcenter.bjj_android.ui.theme.Gray_B9B9B9
import inu.appcenter.bjj_android.ui.theme.Gray_F6F6F8

enum class DropdownType {
    RESTAURANT,
    MENU
}

@Composable
fun DropdownMenuBox(
    reviewViewModel: ReviewViewModel,
    dropdownType: DropdownType, // 드롭다운 타입 추가
    labelText: String // 초기 라벨 텍스트
) {
    var expanded by remember { mutableStateOf(false) }
    val reviewUiState by reviewViewModel.uiState.collectAsState()

    val selectedText = when (dropdownType) {
        DropdownType.RESTAURANT -> reviewUiState.selectedRestaurantAtReviewWrite ?: labelText
        DropdownType.MENU -> reviewUiState.selectedMenu?.mainMenuName ?: labelText
    }


    Box(
        modifier = Modifier
            .border(0.5.dp, Gray_B9B9B9, shape = RoundedCornerShape(3.dp))
            .height(45.dp)
            .width(319.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 18.dp, vertical = 13.dp)
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 라벨 텍스트 (예: "식당 위치")
            Text(
                text = selectedText,
                style = LocalTypography.current.semibold15.copy(
                    letterSpacing = 0.13.sp,
                    lineHeight = 18.sp,
                    color = Color.Black
                )
            )

            // 아래 화살표 아이콘
            Icon(
                painter = painterResource(id = R.drawable.grayunderarrow),
                contentDescription = "Dropdown Arrow",
                tint = Gray_999999
            )
        }

        // 식당 선택과 메뉴 선택에 따른 DropdownMenu 표시
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(319.dp)
                .background(color = Gray_F6F6F8)
        ) {
            when (dropdownType) {
                DropdownType.RESTAURANT -> {
                    reviewUiState.restaurants.forEach { restaurant ->
                        DropdownMenuItem(text = {
                            Text(
                                text = restaurant,
                                color = Color.Black
                            )
                        }, onClick = {
                            reviewViewModel.setSelectedReviewRestaurant(restaurant)
                            reviewViewModel.getMenusByCafeteria(restaurant)
                            expanded = false  // 메뉴 닫기

                            // 메뉴 선택 초기화
                            reviewViewModel.resetSelectedMenu()
                        })
                    }
                }
                DropdownType.MENU -> {
                    reviewUiState.menus.forEach { menu ->
                        DropdownMenuItem(
                            text = { Text(menu.mainMenuName, color = Color.Black) },
                            onClick = {
                                reviewViewModel.setSelectedMenu(menu)
                                expanded = false  // 메뉴 닫기
                            })
                    }
                }
            }
        }
    }
}