package inu.appcenter.bjj_android.shared.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import inu.appcenter.bjj_android.feature.auth.presentation.login.AuthState
import inu.appcenter.bjj_android.feature.profile.presentation.setting.nickname.NicknameState
import inu.appcenter.bjj_android.shared.theme.Gray_999999
import inu.appcenter.bjj_android.shared.theme.Gray_B9B9B9
import inu.appcenter.bjj_android.shared.theme.Gray_D9D9D9
import inu.appcenter.bjj_android.shared.theme.Orange_FF7800

/**
 * 닉네임 입력 컴포넌트
 * 닉네임 입력 필드, 중복 확인 버튼, 결과 메시지를 표시하는 공통 컴포넌트
 *
 * @param nickname 현재 닉네임 값
 * @param onNicknameChange 닉네임 변경 시 호출되는 콜백
 * @param onCheckNickname 중복 확인 버튼 클릭 시 호출되는 콜백
 * @param resetState 닉네임 변경 시 상태를 초기화하는 콜백
 * @param maxLength 닉네임 최대 길이 (기본값: 12)
 * @param isSuccess 닉네임 중복 확인 성공 여부
 * @param isError 닉네임 중복 확인 에러 여부
 * @param isDuplicate 닉네임 중복 여부
 * @param isLoading 중복 확인 로딩 중 여부
 */
@Composable
private fun NicknameInputComponent(
    nickname: String,
    onNicknameChange: (String) -> Unit,
    onCheckNickname: () -> Unit,
    resetState: () -> Unit,
    maxLength: Int = 12,
    isSuccess: Boolean,
    isError: Boolean,
    isDuplicate: Boolean,
    isLoading: Boolean
) {
    Column {
        // 닉네임 레이블 및 제한 설명
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

        // 닉네임 입력 필드 및 중복 확인 버튼
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // TextField를 Box로 감싸서 커스텀 인디케이터 구현
            Box(modifier = Modifier.weight(1f)) {
                val interactionSource = remember { MutableInteractionSource() }
                val isFocused by interactionSource.collectIsFocusedAsState()

                TextField(
                    value = nickname,
                    onValueChange = {
                        if (it.length <= maxLength) {
                            onNicknameChange(it)
                            resetState()
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
                    interactionSource = interactionSource // 포커스 상태 추적
                )

                // 커스텀 인디케이터 추가
                HorizontalDivider(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = (-14).dp), // 위치 조정
                    thickness = 1.dp,
                    color = if (isFocused) Gray_999999 else Gray_D9D9D9 // 포커스 상태에 따라 색상 변경
                )
            }

            Spacer(Modifier.width(6.5.dp))

            OutlinedButton(
                onClick = onCheckNickname,
                enabled = nickname.isNotBlank() && !isLoading,
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

        // 결과 메시지 표시 - Spacer 제거됨 (HorizontalDivider 위치 조정으로 충분한 간격 확보)
        if (isSuccess) {
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
        } else if (isError && isDuplicate) {
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
    }
}

/**
 * NicknameState를 사용하는 화면을 위한 닉네임 입력 컴포넌트
 *
 * @param nickname 현재 닉네임 값
 * @param onNicknameChange 닉네임 변경 시 호출되는 콜백
 * @param onCheckNickname 중복 확인 버튼 클릭 시 호출되는 콜백
 * @param nicknameState 닉네임 상태 (NicknameState)
 * @param resetState 닉네임 변경 시 상태를 초기화하는 콜백
 * @param maxLength 닉네임 최대 길이 (기본값: 12)
 */
@Composable
fun NicknameInputComponent(
    nickname: String,
    onNicknameChange: (String) -> Unit,
    onCheckNickname: () -> Unit,
    nicknameState: NicknameState,
    resetState: () -> Unit,
    maxLength: Int = 12
) {
    NicknameInputComponent(
        nickname = nickname,
        onNicknameChange = onNicknameChange,
        onCheckNickname = onCheckNickname,
        resetState = resetState,
        maxLength = maxLength,
        isSuccess = nicknameState is NicknameState.Success,
        isError = nicknameState is NicknameState.Error,
        isDuplicate = nicknameState is NicknameState.Error && nicknameState.message == "중복",
        isLoading = nicknameState is NicknameState.Loading
    )
}

/**
 * AuthState를 사용하는 화면을 위한 닉네임 입력 컴포넌트
 *
 * @param nickname 현재 닉네임 값
 * @param onNicknameChange 닉네임 변경 시 호출되는 콜백
 * @param onCheckNickname 중복 확인 버튼 클릭 시 호출되는 콜백
 * @param authState 닉네임 상태 (AuthState)
 * @param resetState 닉네임 변경 시 상태를 초기화하는 콜백
 * @param maxLength 닉네임 최대 길이 (기본값: 12)
 */
@Composable
fun NicknameInputComponent(
    nickname: String,
    onNicknameChange: (String) -> Unit,
    onCheckNickname: () -> Unit,
    authState: AuthState,
    resetState: () -> Unit,
    maxLength: Int = 12
) {
    NicknameInputComponent(
        nickname = nickname,
        onNicknameChange = onNicknameChange,
        onCheckNickname = onCheckNickname,
        resetState = resetState,
        maxLength = maxLength,
        isSuccess = authState is AuthState.Success,
        isError = authState is AuthState.Error,
        isDuplicate = authState is AuthState.Error && authState.message == "중복",
        isLoading = authState is AuthState.Loading
    )
}