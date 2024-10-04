package inu.appcenter.bjj_android.ui.review

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import inu.appcenter.bjj_android.ui.navigate.AppBottomBar
import inu.appcenter.bjj_android.ui.theme.Background

@Composable
fun ReviewScreen(
    navController: NavHostController

){
    Scaffold(
        bottomBar = { AppBottomBar(navController) }
    ) {
            innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(color = Background)
        ) {
        }
    }
}