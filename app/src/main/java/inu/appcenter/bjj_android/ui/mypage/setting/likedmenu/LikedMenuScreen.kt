package inu.appcenter.bjj_android.ui.mypage.setting.likedmenu

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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.ui.mypage.component.LikedMenuFrame
import inu.appcenter.bjj_android.ui.mypage.component.MainText
import inu.appcenter.bjj_android.ui.mypage.component.SwitchButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LikedMenuScreen(
    likedMenuViewModel: LikedMenuViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by likedMenuViewModel.uiState.collectAsStateWithLifecycle()

    // 알림 설정 상태
    var notificationEnabled by remember { mutableStateOf(uiState.notificationEnabled) }

    // 화면 진입 시 데이터 로드
    LaunchedEffect(Unit) {
        likedMenuViewModel.getLikedMenus()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.liked_menu_title),
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
                        contentDescription = stringResource(R.string.back_icon),
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
                .padding(horizontal = 15.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // 알림 설정 Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 9.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                MainText(text = stringResource(R.string.liked_alarm_toggle))
                SwitchButton(
                    checked = notificationEnabled,
                    onCheckedChange = { enabled ->
                        notificationEnabled = enabled
                        likedMenuViewModel.toggleNotification(enabled)
                    }
                )
            }
            Spacer(Modifier.height(14.dp))

            // 로딩 상태 표시
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            // 에러 표시
            else if (uiState.error != null) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.error ?: stringResource(R.string.generic_error),
                        color = Color.Red
                    )
                }
            }
            // 좋아요 메뉴 목록 표시
            else if (uiState.likedMenus.isNotEmpty()) {
                uiState.likedMenus.forEach { likedMenu ->
                    LikedMenuFrame(
                        menu = likedMenu.menuName,
                        onHeartClick = { likedMenuViewModel.toggleLike(likedMenu.menuId) }
                    )
                }
            }
            // 빈 목록일 경우
            else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 50.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_liked_menu),
                        style = LocalTypography.current.medium15,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}