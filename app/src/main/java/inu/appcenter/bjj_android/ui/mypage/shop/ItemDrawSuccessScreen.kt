package inu.appcenter.bjj_android.ui.mypage.shop

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.model.item.ItemType
import inu.appcenter.bjj_android.ui.mypage.MyPageViewModel
import inu.appcenter.bjj_android.ui.mypage.component.ShopBackground
import inu.appcenter.bjj_android.ui.theme.Gray_999999
import inu.appcenter.bjj_android.utils.ImageLoader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDrawSuccessScreen(
    myPageViewModel: MyPageViewModel,
    popBackStack: () -> Unit,
    onEquip: () -> Unit
) {
    val uiState by myPageViewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.drawnItem == null) return

    var itemTypeText1 by remember { mutableStateOf("") }
    var itemTypeText2 by remember { mutableStateOf("") }

    LaunchedEffect(
        uiState.drawnItem
    ) {
        if (uiState.drawnItem?.itemType == "CHARACTER") {
            itemTypeText1 = "캐릭터를"
            itemTypeText2 = "캐릭터는"
        } else {
            itemTypeText1 = "배경을"
            itemTypeText2 = "배경은"
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // 혹시 모를 빈 곳 대비
    ) {
        // ✅ 배경 최하단
        ShopBackground(modifier = Modifier.fillMaxSize())

        // ✅ Scaffold에 insets 무시 추가
        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.arrowback),
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier
                                .clickable {
                                    myPageViewModel.resetDrawState()
                                    popBackStack()
                                }
                                .padding(start = 21.dp)
                        )
                    },
                    title = {},
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                    )
                )
            },
            containerColor = Color.Transparent
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .padding(innerPadding),
            ) {
                ShopBackground(modifier = Modifier.fillMaxSize())
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
                        uiState.drawnItem?.let { item ->
                            // 타입에 따라 적절한 ImageLoader 사용
                            when(item.itemType.uppercase()) {
                                ItemType.CHARACTER.name -> {
                                    ImageLoader.CharacterItem(
                                        imageName = item.imageName,
                                        modifier = Modifier
                                    )
                                }
                                ItemType.BACKGROUND.name -> {
                                    ImageLoader.BackgroundItem(
                                        imageName = item.imageName,
                                        modifier = Modifier
                                    )
                                }
                                else -> {
                                    // 알 수 없는 타입일 경우 기본 이미지 표시
                                    ImageLoader.CharacterItem(
                                        imageName = null,
                                        modifier = Modifier
                                    )
                                }
                            }
                        }
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp
                        ),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(43.dp))

                            Text(
                                text = uiState.drawnItem?.itemName ?: "",
                                style = LocalTypography.current.semibold24.copy(color = Color.Black),
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            Text(
                                text = "뽑기를 해서 랜덤으로 ${itemTypeText1} 얻어요.\n뽑은 ${itemTypeText2} 7일의 유효기간이 있어요.",
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
                                    onClick = {
                                        myPageViewModel.resetDrawState()
                                        // 수정: 괄호 추가하여 함수 호출
                                        popBackStack()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(vertical = 14.dp, horizontal = 50.dp)
                                ) {
                                    Text(
                                        "닫기",
                                        color = Color.White
                                    )
                                }

                                Button(
                                    onClick = {
                                        // 아이템 타입에 따라 장착 처리
                                        myPageViewModel.equipDrawnItem()
                                        myPageViewModel.resetDrawState()
                                        // 수정: 괄호 추가하여 함수 호출
                                        onEquip()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7A00)),
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(vertical = 14.dp, horizontal = 50.dp)
                                ) {
                                    Text(
                                        "착용하기",
                                        color = Color.White
                                    )
                                }
                            }
                            Spacer(Modifier.height(40.dp))
                        }
                    }
                }
            }
        }
    }
}
