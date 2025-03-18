package inu.appcenter.bjj_android.ui.mypage.dialog

import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.ui.mypage.component.ShopBackground
import inu.appcenter.bjj_android.ui.theme.Gray_999999
import inu.appcenter.bjj_android.utils.ImageLoader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDrawSuccessDialog(
    imageName: String,
    itemName: String,
    onDismiss: () -> Unit,
    onEquip: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false // ✅ 꽉 채우기
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White) // 혹시 모를 빈 곳 대비
        ) {
            // ✅ 배경 최하단
            ShopBackground(modifier = Modifier.fillMaxSize())

            // ✅ Scaffold에 insets 무시 추가
            Scaffold(
                contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0, 0, 0, 0),
                topBar = {
                    TopAppBar(
                        navigationIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.arrowback),
                                contentDescription = null,
                                tint = Color.Black
                            )
                        },
                        title = {},
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                        )
                    )
                },
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent)
                        .padding(innerPadding),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Transparent)
                    ) {
                        // 아이템 이미지
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .background(Color.Transparent),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            ImageLoader.ReviewImage(
                                imageName = imageName,
                                modifier = Modifier
                                    .width(136.dp)
                                    .height(165.dp)
                            )
                        }

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Spacer(modifier = Modifier.height(43.dp))

                                Text(
                                    text = itemName,
                                    style = LocalTypography.current.semibold24.copy(color = Color.Black),
                                )

                                Spacer(modifier = Modifier.height(18.dp))

                                Text(
                                    text = "뽑기를 해서 랜덤으로 캐릭터를 얻어요.\n뽑은 캐릭터는 7일의 유효기간이 있어요.",
                                    style = LocalTypography.current.medium15,
                                    color = Gray_999999,
                                )

                                Spacer(modifier = Modifier.height(43.dp))

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier
                                        .padding(horizontal = 21.dp)
                                        .fillMaxWidth()
                                ) {
                                    Button(
                                        onClick = onDismiss,
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("닫기", color = Color.White)
                                    }

                                    Button(
                                        onClick = onEquip,
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7A00)),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("착용하기", color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
