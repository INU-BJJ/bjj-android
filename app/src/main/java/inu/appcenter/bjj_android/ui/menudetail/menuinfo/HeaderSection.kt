package inu.appcenter.bjj_android.ui.menudetail.menuinfo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import inu.appcenter.bjj_android.model.todaydiet.TodayDietRes
import inu.appcenter.bjj_android.ui.menudetail.MenuDetailViewModel


@Composable
fun HeaderSection(menu: TodayDietRes, navController: NavHostController, menuDetailViewModel: MenuDetailViewModel) {
    Box(modifier = Modifier.fillMaxWidth()) {
        HeaderImage(menu)
        NavigationButtons(navController)
        MenuInfoColumn(menu, menuDetailViewModel = menuDetailViewModel)
    }
}