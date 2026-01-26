package inu.appcenter.bjj_android.feature.profile.presentation.shop

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import inu.appcenter.bjj_android.R

@Composable
fun DrawMachine(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
){
    Column(
        modifier = modifier
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.draw_text),
            contentDescription = stringResource(R.string.draw_text),
            modifier = Modifier
                .width(110.dp)
                .height(50.dp)
                .offset(y = (21).dp)
        )
        Image(
            painter = painterResource(R.drawable.draw_machine),
            contentDescription = stringResource(R.string.draw_machine)
        )
    }
}