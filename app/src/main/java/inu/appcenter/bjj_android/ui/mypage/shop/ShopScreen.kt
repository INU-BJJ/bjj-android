package inu.appcenter.bjj_android.ui.mypage.shop

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import inu.appcenter.bjj_android.ui.mypage.MyPageViewModel
import inu.appcenter.bjj_android.ui.mypage.component.ShopBackground

@Composable
fun ShopScreen(
    myPageViewModel: MyPageViewModel,
    popBackStack: () -> Unit
) {
    val myPageUiState by myPageViewModel.uiState.collectAsStateWithLifecycle()
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ShopBackground(
            modifier = Modifier
                .fillMaxSize()
        )
        Column(

        ) {
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

                }
            )
        }

    }
}