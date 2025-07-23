package inu.appcenter.bjj_android.ui.mypage.shop

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.ui.theme.Gray_999999
import inu.appcenter.bjj_android.ui.theme.White_FFFFFF

@Composable
fun ShopInfoDialog(
    onDismiss: () -> Unit
) {
    Dialog(
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
        onDismissRequest = onDismiss
    ) {
        Box(
            modifier = Modifier
                .padding(top = 25.dp)
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    onDismiss()
                },
            contentAlignment = Alignment.TopCenter
        ) {
            Card(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                    },
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = White_FFFFFF
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "상점 정보",
                        style = LocalTypography.current.bold18.copy(
                            lineHeight = 15.sp,
                            letterSpacing = 0.13.sp,
                            color = Color.Black
                        )
                    )
                    Spacer(Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Top
                    ) {
                        // 포인트 획득 섹션
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "포인트 획득",
                                style = LocalTypography.current.medium13.copy(
                                    lineHeight = 17.sp,
                                    letterSpacing = 0.13.sp,
                                    color = Color.Black
                                ),
                                textAlign = TextAlign.Center
                            )

                            Spacer(Modifier.height(8.dp))

                            PointInfoRow(
                                label = "사진 있는 리뷰",
                                value = "100 p"
                            )

                            Spacer(Modifier.height(4.dp))

                            PointInfoRow(
                                label = "사진 없는 리뷰",
                                value = "50 p"
                            )
                        }

                        // 구분선
                        VerticalDivider(
                            thickness = 1.dp,
                            color = Gray_999999,
                            modifier = Modifier
                                .height(80.dp)
                                .padding(horizontal = 16.dp)
                        )

                        // 캐릭터 확률 섹션
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "캐릭터 확률",
                                style = LocalTypography.current.medium13.copy(
                                    lineHeight = 17.sp,
                                    letterSpacing = 0.13.sp,
                                    color = Color.Black
                                ),
                                textAlign = TextAlign.Center
                            )

                            Spacer(Modifier.height(8.dp))

                            ProbabilityInfoRow(
                                label = "흔함",
                                value = "75 %"
                            )

                            Spacer(Modifier.height(4.dp))

                            ProbabilityInfoRow(
                                label = "보통",
                                value = "20 %"
                            )

                            Spacer(Modifier.height(4.dp))

                            ProbabilityInfoRow(
                                label = "희귀",
                                value = "5 %"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PointInfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = LocalTypography.current.medium13.copy(
                lineHeight = 17.sp,
                letterSpacing = 0.13.sp,
                color = Gray_999999
            )
        )

        Text(
            text = value,
            style = LocalTypography.current.medium13.copy(
                lineHeight = 17.sp,
                letterSpacing = 0.13.sp,
                color = Gray_999999
            )
        )
    }
}

@Composable
private fun ProbabilityInfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = LocalTypography.current.medium13.copy(
                lineHeight = 17.sp,
                letterSpacing = 0.13.sp,
                color = Gray_999999
            )
        )

        Text(
            text = value,
            style = LocalTypography.current.medium13.copy(
                lineHeight = 17.sp,
                letterSpacing = 0.13.sp,
                color = Gray_999999
            )
        )
    }
}