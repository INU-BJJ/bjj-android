package inu.appcenter.bjj_android.ui.mypage.setting.nickname

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
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
import androidx.compose.material3.HorizontalDivider
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
import inu.appcenter.bjj_android.ui.component.noRippleClickable
import inu.appcenter.bjj_android.ui.theme.Gray_999999
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
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

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
                        text = stringResource(R.string.nickname_change_title),
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
                            .noRippleClickable { onNavigateBack() },
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
                .padding(horizontal = MaterialTheme.paddings.large, vertical = MaterialTheme.paddings.xlarge),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.nickname_label),
                        style = LocalTypography.current.medium15.copy(
                            lineHeight = 18.sp,
                            letterSpacing = 0.13.sp,
                            color = Color.Black
                        )
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = stringResource(R.string.nickname_max_length_hint),
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
                    Box(modifier = Modifier.weight(1f)) {
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
                                    text = stringResource(R.string.nickname_placeholder),
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
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedPlaceholderColor = Gray_D9D9D9,
                                focusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                // TextField 기본 수평 패딩값이 16.dp이고 피그마에서는 왼쪽부터 6.dp만큼 패딩이 있어서 왼쪽으로 10.dp를 줘야함
                                .offset(x = (-10).dp),
                            maxLines = 1,
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done
                            ),
                            interactionSource = interactionSource // 추가: focus 상태 추적
                        )

                        // 커스텀 indicator 추가
                        HorizontalDivider(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .offset(y = (-14).dp), // 원하는 위치로 조정
                            thickness = 1.dp,
                            color = if (isFocused) Gray_999999 else Gray_D9D9D9 // focus 상태에 따라 색상 변경
                        )
                    }
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
                            text = stringResource(R.string.check_duplicate),
                            style = LocalTypography.current.regular13.copy(
                                lineHeight = 17.sp,
                                letterSpacing = 0.13.sp,
                                color = Color.Black
                            )
                        )
                    }
                }
                // 기존에는 indicator와의 거리때문에 Spacer 넣었지만 새로 만든 Divider를 offset으로 올려서 추가적으로 Spacer 안줘도 충분한 패딩값을 가지게 됌
//                Spacer(Modifier.height(7.5.dp))
                when(uiState.checkNicknameState) {
                    NicknameState.Success -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.success_with_background),
                                tint = Color.Unspecified,
                                contentDescription = stringResource(R.string.available_nickname_icon)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = stringResource(R.string.nickname_available),
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
                                contentDescription = stringResource(R.string.unavailable_nickname_icon)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = stringResource(R.string.nickname_unavailable),
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
                    text = stringResource(R.string.change_nickname_button),
                    style = LocalTypography.current.semibold15.copy(
                        lineHeight = 18.sp,
                        letterSpacing = 0.13.sp
                    )
                )
            }
        }
    }
}