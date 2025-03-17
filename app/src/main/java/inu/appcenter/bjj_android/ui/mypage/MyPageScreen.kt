package inu.appcenter.bjj_android.ui.mypage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.ui.login.AuthState
import inu.appcenter.bjj_android.ui.login.AuthViewModel
import inu.appcenter.bjj_android.ui.navigate.AllDestination
import inu.appcenter.bjj_android.ui.navigate.AppBottomBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPageScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    val authUiState by authViewModel.uiState.collectAsState()

    // 로그아웃 상태 관찰
    LaunchedEffect(authUiState.logoutState) {
        when (authUiState.logoutState) {
            is AuthState.Success -> {
                // 로그아웃 성공 시 로그인 화면으로 이동하고 백스택 클리어
                navController.navigate(AllDestination.Login.route) {
                    popUpTo(navController.graph.id) { inclusive = true }
                }
                // 로그아웃 상태 초기화
                authViewModel.resetState()
            }
            is AuthState.Error -> {
                // 에러 처리가 필요한 경우 여기에 추가
                authViewModel.resetState()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.my_page_title),
                        style = LocalTypography.current.semibold18.copy(
                            textAlign = TextAlign.Center,
                            lineHeight = 15.sp,
                            letterSpacing = 0.13.sp,
                        )
                    )
                },
                actions = {
                    Icon(
                        painter = painterResource(R.drawable.setting),
                        contentDescription = stringResource(R.string.settings_icon_description),
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .padding(end = 18.dp)
                            .clickable {
                                navController.navigate(AllDestination.Setting.route)
                            }
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    actionIconContentColor = Color.Black
                ),
            )
        },
        bottomBar = { AppBottomBar(navController) },
        containerColor = Color.White,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {

        }
    }
}