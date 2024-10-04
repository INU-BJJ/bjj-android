package inu.appcenter.bjj_android.ui.menudetail.menuinfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.ui.main.LocalTypography
import inu.appcenter.bjj_android.ui.main.MainMenu
import inu.appcenter.bjj_android.ui.menudetail.common.IconWithShadow
import inu.appcenter.bjj_android.ui.menudetail.common.numberFormatter


@Composable
fun MenuNameAndPrice(menu: MainMenu) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 39.dp, start = 29.5.dp, end = 29.5.dp)
    ) {
        Text(
            text = menu.menu,
            style = LocalTypography.current.semibold24.copy(letterSpacing = 0.13.sp),
            color = Color.Black
        )
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${menu.price.numberFormatter()}원",
                style = LocalTypography.current.regular20.copy(
                    letterSpacing = 0.13.sp,
                ),
                color = Color.Black
            )
            IconWithShadow(
                painter = painterResource(R.drawable.filled_heart),
                modifier = Modifier.size(25.dp),
                contentDescription = "상세 메뉴 좋아요",
                tint = Color.Unspecified,
                shadowOffset = Pair(4f, 4f),
                shadowRadius = 20f
            )
        }
    }
}