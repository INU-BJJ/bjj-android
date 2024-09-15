package inu.appcenter.bjj_android.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import inu.appcenter.bjj_android.ui.theme.Background

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,

){
    Column(
        modifier = Modifier
            .fillMaxSize().background(Background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                onLoginSuccess()
            }
        ) {
            Text("로그인")
        }
    }
}