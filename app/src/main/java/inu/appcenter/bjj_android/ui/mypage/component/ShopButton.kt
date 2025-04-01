package inu.appcenter.bjj_android.ui.mypage.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R

@Composable
fun ShopButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
){
    Column(
        modifier = modifier
            .clickable {
                onClick()
            },
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        Image(
            painter = painterResource(R.drawable.shop),
            contentDescription = stringResource(R.string.shop)
        )
        Spacer(Modifier.height(9.3.dp))
        Text(text = "상점", style = LocalTypography.current.bold15.copy(color = Color.Black, fontSize = 13.sp, fontWeight = FontWeight.Black))
    }
}