package inu.appcenter.bjj_android.feature.main.presentation.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.shared.theme.Gray_999999
import inu.appcenter.bjj_android.shared.theme.Orange_FF7800

@Composable
fun MainRestaurantButton(
    restaurant : String,
    restaurants : List<String>,
    selectedButton : String,
    onClick : (String) -> Unit
){
    OutlinedButton(
        modifier = Modifier
            .padding(start = if (restaurant == restaurants.first()) 20.dp else 0.dp,end = if (restaurant == restaurants.last()) 20.dp else 9.dp),
        onClick = {
            onClick(restaurant)
        },
        shape = RoundedCornerShape(100.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (restaurant == selectedButton) Orange_FF7800 else Color.White,
            contentColor = if (restaurant == selectedButton) Color.White else Gray_999999,
        ),
        border = BorderStroke(width = 1.dp, color = if (restaurant == selectedButton) Orange_FF7800 else Gray_999999),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 9.dp)
    ) {
        Text(
            text = restaurant,
            style = LocalTypography.current.medium15.copy(
                letterSpacing = 0.13.sp,
                lineHeight = 15.sp
            )
        )
    }
}