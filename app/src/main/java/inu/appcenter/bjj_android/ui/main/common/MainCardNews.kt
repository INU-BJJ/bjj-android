package inu.appcenter.bjj_android.ui.main.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MainCardNews(
    backgroundColor : Color,
    innerPadding : PaddingValues,
    content : @Composable () -> Unit
){
    Column(
        modifier = Modifier
            .background(color = backgroundColor)
            .fillMaxWidth()
            .height(168.dp)
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        content()
    }
}