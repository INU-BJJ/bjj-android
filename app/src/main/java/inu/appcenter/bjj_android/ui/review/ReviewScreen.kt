package inu.appcenter.bjj_android.ui.review

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.ui.component.noRippleClickable
import inu.appcenter.bjj_android.ui.navigate.AllDestination
import inu.appcenter.bjj_android.ui.navigate.AppBottomBar
import inu.appcenter.bjj_android.ui.review.reviewPagePart.ReviewFrameScreen
import inu.appcenter.bjj_android.ui.theme.Gray_D9D9D9

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(navController: NavHostController, reviewViewModel: ReviewViewModel) {
    val scrollState = rememberScrollState()
    val reviewUiState by reviewViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        reviewViewModel.getMyReviews()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.review_page_title),
                        style = LocalTypography.current.semibold18.copy(
                            letterSpacing = 0.13.sp,
                            lineHeight = 15.sp
                        )
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        },
        // 밑에 버튼에 icon 넣는법 찾기
        floatingActionButton = {
            Icon(
                painter = painterResource(id = R.drawable.writeplusbutton),
                contentDescription = stringResource(R.string.write_review_button),
                tint = Color.Unspecified,
                modifier = Modifier
                    .noRippleClickable { navController.navigate(AllDestination.ReviewWrite.route) },
            )
        },
        bottomBar = { AppBottomBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(innerPadding)
                .verticalScroll(scrollState)
        ) {
            Spacer(Modifier.height(20.dp))

            if (reviewUiState.reviews?.myReviewDetailList.isNullOrEmpty()) {
                Spacer(Modifier.height(196.dp))
                Column(
                    modifier = Modifier
                        .padding(horizontal = 102.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(contentAlignment = Alignment.Center)
                    {
                        Icon(
                            painter = painterResource(id = R.drawable.cautiontriangle),
                            contentDescription = stringResource(R.string.warning_icon),
                            tint = Gray_D9D9D9,
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.caution), // 위에 겹쳐지는 벡터 이미지
                            contentDescription = stringResource(R.string.warning_icon),
                            tint = Color.White,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 21.75.dp)
                        )
                    }
                    Spacer(Modifier.height(20.25.dp))
                    Text(
                        text = stringResource(R.string.add_review_message),
                        color = Gray_D9D9D9,
                        style = LocalTypography.current.bold18.copy(
                            letterSpacing = 0.13.sp,
                            lineHeight = 15.sp,
                        )
                    )
                }
            } else {
                reviewUiState.reviews?.myReviewDetailList?.forEach { (restaurantName) ->
                    ReviewFrameScreen(
                        navController,
                        restaurantName,
                        reviewViewModel
                    )
                }
            }
        }
    }
}