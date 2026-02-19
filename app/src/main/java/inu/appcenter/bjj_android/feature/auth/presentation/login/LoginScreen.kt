package inu.appcenter.bjj_android.feature.auth.presentation.login

import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import inu.appcenter.bjj_android.BuildConfig
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.feature.auth.presentation.login.common.SocialLoginButton
import inu.appcenter.bjj_android.shared.theme.Green_27D34A
import inu.appcenter.bjj_android.shared.theme.Orange_FF7800
import inu.appcenter.bjj_android.shared.theme.White_FFFFFF
import inu.appcenter.bjj_android.shared.theme.Yellow_FFEB02
import kotlinx.coroutines.launch
import org.json.JSONObject

@Composable
fun LoginScreen(
    onLoginSuccessAlreadySignup: () -> Unit,
    onLoginSuccessFirst : () -> Unit,
    onLoginFailure: () -> Unit,
    authViewModel: AuthViewModel
){
    val scope = rememberCoroutineScope()
    val context = LocalContext.current


    var showLoginDialog by rememberSaveable { mutableStateOf(false) }

    val authUiState by authViewModel.uiState.collectAsState()

    // 회원가입 상태 처리
    LaunchedEffect(authUiState.signupState) {
        when (authUiState.signupState) {
            is AuthState.Success -> {
                onLoginSuccessAlreadySignup()
                authViewModel.resetState()
            }
            is AuthState.Error -> {
                if ((authUiState.signupState as
                            AuthState.Error).message == "NEW_USER") {
                    onLoginSuccessFirst()
                    authViewModel.resetState()
                }
            }
            else -> {}
        }
    }


    if (showLoginDialog) {
        SocialLoginDialog(
            onLoginSuccessAlreadySignup = {
                onLoginSuccessAlreadySignup()
                showLoginDialog = false
            },
            onLoginSuccessFirst = {
                onLoginSuccessFirst()
                showLoginDialog = false
            },
            onLoginFailure = {
                onLoginFailure()
                showLoginDialog = false
            },
            authViewModel = authViewModel,
            authUiState = authUiState
        )
    }

    Box(
        modifier = Modifier.fillMaxSize()
            .background(
                White_FFFFFF
            )
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(horizontal = 17.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(141.dp))
            Icon(
                painter = painterResource(R.drawable.unfilled_bjj_logo),
                contentDescription = "앱 로고",
                modifier = Modifier
                    .width(94.dp)
                    .height(103.dp),
                tint = Orange_FF7800
            )
            Spacer(modifier = Modifier.height(13.dp))
            // 앱 이름
            Text(
                text = "밥점줘",
                style = LocalTypography.current.bold18.copy(
                    fontSize = 23.sp,
                    lineHeight = 30.sp,
                    fontWeight = FontWeight(700),
                    color = Orange_FF7800,
                    textAlign = TextAlign.Center,
                    letterSpacing = 0.13.sp,
                )
            )
            Spacer(modifier = Modifier.height(77.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "학식 뭐가 제일 맛있지?",
                    style = LocalTypography.current.bold18.copy(
                        fontSize = 15.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight(700),
                        color = Color(0xFFFF7800),
                        textAlign = TextAlign.Center,
                        letterSpacing = 0.13.sp,
                    ),
                    modifier = Modifier
                        .background(color = Color(0xFFFFF4DF), shape = RoundedCornerShape(size = 10.dp))
                        .padding(vertical = 4.dp, horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(25.dp))
                Text(
                    text = "지금 바로 평점을 남겨봐~",
                    style = LocalTypography.current.bold18.copy(
                        fontSize = 15.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight(700),
                        color = Color(0xFFFF7800),
                        textAlign = TextAlign.Center,
                        letterSpacing = 0.13.sp,
                    ),
                    modifier = Modifier
                        .padding(start = 139.dp)
                        .background(color = Color(0xFFFFF4DF), shape = RoundedCornerShape(size = 10.dp))
                        .padding(vertical = 4.dp, horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(25.dp))
                Text(
                    text = "식당 정보랑 메뉴도 바로 알 수 있어!",
                    style = LocalTypography.current.bold18.copy(
                        fontSize = 15.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight(700),
                        color = Color(0xFFFF7800),
                        textAlign = TextAlign.Center,
                        letterSpacing = 0.13.sp,
                    ),
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .background(color = Color(0xFFFFF4DF), shape = RoundedCornerShape(size = 10.dp))
                        .padding(vertical = 4.dp, horizontal = 16.dp)
                )
            }
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 20.dp),
        ) {
            // 소셜 로그인 버튼들

            SocialLoginButton(
                onClick = { _ ->
                    scope.launch {
                        try {
                            val credentialManager =
                                CredentialManager.create(context)
                            val googleIdOption =
                                GetGoogleIdOption.Builder()
                                    .setFilterByAuthorizedAccounts(false)
                                    .setServerClientId(BuildConfig.GOOGLE_WEB_CLIENT_ID)
                                    .build()
                            val request =
                                GetCredentialRequest.Builder()
                                    .addCredentialOption(googleIdOption)
                                    .build()

                            val result =
                                credentialManager.getCredential(context, request)
                            val googleCredential =
                                GoogleIdTokenCredential.createFrom(result.credential.data)

                            val email = googleCredential.id
                            val idToken = googleCredential.idToken

                            // idToken(JWT)에서 sub(providerId) 추출
                            val parts = idToken.split(".")
                            val padded =
                                parts[1].padEnd((parts[1].length + 3) / 4 * 4, '=')
                            val payload =
                                JSONObject(String(Base64.decode(padded, Base64.URL_SAFE)))
                            val providerId = payload.getString("sub")

                            authViewModel.setSocialName("google")

                            authViewModel.setSocialProviderId(providerId)
                            authViewModel.setSignupEmail(email)
                            authViewModel.login(providerId =
                                providerId, provider = "google")

                        } catch (e: GetCredentialException) {
                            onLoginFailure()
                        }
                    }
                },
                socialLogin = "google",
                background = White_FFFFFF,
                icon = painterResource(R.drawable.google),
                text = "구글로 시작하기",
                isBorderStroke = true
            )


            Spacer(modifier = Modifier.height(12.dp))


            SocialLoginButton(
                onClick = {
                    authViewModel.setSocialName(it)
                    showLoginDialog = true
                },
                socialLogin = "kakao",
                background = Yellow_FFEB02,
                icon = painterResource(R.drawable.kakao),
                text = "카카오로 시작하기",
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
                text = "네이버로 시작하기",
            )

            Spacer(modifier = Modifier.height(54.dp))
        }
    }
}