package inu.appcenter.bjj_android.ui.mypage.shop

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import inu.appcenter.bjj_android.ui.mypage.MyPageViewModel
import inu.appcenter.bjj_android.ui.mypage.component.ShopBackground
import inu.appcenter.bjj_android.ui.mypage.dialog.ItemDrawDialog

@Composable
fun ShopScreen(
    myPageViewModel: MyPageViewModel,
    popBackStack: () -> Unit,
    navigateToDrawSuccess: () -> Unit // 뽑기 성공 화면으로 이동하는 함수 추가
) {
    val myPageUiState by myPageViewModel.uiState.collectAsStateWithLifecycle()

    // 뽑기 다이얼로그 상태
    var showDrawDialog by remember { mutableStateOf(false) }

    LaunchedEffect(
        key1 = true
    ) {
        myPageViewModel.getMyPageInfo()
        myPageViewModel.getAllItemsInfo()
    }

    // 뽑기 성공 상태가 변경될 때 ItemDrawSuccessScreen으로 이동
    LaunchedEffect(myPageUiState.isDrawSuccess) {
        if (myPageUiState.isDrawSuccess && myPageUiState.drawnItem != null) {
            // 뽑기 성공 화면으로 네비게이션
            navigateToDrawSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ShopBackground(
            modifier = Modifier
                .fillMaxSize()
        )
        Column {
            ShopTopContent(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 10.dp,
                        start = 18.dp,
                        end = 18.dp
                    ),
                point = myPageUiState.point,
                popBackStack = {
                    popBackStack()
                }
            )
            Spacer(Modifier.height(30.dp))
            DrawMachine(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    showDrawDialog = true
                }
            )
            Spacer(Modifier.height(37.dp))
            ItemFrame(
                modifier = Modifier
                    .fillMaxWidth(),
                selectedCategory = myPageUiState.selectedCategory,
                itemList = myPageUiState.items,
                selectCategory = { myPageViewModel.selectCategory(it) },
                onItemClick = { myPageViewModel.wearItem(it) }
            )
        }

        // 뽑기 다이얼로그
        if (showDrawDialog) {
            ItemDrawDialog(
                itemType = myPageUiState.selectedCategory,
                onDismiss = {
                    showDrawDialog = false
                },
                onDraw = {
                    // 뽑기 로직 호출
                    myPageViewModel.drawItem(myPageUiState.selectedCategory)

                    // 다이얼로그 닫기
                    showDrawDialog = false
                }
            )
        }
    }
}