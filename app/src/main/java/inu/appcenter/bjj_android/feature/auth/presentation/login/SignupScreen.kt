package inu.appcenter.bjj_android.feature.auth.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.model.member.SignupReq
import inu.appcenter.bjj_android.shared.component.HorizontalDivider
import inu.appcenter.bjj_android.shared.component.NicknameInputComponent
import inu.appcenter.bjj_android.shared.theme.Gray_B9B9B9
import inu.appcenter.bjj_android.shared.theme.Gray_D9D9D9
import inu.appcenter.bjj_android.shared.theme.Gray_F6F6F8
import inu.appcenter.bjj_android.shared.theme.Gray_F6F8F8
import inu.appcenter.bjj_android.shared.theme.Orange_FF7800
import inu.appcenter.bjj_android.shared.theme.paddings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    successSignup: () -> Unit,
    uiState: AuthUiState
){
    var nickname by remember { mutableStateOf("") }

    // 약관동의 상태
    var ageAgreement by remember { mutableStateOf(false) }
    var serviceAgreement by remember { mutableStateOf(false) }
    var privacyAgreement by remember { mutableStateOf(false) }
    var allAgreement by remember { mutableStateOf(false) }

    // 전체 동의 체크박스 로직
    LaunchedEffect(ageAgreement, serviceAgreement, privacyAgreement) {
        allAgreement = ageAgreement && serviceAgreement && privacyAgreement
    }

    // 전체 동의 클릭 시 모든 약관 동의/해제
    fun toggleAllAgreement() {
        val newState = !allAgreement
        allAgreement = newState
        ageAgreement = newState
        serviceAgreement = newState
        privacyAgreement = newState
    }

    // 필수 약관이 모두 체크되었는지 확인
    val isRequiredAgreementsChecked = ageAgreement && serviceAgreement && privacyAgreement
    val canProceed = uiState.checkNicknameState == AuthState.Success && isRequiredAgreementsChecked

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
                        style = LocalTypography.current.bold18.copy(
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
                .padding(contentPadding),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 21.dp),
            ) {
                // 이메일 섹션
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

                // 닉네임 섹션
                NicknameInputComponent(
                    nickname = nickname,
                    onNicknameChange = { nickname = it },
                    onCheckNickname = { authViewModel.checkNickname(nickname) },
                    authState = uiState.checkNicknameState,
                    resetState = { authViewModel.resetNicknameCheckState() }
                )
            }

            Column{
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(7.dp),
                    color = Gray_F6F6F8
                )
                Spacer(Modifier.height(20.dp))
                Column(
                    modifier = Modifier
                        .padding(horizontal = 21.dp),
                ) {
                    // 약관동의 섹션
                    Text(
                        text = "약관 동의",
                        style = LocalTypography.current.medium15.copy(
                            lineHeight = 18.sp,
                            letterSpacing = 0.13.sp,
                            color = Color.Black
                        )
                    )
                    Spacer(Modifier.height(12.dp))

                    // 개별 약관 체크박스들
                    TermsCheckboxItem(
                        text = "(필수) 만 14세 이상입니다.",
                        checked = ageAgreement,
                        onCheckedChange = { ageAgreement = it }
                    )
                    Spacer(Modifier.height(12.dp))

                    TermsCheckboxItem(
                        text = "(필수) 서비스 이용약관에 동의합니다.",
                        checked = serviceAgreement,
                        onCheckedChange = { serviceAgreement = it }
                    )
                    Spacer(Modifier.height(12.dp))

                    TermsCheckboxItem(
                        text = "(필수) 개인정보 수집·이용에 동의합니다.",
                        checked = privacyAgreement,
                        onCheckedChange = { privacyAgreement = it }
                    )
                    Spacer(Modifier.height(12.dp))

                    // 구분선
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Gray_D9D9D9)
                    )
                    Spacer(Modifier.height(12.dp))

                    // 전체 동의 체크박스
                    TermsCheckboxItem(
                        text = "전체 동의",
                        checked = allAgreement,
                        onCheckedChange = { toggleAllAgreement() },
                        textStyle = LocalTypography.current.medium15.copy(
                            lineHeight = 17.sp,
                            letterSpacing = 0.13.sp,
                            color = Color.Black
                        ),
                        size = 24.dp,
                        spaceSize = 12.dp
                    )
                    Spacer(Modifier.height(34.dp))
                    Button(
                        onClick = {
                            if (canProceed) {
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
                            containerColor = if (canProceed) Orange_FF7800 else Gray_B9B9B9,
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
    }
}

@Composable
private fun TermsCheckboxItem(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    textStyle: androidx.compose.ui.text.TextStyle = LocalTypography.current.regular13.copy(
        lineHeight = 18.sp,
        letterSpacing = 0.13.sp,
        color = Color.Black
    ),
    size: Dp = 20.dp,
    spaceSize: Dp = 8.dp
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = Orange_FF7800,
                uncheckedColor = Gray_D9D9D9,
                checkmarkColor = Color.White
            ),
            modifier = Modifier.size(size).scale(size.value / 20f)
        )
        Spacer(Modifier.width(spaceSize))
        Text(
            text = text,
            style = textStyle
        )
    }
}