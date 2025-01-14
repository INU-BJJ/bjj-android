package inu.appcenter.bjj_android.ui.menudetail.menuinfo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import inu.appcenter.bjj_android.model.todaydiet.TodayDietRes
import inu.appcenter.bjj_android.ui.menudetail.MenuDetailViewModel
import inu.appcenter.bjj_android.ui.menudetail.common.GrayHorizontalDivider


@Composable
fun MenuInfoColumn(menu: TodayDietRes, menuDetailViewModel: MenuDetailViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 218.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
            )
    ) {
        MenuNameAndPrice(menu, menuDetailViewModel =  menuDetailViewModel)
        Spacer(Modifier.height(22.8.dp))
        GrayHorizontalDivider(Modifier.padding(horizontal = 21.3.dp))
        Spacer(Modifier.height(29.8.dp))
        MenuStructure(menu)
    }
}