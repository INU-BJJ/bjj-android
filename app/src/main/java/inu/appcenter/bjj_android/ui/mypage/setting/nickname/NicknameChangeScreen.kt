package inu.appcenter.bjj_android.ui.mypage.setting.nickname

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.ui.component.NicknameInputComponent
import inu.appcenter.bjj_android.ui.component.noRippleClickable
import inu.appcenter.bjj_android.ui.theme.Gray_B9B9B9
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
            // 닉네임 입력 컴포넌트 적용
            NicknameInputComponent(
                nickname = nickname,
                onNicknameChange = { nickname = it },
                onCheckNickname = { nicknameChangeViewModel.checkNickname(nickname) },
                nicknameState = uiState.checkNicknameState,
                resetState = { nicknameChangeViewModel.resetNicknameCheckState() }
            )

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