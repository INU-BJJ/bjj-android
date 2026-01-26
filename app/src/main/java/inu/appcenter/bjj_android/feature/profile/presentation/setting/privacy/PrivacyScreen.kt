package inu.appcenter.bjj_android.feature.profile.presentation.setting.privacy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.shared.component.noRippleClickable
import inu.appcenter.bjj_android.shared.theme.paddings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyScreen(
    onNavigateBack: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.privacy_policy),
                        style = LocalTypography.current.bold18.copy(
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
                .verticalScroll(scrollState)
                .padding(
                    horizontal = MaterialTheme.paddings.large,
                    vertical = MaterialTheme.paddings.xlarge
                ),
            verticalArrangement = Arrangement.Top
        ) {
            ParsedPrivacyContent()
        }
    }
}

/**
 * 개인정보처리방침 내용을 구조화된 형태로 표시하는 컴포넌트
 */
@Composable
private fun ParsedPrivacyContent() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        TitleSection("개인정보 처리방침")
        NormalText("본 개인정보처리방침은 '밥점줘'(이하 \"회사\" 또는 \"서비스\")가 이용자의 개인정보를 어떻게 수집, 이용, 보관, 파기하는지에 대해 규정합니다.")

        MainSection(" 1. 수집하는 개인정보 항목")
        SubItem("a. 이메일")
        SubItem("b. 닉네임")
        SubItem("c. 리뷰 내용")
        SubItem("d. 포인트 적립 및 사용 내역")
        SubItem("e. 아이템 보유 및 사용 정보")
        SubItem("f. 접속 로그 및 서비스 이용 기록")

        MainSection(" 2. 개인정보 수집 및 이용 목적")
        SubItem("a. 회원 식별 및 인증")
        SubItem("b. 리뷰 작성 및 관리")
        SubItem("c. 포인트 적립 및 아이템 뽑기 서비스 제공")
        SubItem("d. 서비스 운영 및 품질 개선")
        SubItem("e. 사용자 랭킹 및 통계 제공")

        MainSection(" 3. 개인정보 보유 및 이용 기간")
        SubItem("a. 회원 탈퇴 시까지 보유 및 이용")
        SubItem("b. 법령에 따른 보존 의무가 있는 경우 해당 기간 동안 보관")

        MainSection(" 4. 개인정보 제3자 제공")
        SubItem("a. 회사는 원칙적으로 개인정보를 외부에 제공하지 않습니다.")
        SubItem("b. 다만, 법령에 의하거나 이용자의 별도 동의를 받은 경우는 예외입니다.")

        MainSection(" 5. 개인정보 처리 위탁")
        SubItem("a. 현재 개인정보 처리를 위탁하지 않으며, 위탁 시 사전 고지 및 동의를 받습니다.")

        MainSection(" 6. 개인정보 파기 절차 및 방법")
        SubItem("a. 개인정보 보유 기간 경과 시 지체 없이 파기합니다.")
        SubItem("b. 전자적 파일은 복구 불가능한 방법으로 삭제하며, 종이 문서는 분쇄 또는 소각합니다.")

        MainSection(" 7. 이용자의 권리 및 행사 방법")
        SubItem("a. 언제든지 개인정보 열람, 정정, 삭제를 요청할 수 있습니다.")
        SubItem("b. 회원 탈퇴를 요청할 경우 모든 개인정보를 삭제합니다.")
        SubItem("c. 권리 행사는 앱 내 설정 또는 이메일 문의(highyun3269@gmail.com)로 가능합니다.")

        MainSection(" 8. 개인정보 보호책임자")
        SubItem("a. 이름: 고지윤")
        SubItem("b. 이메일: highyun3269@gmail.com")

        MainSection(" 9. 고지의 의무")
        SubItem("a. 개인정보처리방침 내용이 변경될 경우 앱 내 설정 메뉴를 통해 공지합니다.")

        MainSection("부칙)")
        NormalText("본 개인정보처리방침은 2025년 7월 23일부터 적용됩니다.")
    }
}

@Composable
private fun TitleSection(content: String) {
    Text(
        text = content,
        style = LocalTypography.current.bold15.copy(
            lineHeight = 22.sp,
            letterSpacing = 0.13.sp,
            color = Color.Black
        ),
        modifier = Modifier.padding(bottom = MaterialTheme.paddings.medium)
    )
}

@Composable
private fun MainSection(content: String) {
    Text(
        text = content,
        style = LocalTypography.current.medium13.copy(
            lineHeight = 18.sp,
            letterSpacing = 0.13.sp,
            color = Color.Black
        ),
        modifier = Modifier.padding(top = MaterialTheme.paddings.medium)
    )
}

@Composable
private fun SubItem(content: String) {
    val parts = content.split(". ", limit = 2)

    Row(
        modifier = Modifier.padding(start = MaterialTheme.paddings.xlarge),
        verticalAlignment = Alignment.Top
    ) {
        // 마커 부분 (a., b., c. 등)
        Text(
            text = if (parts.size == 2) "${parts[0]}. " else "",
            style = LocalTypography.current.medium13.copy(
                lineHeight = 18.sp,
                letterSpacing = 0.13.sp,
                color = Color.Black
            )
        )

        // 텍스트 내용 부분
        Text(
            text = if (parts.size == 2) parts[1] else content,
            style = LocalTypography.current.medium13.copy(
                lineHeight = 18.sp,
                letterSpacing = 0.13.sp,
                color = Color.Black
            ),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun NormalText(content: String) {
    Text(
        text = content,
        style = LocalTypography.current.medium13.copy(
            lineHeight = 18.sp,
            letterSpacing = 0.13.sp,
            color = Color.Black
        )
    )
}
