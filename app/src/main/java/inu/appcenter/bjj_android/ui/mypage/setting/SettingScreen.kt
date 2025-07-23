package inu.appcenter.bjj_android.ui.mypage.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.ui.component.HorizontalDivider
import inu.appcenter.bjj_android.ui.component.noRippleClickable
import inu.appcenter.bjj_android.ui.mypage.component.MainText
import inu.appcenter.bjj_android.ui.theme.Red_FF0000
import inu.appcenter.bjj_android.ui.theme.paddings

private val BIGHEIGHT = 415.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    onNavigateBack: () -> Unit,
    onNavigeteToNickname: () -> Unit = {},
    onNavigateToLikedMenu: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onWithdrawalAccount: () -> Unit = {}
) {
    // 회원 탈퇴 확인 다이얼로그 표시 여부 상태
    var showDeleteAccountDialog by remember { mutableStateOf(false) }

    // 회원 탈퇴 확인 다이얼로그
    if (showDeleteAccountDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteAccountDialog = false },
            title = { Text(stringResource(R.string.withdraw_confirmation_title)) },
            text = { Text(stringResource(R.string.withdraw_confirmation_message)) },
            confirmButton = {
                Button(
                    onClick = {
                        onWithdrawalAccount()
                        showDeleteAccountDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Red_FF0000
                    )
                ) {
                    Text(stringResource(R.string.withdraw_confirm), color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeleteAccountDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.setting_title),
                        style = LocalTypography.current.semibold18.copy(
                            lineHeight = 15.sp,
                            letterSpacing = 0.13.sp,
                        )
                    )
                },
                navigationIcon = {
                    Icon(
                        modifier = Modifier
                            // 원래 20dp값 이지만 아이콘 자체에 내부 패딩이 있어서 16dp로 함
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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(innerPadding)
        ) {
            // 상단 메뉴 항목들
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.paddings.xlarge),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.paddings.xlarge)
            ) {
                MainText(
                    text = stringResource(R.string.change_nickname),
                    onClick = { onNavigeteToNickname() }
                )
                MainText(
                    text = stringResource(R.string.liked_menu),
                    onClick = { onNavigateToLikedMenu() }
                )
            }
            Spacer(Modifier.height(BIGHEIGHT - (2 * MaterialTheme.paddings.xlarge)))
            // Divider (padding 없이 전체 너비 차지)
            HorizontalDivider()

            // 하단 메뉴 항목들 (로그아웃, 탈퇴하기)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.paddings.xlarge),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.paddings.xlarge)
            ) {
                Text(
                    text = stringResource(R.string.logout),
                    style = LocalTypography.current.medium15.copy(
                        lineHeight = 18.sp,
                        letterSpacing = 0.13.sp,
                        fontWeight = FontWeight(400),
                        color = Color.Black
                    ),
                    modifier = Modifier.clickable { onNavigateToLogin() }
                )
                Text(
                    text = stringResource(R.string.withdraw),
                    style = LocalTypography.current.medium15.copy(
                        lineHeight = 18.sp,
                        letterSpacing = 0.13.sp,
                        fontWeight = FontWeight(400),
                        color = Red_FF0000
                    ),
                    modifier = Modifier.clickable { showDeleteAccountDialog = true }
                )
            }
        }
    }
}