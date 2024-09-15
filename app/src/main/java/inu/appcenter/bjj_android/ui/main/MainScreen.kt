package inu.appcenter.bjj_android.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import inu.appcenter.bjj_android.ui.common.AppBottomBar
import inu.appcenter.bjj_android.ui.navigate.Screen
import inu.appcenter.bjj_android.ui.theme.Background

@Composable
fun MainScreen(
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

