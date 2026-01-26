package inu.appcenter.bjj_android.feature.menudetail.presentation.menuinfo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import inu.appcenter.bjj_android.R


@Composable
fun NavigationButtons(navController: NavHostController) {
    Row(
        modifier = Modifier
            .padding(vertical = 48.dp, horizontal = 20.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            painter = painterResource(R.drawable.arrowback),
            contentDescription = "뒤로가기",
            modifier = Modifier.clickable { navController.popBackStack() }
        )
        Icon(
            painter = painterResource(R.drawable.x),
            contentDescription = "뒤로가기_x",
            modifier = Modifier.clickable { navController.popBackStack() }
        )
    }
}