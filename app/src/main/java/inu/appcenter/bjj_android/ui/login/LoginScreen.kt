package inu.appcenter.bjj_android.ui.login

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import inu.appcenter.bjj_android.LocalTypography
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

    val authUiState by authViewModel.uiState.collectAsState()

    // 회원가입 상태 처리
    LaunchedEffect(authUiState.signupState) {
        when (authUiState.signupState) {
            is AuthState.Success -> {
                onLoginSuccessAlreadySignup()
                authViewModel.resetState() // 상태 초기화
            }
            is AuthState.Error -> {
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
            authUiState = authUiState
        )
    }

    Box(
        modifier = Modifier.fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.38f to Color(0xFFFF8D21), // 5% 위치에 오렌지
                        0.95f to Color(0xFFFFFFFF)  // 95% 위치에 흰색
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(200.dp))
            Image(
                painter = painterResource(R.drawable.unfilled_bjj_logo),
                contentDescription = "앱 로고",
                modifier = Modifier
                    .width(94.dp)
                    .height(103.dp),
            )
            Spacer(modifier = Modifier.height(12.dp))
            // 앱 이름
            Text(
                text = "밥점줘",
                style = LocalTypography.current.bold18.copy(
                    fontSize = 25.sp,
                    lineHeight = 30.sp,
                    fontWeight = FontWeight(700),
                    color = Color(0xFFFFFFFF),
                    textAlign = TextAlign.Center,
                    letterSpacing = 0.13.sp,
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            // 앱 설명
            Text(
                text = "인천대학교 학식 평점 커뮤니티",
                style = LocalTypography.current.semibold18.copy(
                    fontSize = 17.sp,
                    lineHeight = 22.sp,
                    letterSpacing = 0.13.sp,
                    color = Color(0xFFFFFFFF)
                )
            )

        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 20.dp),
        ) {
            // 소셜 로그인 버튼들
            SocialLoginButton(
                onClick = {
                    authViewModel.setSocialName(it)
                    showLoginDialog = true
                },
                socialLogin = "kakao",
                background = Yellow_FFEB02,
                icon = painterResource(R.drawable.kakao),
                text = "카카오로 시작하기"
            )
            Spacer(modifier = Modifier.height(12.dp))

            SocialLoginButton(
                onClick = {
                    authViewModel.setSocialName(it)
                    showLoginDialog = true
                },
                socialLogin = "naver",
                textColor = White_FFFFFF,
                background = Green_27D34A,
                icon = painterResource(R.drawable.naver),
                text = "네이버로 시작하기"
            )
            Spacer(modifier = Modifier.height(12.dp))

            SocialLoginButton(
                onClick = {
                    authViewModel.setSocialName(it)
                    showLoginDialog = true
                },
                socialLogin = "google",
                background = White_FFFFFF,
                icon = painterResource(R.drawable.google),
                text = "구글로 시작하기",
                isBorderStroke = true
            )
            Spacer(modifier = Modifier.height(54.dp))
        }
    }
}