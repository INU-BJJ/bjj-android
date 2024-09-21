package inu.appcenter.bjj_android.ui.menudetail.menuinfo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import inu.appcenter.bjj_android.ui.main.MainMenu


@Composable
fun HeaderSection(menu: MainMenu, navController: NavHostController) {
    Box(modifier = Modifier.fillMaxWidth()) {
        HeaderImage()
        NavigationButtons(navController)
        MenuInfoColumn(menu)
    }
}