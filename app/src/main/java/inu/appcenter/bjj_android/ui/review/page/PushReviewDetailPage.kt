package inu.appcenter.bjj_android.ui.review.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.ui.navigate.AllDestination
import inu.appcenter.bjj_android.ui.review.ReviewViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PushReviewDetailScreen(navController: NavController, reviewViewModel : ReviewViewModel) {
    val reviewUiState by reviewViewModel.uiState.collectAsState()
    val imageName = reviewUiState.selectedImageName

    Column(
        modifier = Modifier
            .background(color = Color.Black)
            .fillMaxSize()
    ) {
        // 상단바
        CenterAlignedTopAppBar(colors = TopAppBarDefaults.centerAlignedTopAppBarColors(Color.Black),
            title = {
                Text(
                    text = "1/2",
                    style = LocalTypography.current.medium15.copy(
                        letterSpacing = 0.13.sp,
                        lineHeight = 15.sp
                    ),
                    color = Color.White
                )
            },
            navigationIcon = {
                Icon(
                    modifier = Modifier
                        .offset(x = 19.4.dp, y = 4.5.dp)
                        .clickable { navController.popBackStack() },
                    painter = painterResource(id = R.drawable.leftarrow),
                    contentDescription = "뒤로 가기",
                    tint = Color.White
                )

            },
            actions = {
                Icon(
                    modifier = Modifier
                        .offset(x = -(12.1).dp)
                        .clickable { navController.popBackStack() },
                    painter = painterResource(id = R.drawable.xbutton),
                    contentDescription = "뒤로 가기",
                    tint = Color.White
                )
            })
        Spacer(Modifier.height(76.1.dp))

        AsyncImage(
            model = "https://bjj.inuappcenter.kr/images/review/${imageName ?: ""}",
            contentDescription = "Selected Food Image",
            modifier = Modifier
                .height(480.dp)
                .width(360.dp)
        )
        Spacer(Modifier.height(76.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 20.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Icon(
                painter = painterResource(id = R.drawable.seemainbutton),
                contentDescription = "본문 보기",
                tint = Color.White,
                modifier = Modifier.clickable {navController.navigate(AllDestination.ReviewDetail.route) }
            )
        }

    }
}