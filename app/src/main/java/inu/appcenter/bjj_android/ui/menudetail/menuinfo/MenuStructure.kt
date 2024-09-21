package inu.appcenter.bjj_android.ui.menudetail.menuinfo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import inu.appcenter.bjj_android.ui.main.LocalTypography
import inu.appcenter.bjj_android.ui.main.MainMenu
import inu.appcenter.bjj_android.ui.theme.Orange_FFF4DF


@Composable
fun MenuStructure(menu: MainMenu) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 29.5.dp, end = 29.5.dp),
    ) {
        Text(
            text = "메뉴 구성",
            style = LocalTypography.current.menuDetail_menuStructure,
            color = Color.Black
        )
        Spacer(Modifier.height(22.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Orange_FFF4DF, shape = RoundedCornerShape(10.dp))
                .padding(vertical = 10.dp, horizontal = 15.dp)
        ) {
            menu.menuStructure.forEach { menuDetail ->
                Text(
                    text = menuDetail,
                    style = LocalTypography.current.menuDetail_menuStructureDetail,
                    color = Color.Black,
                )
            }
        }
    }
}