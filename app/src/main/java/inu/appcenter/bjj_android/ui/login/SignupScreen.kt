package inu.appcenter.bjj_android.ui.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.model.member.SignupReq
import inu.appcenter.bjj_android.ui.theme.Gray_B9B9B9
import inu.appcenter.bjj_android.ui.theme.Gray_D9D9D9
import inu.appcenter.bjj_android.ui.theme.Gray_F6F8F8
import inu.appcenter.bjj_android.ui.theme.Orange_FF7800
import inu.appcenter.bjj_android.ui.theme.paddings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    successSignup: () -> Unit,
    uiState: AuthUiState
){


    var nickname by remember { mutableStateOf("") }
    LaunchedEffect(uiState.signupState) {
        if (uiState.signupState == AuthState.Success) {
            successSignup()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "회원가입하기",
                        style = LocalTypography.current.semibold18.copy(
                            lineHeight = 15.sp,
                            letterSpacing = 0.13.sp
                        )
                    )
                },
                navigationIcon = {
                    Icon(
                        painter = painterResource(R.drawable.arrowback),
                        contentDescription = "backToLogin",
                        modifier = Modifier
                            .padding(start = MaterialTheme.paddings.topBarPadding - MaterialTheme.paddings.iconOffset)
                            .offset(y = MaterialTheme.paddings.iconOffset)
                            .clickable {
                                navController.popBackStack()
                            }
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black
                )
            )
        }
    ) {contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(contentPadding)
                .padding(horizontal = 21.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
            ) {
                Text(
                    text = "이메일",
                    style = LocalTypography.current.medium15.copy(
                        lineHeight = 18.sp,
                        letterSpacing = 0.13.sp,
                        color = Color.Black
                    )
                )
                Spacer(Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .background(
                            color = Gray_F6F8F8,
                            shape = RoundedCornerShape(3.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = Gray_D9D9D9,
                            shape = RoundedCornerShape(3.dp)
                        )
                        .padding(horizontal = 11.dp)
                ){
                    Text(
                        text = uiState.signupEmail,
                        style = LocalTypography.current.regular13.copy(
                            lineHeight = 17.sp,
                            letterSpacing = 0.13.sp,
                            color = Gray_B9B9B9,
                            textAlign = TextAlign.Left
                        ),
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                    )
                }
                Spacer(Modifier.height(33.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "닉네임",
                        style = LocalTypography.current.medium15.copy(
                            lineHeight = 18.sp,
                            letterSpacing = 0.13.sp,
                            color = Color.Black
                        )
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = "닉네임은 12글자까지 가능합니다.",
                        style = LocalTypography.current.regular13.copy(
                            lineHeight = 17.sp,
                            letterSpacing = 0.13.sp,
                            color = Gray_B9B9B9
                        )
                    )
                }
                Spacer(Modifier.height(7.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = nickname,
                        onValueChange = {
                            if (it.length <= 12) {
                                nickname = it
                                authViewModel.resetNicknameCheckState()
                            }
                        },
                        placeholder = {
                            Text(
                                text = "닉네임",
                                style = LocalTypography.current.regular13.copy(
                                    lineHeight = 17.sp,
                                    letterSpacing = 0.13.sp,
                                    color = Gray_D9D9D9
                                )
                            )
                        },
                        textStyle = LocalTypography.current.regular13.copy(
                            lineHeight = 15.sp,
                            letterSpacing = 0.13.sp,
                            color = Color.Black,
                        ),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            unfocusedPlaceholderColor = Gray_D9D9D9,
                            unfocusedIndicatorColor = Gray_D9D9D9,
                            focusedPlaceholderColor = Gray_D9D9D9,
                            focusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Gray_D9D9D9
                        ),
                        modifier = Modifier
                            .weight(1f),
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        )
                    )
                    Spacer(Modifier.width(6.5.dp))
                    OutlinedButton(
                        onClick = {
                            authViewModel.checkNickname(nickname = nickname)
                        },
                        border = BorderStroke(width = 1.dp, color = Orange_FF7800),
                        shape = RoundedCornerShape(100.dp),
                        contentPadding = PaddingValues(
                            vertical = 5.dp,
                            horizontal = 13.dp
                        ),
                        modifier = Modifier
                            .width(77.dp)
                            .height(27.dp)
                    ) {
                        Text(
                            text = "중복 확인",
                            style = LocalTypography.current.regular13.copy(
                                lineHeight = 17.sp,
                                letterSpacing = 0.13.sp,
                                color = Color.Black
                            )
                        )
                    }
                }
                Spacer(Modifier.height(7.5.dp))
                when(uiState.checkNicknameState){
                    AuthState.Success->{
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.success_with_background),
                                tint = Color.Unspecified,
                                contentDescription = "available nickname"
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = "사용 가능한 닉네임입니다.",
                                style = LocalTypography.current.regular11.copy(
                                    lineHeight = 15.sp,
                                    letterSpacing = 0.13.sp,
                                    color = Color.Black
                                )
                            )
                        }
                    }
                    AuthState.Error(message = "중복") -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.fail_with_background),
                                tint = Color.Unspecified,
                                contentDescription = "unavailable nickname"
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = "사용 불가능한 닉네임입니다.",
                                style = LocalTypography.current.regular11.copy(
                                    lineHeight = 15.sp,
                                    letterSpacing = 0.13.sp,
                                    color = Color.Black
                                )
                            )
                        }
                    }
                    AuthState.Idle->{
                    }
                    else->{

                    }
                }

            }
            Button(
                onClick = {
                    if (uiState.checkNicknameState == AuthState.Success){
                        authViewModel.signup(
                            signupReq = SignupReq(
                                nickname = nickname,
                                email = uiState.signupEmail,
                                provider = uiState.socialName
                            )
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (uiState.checkNicknameState == AuthState.Success) Orange_FF7800 else Gray_B9B9B9,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .padding(bottom = 40.dp)
                    .width(319.dp)
                    .height(47.dp),
                shape = RoundedCornerShape(11.dp)
            ) {
                Text(
                    text = "밥점줘 시작하기",
                    style = LocalTypography.current.semibold15.copy(
                        lineHeight = 18.sp,
                        letterSpacing = 0.13.sp
                    )
                )
            }
        }
    }
}