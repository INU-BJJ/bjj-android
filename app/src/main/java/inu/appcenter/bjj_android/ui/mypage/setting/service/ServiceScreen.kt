package inu.appcenter.bjj_android.ui.mypage.setting.service

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.ui.component.noRippleClickable
import inu.appcenter.bjj_android.ui.theme.paddings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceScreen(
    onNavigateBack: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.term_of_service),
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
                .verticalScroll(scrollState)
                .padding(
                    horizontal = MaterialTheme.paddings.large,
                    vertical = MaterialTheme.paddings.xlarge
                ),
            verticalArrangement = Arrangement.Top
        ) {
            ParsedTermsContent()
        }
    }
}

/**
 * 약관 내용을 구조화된 형태로 표시하는 컴포넌트
 */
@Composable
private fun ParsedTermsContent() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        TitleSection("밥점줘 서비스 이용약관")
        NormalText("본 약관은 '밥점줘'(이하 \"회사\" 또는 \"서비스\")와 이용자 간의 권리, 의무 및 책임사항을 규정합니다.")

        MainSection("1. 목적")
        NormalText("이 약관은 밥점줘 앱을 통해 제공되는 리뷰 작성, 포인트 적립 및 아이템 뽑기 기능 등 일체의 서비스 이용에 관한 조건과 절차를 명시합니다.")

        MainSection("2. 이용계약의 성립")
        NormalText("회원 가입 후 본 약관에 동의함으로써 이용 계약이 체결됩니다.")

        MainSection("3. 서비스 내용")
        SubItem("a. 인천대학교 학식 정보 제공")
        SubItem("b. 학식 리뷰 작성 및 조회")
        SubItem("c. 포인트 적립 및 사용")
        SubItem("d. 아이템 뽑기 기능")
        SubItem("e. 학식 랭킹 시스템")

        MainSection("4. 회원의 의무")
        SubItem("a. 타인의 정보를 도용하거나 허위 정보를 입력하지 않습니다.")
        SubItem("b. 부적절하거나 욕설이 포함된 리뷰를 작성하지 않습니다.")
        SubItem("c. 포인트를 부정하게 적립하거나 시스템을 악용하지 않습니다.")

        MainSection("5. 서비스의 중단 및 변경")
        SubItem("a. 서비스는 운영상 또는 기술상의 필요에 따라 변경되거나 중단될 수 있습니다.")
        SubItem("b. 긴급한 상황(서버 오류, 보안 이슈 등)의 경우 사전 고지 없이 일시 중단될 수 있으며, 이로 인한 손해에 대해 회사는 고의 또는 중대한 과실이 없는 한 책임을 지지 않습니다.")

        MainSection("6. 포인트 및 아이템 관련")
        SubItem("a. 포인트는 리뷰 작성 시 적립되며, 아이템 뽑기에 사용할 수 있습니다.")
        SubItem("b. 포인트는 현금으로 환급되지 않으며, 회사는 사전 통지 없이 포인트 정책을 변경할 수 있습니다.")

        MainSection("7. 책임의 제한")
        SubItem("a. 학식 정보나 리뷰는 참고용이며, 회사는 정보의 정확성이나 최신성에 대해 보장하지 않습니다.")
        SubItem("b. 사용자의 부주의로 인한 포인트 손실, 계정 도용 등에 대해 회사는 책임지지 않습니다.")

        MainSection("8. 약관의 개정")
        SubItem("a. 회사는 필요한 경우 본 약관을 변경할 수 있으며, 변경된 약관은 앱 내 동의 절차 또는 별도 화면을 통해 안내될 수 있습니다.")
        SubItem("b. 변경된 약관에 동의하지 않을 경우, 사용자는 서비스 이용을 중단하고 탈퇴할 수 있습니다.")

        MainSection("9. 문의사항")
        ContactSection("이메일: highyun3269@gmail.com")

        MainSection("10. 부칙")
        NormalText("본 약관은 2025년 7월 23일부터 적용됩니다.")
    }
}

@Composable
private fun TitleSection(content: String) {
    Text(
        text = content,
        style = LocalTypography.current.semibold18.copy(
            lineHeight = 22.sp,
            letterSpacing = 0.13.sp,
            fontWeight = FontWeight(600),
            color = Color.Black
        ),
        modifier = Modifier.padding(bottom = MaterialTheme.paddings.medium)
    )
}

@Composable
private fun MainSection(content: String) {
    Text(
        text = content,
        style = LocalTypography.current.medium15.copy(
            lineHeight = 18.sp,
            letterSpacing = 0.13.sp,
            fontWeight = FontWeight(500),
            color = Color.Black
        ),
        modifier = Modifier.padding(
            top = MaterialTheme.paddings.medium,
            bottom = MaterialTheme.paddings.small
        )
    )
}

@Composable
private fun SubItem(content: String) {
    // a., b., c. 등의 번호와 내용을 다르게 스타일링
    val annotatedText = buildAnnotatedString {
        val parts = content.split(". ", limit = 2)
        if (parts.size == 2) {
            // 번호 부분 (a., b., c. 등)
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight(500),
                    color = Color.Black
                )
            ) {
                append("${parts[0]}. ")
            }

            // 내용 부분
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight(400),
                    color = Color.Black
                )
            ) {
                append(parts[1])
            }
        } else {
            append(content)
        }
    }

    Text(
        text = annotatedText,
        style = LocalTypography.current.medium15.copy(
            lineHeight = 18.sp,
            letterSpacing = 0.13.sp
        ),
        modifier = Modifier.padding(
            start = MaterialTheme.paddings.medium,
            bottom = MaterialTheme.paddings.xsmall
        )
    )
}

@Composable
private fun NormalText(content: String) {
    Text(
        text = content,
        style = LocalTypography.current.medium15.copy(
            lineHeight = 18.sp,
            letterSpacing = 0.13.sp,
            fontWeight = FontWeight(400),
            color = Color.Black
        ),
        modifier = Modifier.padding(bottom = MaterialTheme.paddings.small)
    )
}

@Composable
private fun ContactSection(content: String) {
    // 이메일 정보를 강조
    val annotatedText = buildAnnotatedString {
        if (content.contains("이메일:")) {
            val parts = content.split(":", limit = 2)
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight(500),
                    color = Color.Black
                )
            ) {
                append("${parts[0]}: ")
            }

            if (parts.size == 2) {
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight(400),
                        color = Color.Black
                    )
                ) {
                    append(parts[1].trim())
                }
            }
        } else {
            append(content)
        }
    }

    Text(
        text = annotatedText,
        style = LocalTypography.current.medium15.copy(
            lineHeight = 18.sp,
            letterSpacing = 0.13.sp
        ),
        modifier = Modifier.padding(
            top = MaterialTheme.paddings.medium,
            bottom = MaterialTheme.paddings.small
        )
    )
}