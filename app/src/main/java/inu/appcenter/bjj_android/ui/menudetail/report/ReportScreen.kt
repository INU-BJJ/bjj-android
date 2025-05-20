package inu.appcenter.bjj_android.ui.menudetail.report

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.ui.theme.Gray_999999
import inu.appcenter.bjj_android.ui.theme.Gray_B9B9B9
import inu.appcenter.bjj_android.ui.theme.Orange_FF7800

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    navController: NavHostController,
    reviewId: Long,
    onReportComplete: () -> Unit
) {
    val reasonOptions = listOf(
        "메뉴와 관련없는 내용",
        "음란성, 욕설 등 부적절한 내용",
        "부적절한 홍보 또는 광고",
        "주문과 관련없는 사진 게시",
        "개인정보 유출 위험",
        "리뷰 작성 취지에 맞지 않는 내용(복사글 등)",
        "누군가를 비방하는 내용",
        "기타(하단 내용 작성)"
    )

    var selectedReasons by remember { mutableStateOf(List(reasonOptions.size) { false }) }
    var reportText by remember { mutableStateOf("") }
    var textCount by remember { mutableStateOf(0) }

    // 텍스트 필드 포커스 관리를 위한 FocusRequester
    val focusRequester = remember { FocusRequester() }
    val scrollState = rememberScrollState()
    val keyboardController = LocalSoftwareKeyboardController.current

    // 텍스트 필드에 포커스가 갈 때 자동으로 스크롤
    var isTextFieldFocused by remember { mutableStateOf(false) }

    // 기타 옵션 선택 여부 확인
    val isOtherReasonSelected by remember(selectedReasons) {
        derivedStateOf { selectedReasons.lastOrNull() == true }
    }

    // 다른 옵션 중 하나라도 선택 여부 확인
    val isAnyOtherReasonSelected by remember(selectedReasons) {
        derivedStateOf { selectedReasons.dropLast(1).any { it } }
    }

    // 버튼 활성화 여부 결정
    val isButtonEnabled by remember(selectedReasons, reportText) {
        derivedStateOf {
            isAnyOtherReasonSelected || (isOtherReasonSelected && reportText.isNotEmpty())
        }
    }

    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                ),
                title = {
                    Text(
                        text = "리뷰 신고하기",
                        style = LocalTypography.current.bold18.copy(
                            letterSpacing = 0.13.sp,
                            lineHeight = 15.sp
                        ),
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    Icon(
                        painter = painterResource(R.drawable.x),
                        contentDescription = "뒤로가기",
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .clickable { navController.popBackStack() },
                        tint = Color.Black,
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "리뷰를 신고하는 이유를 알려주세요!",
                    style = LocalTypography.current.bold15,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "타당한 근거 없이 신고된 내용은 관리자 확인 후 반영되지 않을 수 있습니다.",
                    style = LocalTypography.current.medium13,
                    color = Gray_999999
                )

                Spacer(modifier = Modifier.height(26.dp))

                // 체크박스 목록
                reasonOptions.forEachIndexed { index, reason ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 6.dp)
                            .clickable {
                                val newList = selectedReasons.toMutableList()
                                newList[index] = !newList[index]
                                selectedReasons = newList
                            }
                    ) {
                        CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                            Checkbox(
                                checked = selectedReasons[index],
                                onCheckedChange = {
                                    val newList = selectedReasons.toMutableList()
                                    newList[index] = it
                                    selectedReasons = newList
                                },
                                colors = CheckboxDefaults.colors(
                                    checkmarkColor = Color.White,
                                    checkedColor = Orange_FF7800,
                                    uncheckedColor = Gray_B9B9B9
                                )
                            )
                        }
                        Spacer(modifier = Modifier.width(13.dp))

                        Text(
                            text = reason,
                            style = LocalTypography.current.medium13.copy(lineHeight = 17.sp, letterSpacing = 0.13.sp),
                            color = Color.Black
                        )
                    }
                    if (index != reasonOptions.size - 1){
                        Spacer(Modifier.height(23.dp))
                    }
                }

                Spacer(modifier = Modifier.height(13.dp))

                // 기타 옵션이 선택된 경우에만 텍스트 필드 표시
                if (isOtherReasonSelected) {
                    // 추가 내용 입력 필드
                    OutlinedTextField(
                        value = reportText,
                        onValueChange = {
                            if (it.length <= 500) {
                                reportText = it
                                textCount = it.length
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .focusRequester(focusRequester),
                        placeholder = {
                            Text(
                                text = "신고하신 이유를 적어주세요.",
                                style = LocalTypography.current.regular13,
                                color = Gray_999999
                            )
                        },
                        supportingText = {
                            // 글자 수 카운팅 텍스트
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.BottomEnd
                            ) {
                                Text(
                                    text = "$textCount / 500",
                                    style = LocalTypography.current.regular11,
                                    color = Gray_999999
                                )
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledBorderColor = Gray_B9B9B9,
                            focusedBorderColor = Gray_B9B9B9,
                            unfocusedBorderColor = Gray_B9B9B9,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            unfocusedPlaceholderColor = Gray_999999,
                            focusedPlaceholderColor = Gray_999999
                        ),
                        shape = RoundedCornerShape(4.dp),
                    )

                    // 텍스트 필드가 나타날 때 자동으로 포커스
                    LaunchedEffect(isOtherReasonSelected) {
                        if (isOtherReasonSelected) {
                            focusRequester.requestFocus()
                        }
                    }

                    // 텍스트 필드에 포커스가 갔을 때 스크롤
                    LaunchedEffect(isTextFieldFocused) {
                        if (isTextFieldFocused) {
                            scrollState.animateScrollTo(scrollState.maxValue)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
            // 신고하기 버튼 - 활성화 조건에 따라 색상 및 클릭 가능 여부 설정
            Button(
                onClick = {
                    keyboardController?.hide()
                    // 신고 처리 로직
                    onReportComplete()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(47.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isButtonEnabled) Orange_FF7800 else Gray_B9B9B9,
                    disabledContainerColor = Gray_B9B9B9
                ),
                shape = RoundedCornerShape(11.dp),
                enabled = isButtonEnabled
            ) {
                Text(
                    text = "신고하기",
                    style = LocalTypography.current.medium15,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp)) // 키보드가 올라올 때 여분의 공간
        }
    }
}