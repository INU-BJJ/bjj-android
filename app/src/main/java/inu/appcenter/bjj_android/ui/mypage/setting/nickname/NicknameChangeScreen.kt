package inu.appcenter.bjj_android.ui.mypage.setting.nickname

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.ui.theme.Gray_B9B9B9
import inu.appcenter.bjj_android.ui.theme.Gray_D9D9D9
import inu.appcenter.bjj_android.ui.theme.Orange_FF7800
import inu.appcenter.bjj_android.ui.theme.paddings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NicknameChangeScreen(
    onNavigateBack: () -> Unit,
    nicknameChangeViewModel: NicknameChangeViewModel,
    successChange: () -> Unit,
) {
    val uiState by nicknameChangeViewModel.uiState.collectAsState()
    var nickname by remember { mutableStateOf("") }

    // 화면에 진입할 때 닉네임 정보 불러오기
    LaunchedEffect(Unit) {
        nicknameChangeViewModel.fetchCurrentNickname()
    }

    // 화면을 나갈 때 상태 초기화
    DisposableEffect(Unit) {
        onDispose {
            nicknameChangeViewModel.resetState()
        }
    }

    // 닉네임 변경 성공 시 처리
    LaunchedEffect(uiState.changeNicknameState) {
        if (uiState.changeNicknameState == NicknameState.Success) {
            // 상태 리셋 후 성공 콜백 호출
            nicknameChangeViewModel.resetChangeNicknameState()
            successChange()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "닉네임 변경하기",
                        style = LocalTypography.current.semibold18.copy(
                            lineHeight = 15.sp,
                            letterSpacing = 0.13.sp
                        )
                    )
                },
                navigationIcon = {
                    Icon(
                        modifier = Modifier
                            .padding(start = MaterialTheme.paddings.topBarPadding - MaterialTheme.paddings.iconOffset)
                            .offset(y = MaterialTheme.paddings.iconOffset)
                            .clickable { onNavigateBack() },
                        painter = painterResource(id = R.drawable.leftarrow),
                        contentDescription = stringResource(R.string.back_description)
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black
                )
            )
        }
    ) { contentPadding ->
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
                                nicknameChangeViewModel.resetNicknameCheckState()
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
                            nicknameChangeViewModel.checkNickname(nickname = nickname)
                        },
                        enabled = nickname.isNotBlank(),
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
                when(uiState.checkNicknameState) {
                    NicknameState.Success -> {
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
                    is NicknameState.Error -> {
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
                    else -> {
                        // Idle 또는 Loading 상태에서는 아무것도 표시하지 않음
                    }
                }
            }

            Button(
                onClick = {
                    if (uiState.checkNicknameState == NicknameState.Success) {
                        nicknameChangeViewModel.changeNickname(nickname)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (uiState.checkNicknameState == NicknameState.Success) Orange_FF7800 else Gray_B9B9B9,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .padding(bottom = 40.dp)
                    .width(319.dp)
                    .height(47.dp),
                shape = RoundedCornerShape(11.dp)
            ) {
                Text(
                    text = "닉네임 변경하기",
                    style = LocalTypography.current.semibold15.copy(
                        lineHeight = 18.sp,
                        letterSpacing = 0.13.sp
                    )
                )
            }
        }
    }
}