package inu.appcenter.bjj_android.ui.mypage.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.ui.mypage.component.MainText
import inu.appcenter.bjj_android.ui.theme.Red_FF0000

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    onNavigateBack: () -> Unit,
    onNavigeteToNickname: () -> Unit = {},
    onNavigateToLikedMenu: () -> Unit,
    onNavigateToBlockedUser: () -> Unit,
    onWithdrawalAccount: () -> Unit = {} // 탈퇴
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "설정",
                        style = LocalTypography.current.semibold18.copy(
                            textAlign = TextAlign.Center,
                            lineHeight = 15.sp,
                            letterSpacing = 0.13.sp,
                        )
                    )
                },
                navigationIcon = {
                    Icon(
                        modifier = Modifier
                            .offset(x = 20.dp, y = 4.5.dp)
                            .clickable { onNavigateBack() },
                        painter = painterResource(id = R.drawable.leftarrow),
                        contentDescription = "뒤로 가기",
                        tint = Color.Black
                    )

                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    actionIconContentColor = Color.Black
                ),
            )
        },
        containerColor = Color.White,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            MainText(
                text = "닉네임 변경하기",
                onClick = { onNavigeteToNickname() }
            )
            MainText(
                text = "좋아요한 메뉴",
                onClick = { onNavigateToLikedMenu() }
            )
            MainText(
                text = "차단한 유저 보기",
                onClick = { onNavigateToBlockedUser() }
            )
            Spacer(Modifier.height(400.dp))
            Text(
                text = "탈퇴하기",
                style = LocalTypography.current.medium15.copy(
                    lineHeight = 18.sp,
                    letterSpacing = 0.13.sp,
                    fontWeight = FontWeight(400),
                    color = Red_FF0000
                ),
                modifier = Modifier.clickable { onWithdrawalAccount() }
            )
        }
    }
}