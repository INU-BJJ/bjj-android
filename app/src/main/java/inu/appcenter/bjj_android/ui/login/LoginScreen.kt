package inu.appcenter.bjj_android.ui.login

import android.util.Log
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun LoginScreen(
    onLoginSuccessAlreadySignup: () -> Unit,
    onLoginSuccessFirst : () -> Unit,
    onLoginFailure: () -> Unit,
    authViewModel: AuthViewModel
){
    var showLoginDialog by remember { mutableStateOf(false) }

    val signupState by authViewModel.signupState.collectAsState()

    // 회원가입 상태 처리
    LaunchedEffect(signupState) {
        when (signupState) {
            is SignupState.Success -> {
                onLoginSuccessAlreadySignup()
                authViewModel.resetSignupState() // 상태 초기화
            }
            is SignupState.Error -> {
                // 에러 처리 (예: 스낵바 표시)
            }
            else -> {} // Loading 및 Idle 상태 처리
        }
    }

    if (showLoginDialog) {
        NaverLoginDialog(
            onLoginSuccessAlreadySignup = {
                onLoginSuccessAlreadySignup()
                showLoginDialog = false
                Log.e("LoginSuccessAlreadySignup", "LoginSuccessAlreadySignup")
            },
            onLoginSuccessFirst = {
                onLoginSuccessFirst()
                showLoginDialog = false
                Log.e("LoginSuccessFirst", "LoginSuccessFirst")
            },
            onLoginFailure = {
                onLoginFailure()
                showLoginDialog = false
                Log.e("LoginFailure", "Login failed")
            },
            authViewModel = authViewModel
        )
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .background(color = Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { showLoginDialog = true }) {
            Text("네이버 로그인")
        }
        Button(onClick = { onLoginSuccessAlreadySignup() }) {
            Text("임시 로그인")
        }
    }
}


