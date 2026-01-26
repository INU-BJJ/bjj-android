package inu.appcenter.bjj_android.feature.profile.presentation.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.model.item.ItemType
import inu.appcenter.bjj_android.shared.theme.Gray_999999
import inu.appcenter.bjj_android.shared.theme.White_FFFFFF
import inu.appcenter.bjj_android.shared.theme.Yellow_FFEB62

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDrawDialog(
    itemType: ItemType,
    onDismiss: () -> Unit,
    onDraw: () -> Unit
) {
    val cost = if (itemType == ItemType.CHARACTER) 50 else 100

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            color = White_FFFFFF
        ) {
            Column(
                modifier = Modifier.padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // 타이틀
                Text(
                    text = when(itemType) {
                        ItemType.CHARACTER -> "캐릭터 뽑기"
                        ItemType.BACKGROUND -> "배경 뽑기"
                    },
                    style = LocalTypography.current.bold18.copy(color = Color.Black),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                // 설명 텍스트
                Text(
                    text = when(itemType) {
                        ItemType.CHARACTER -> "뽑기를 해서 랜덤으로 캐릭터를 얻어요.\n뽑은 캐릭터는 7일의 유효기간이 있어요."
                        ItemType.BACKGROUND -> "뽑기를 해서 랜덤으로 배경을 얻어요.\n뽑은 배경은 7일의 유효기간이 있어요."
                    },
                    style = LocalTypography.current.regular13.copy(color = Gray_999999),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(28.dp))

                // 포인트 버튼
                Box(
                    modifier = Modifier
                        .background(
                            color = Yellow_FFEB62,
                            shape = RoundedCornerShape(5.dp)
                        )
                        .clickable { onDraw() }
                        .padding(vertical = 6.dp, horizontal = 15.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.point),
                            contentDescription = stringResource(R.string.point),
                            modifier = Modifier
                                .width(26.dp)
                                .height(26.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$cost",
                            style = LocalTypography.current.medium15.copy(fontSize = 18.sp, color = Color.Black)
                        )
                    }
                }
            }
        }
    }
}

