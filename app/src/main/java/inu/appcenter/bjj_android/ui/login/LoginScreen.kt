package inu.appcenter.bjj_android.ui.login

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.ui.login.common.SocialLoginButton
import inu.appcenter.bjj_android.ui.theme.Green_27D34A
import inu.appcenter.bjj_android.ui.theme.White_FFFFFF
import inu.appcenter.bjj_android.ui.theme.Yellow_FFEB02

@Composable
fun LoginScreen(
    onLoginSuccessAlreadySignup: () -> Unit,
    onLoginSuccessFirst : () -> Unit,
    onLoginFailure: () -> Unit,
    authViewModel: AuthViewModel
){
    var showLoginDialog by remember { mutableStateOf(false) }

    val signupState by authViewModel.signupState.collectAsState()

    val socialLogin by authViewModel.socialName.collectAsState()

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
        SocialLoginDialog(
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
            authViewModel = authViewModel,
            socialLogin = socialLogin
        )
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .background(color = Color.White)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SocialLoginButton(
            onClick = {
                authViewModel.setSocialName(it)
                showLoginDialog = true
            },
            socialLogin = "kakao",
            background = Yellow_FFEB02,
            icon = painterResource(R.drawable.kakao),
            text = "카카오로 로그인하기"
        )
        Spacer(modifier = Modifier.height(11.dp))
        SocialLoginButton(
            onClick = {
                authViewModel.setSocialName(it)
                showLoginDialog = true
            },
            socialLogin = "naver",
            textColor = White_FFFFFF,
            background = Green_27D34A,
            icon = painterResource(R.drawable.naver),
            text = "네이버로 로그인하기"
        )
        Spacer(modifier = Modifier.height(11.dp))
        SocialLoginButton(
            onClick = {
                authViewModel.setSocialName(it)
                showLoginDialog = true
            },
            socialLogin = "google",
            background = White_FFFFFF,
            icon = painterResource(R.drawable.google),
            text = "구글로 로그인하기",
            isBorderStroke = true
        )
    }
}


