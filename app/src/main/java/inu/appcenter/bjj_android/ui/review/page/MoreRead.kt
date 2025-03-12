package inu.appcenter.bjj_android.ui.review.page

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.model.review.MyReviewDetailRes
import inu.appcenter.bjj_android.ui.review.ReviewViewModel
import inu.appcenter.bjj_android.ui.theme.Gray_999999
import inu.appcenter.bjj_android.ui.theme.Gray_B9B9B9
import inu.appcenter.bjj_android.ui.theme.Orange_FF7800
import inu.appcenter.bjj_android.ui.theme.paddings

private val BOTTOM_SPACER_HEIGHT = 28.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreReadScreen(
    onNavigateBack: () -> Unit,
    onNavigateToReviewDetail: (MyReviewDetailRes) -> Unit,  // 타입 명시
    reviewViewModel: ReviewViewModel
) {
    val reviewUiState by reviewViewModel.uiState.collectAsState()
    val selectedRestaurant = reviewUiState.selectedRestaurant

    LaunchedEffect(selectedRestaurant) {
        selectedRestaurant?.let {
            reviewViewModel.getMoreReviewsByCafeteria(it)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = reviewUiState.selectedRestaurant ?: "",
                        style = LocalTypography.current.semibold18.copy(
                            lineHeight = 15.sp,
                            letterSpacing = 0.13.sp,
                        )
                    )
                },
                navigationIcon = {
                    Icon(
                        modifier = Modifier
                            .padding(start = MaterialTheme.paddings.topBarPadding - MaterialTheme.paddings.iconOffset)
                            .offset(y = MaterialTheme.paddings.iconOffset)
                            .clickable { onNavigateBack() },
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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(innerPadding)
                .padding(horizontal = MaterialTheme.paddings.medium, vertical = MaterialTheme.paddings.large)
        ) {
            LazyColumn(
                userScrollEnabled = true
            ) {
                reviewUiState.reviewsChoiceByRestaurant?.myReviewDetailList?.let {
                    items(it) { item ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(63.dp)
                                .border(0.5.dp, Gray_B9B9B9, shape = RoundedCornerShape(3.dp))
                                .clickable {
                                    onNavigateToReviewDetail(item)
                                }
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 9.dp)
                                    .fillMaxWidth()
                            ) {
                                Spacer(Modifier.height(12.dp))
                                Text(
                                    text = item.mainMenuName,
                                    style = LocalTypography.current.semibold15.copy(
                                        letterSpacing = 0.13.sp,
                                        lineHeight = 18.sp,
                                    ),
                                    color = Color.Black
                                )
                                Spacer(Modifier.height(5.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = item.createdDate.format(),
                                        style = LocalTypography.current.regular13.copy(
                                            letterSpacing = 0.13.sp,
                                            lineHeight = 17.sp,
                                        ),
                                        color = Gray_999999
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painter = painterResource(R.drawable.star),
                                            contentDescription = stringResource(R.string.full_star_description),
                                            tint = Orange_FF7800
                                        )
                                        Spacer(Modifier.width(4.dp))
                                        Text(
                                            text = item.rating.toString(),
                                            style = LocalTypography.current.semibold15.copy(
                                                letterSpacing = 0.13.sp,
                                                lineHeight = 18.sp,
                                            ),
                                            color = Color.Black
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(Modifier.height(10.dp))
                    }

                    item {
                        LoadMoreButton(
                            isVisible = reviewUiState.reviewsChoiceByRestaurant?.lastPage == false,
                            onLoadMore = {
                                if (selectedRestaurant != null) {
                                    reviewViewModel.getMoreReviewsByCafeteria(selectedRestaurant)
                                }
                            }
                        )
                    }
                }
            }

        }
    }
}

@Composable
private fun LoadMoreButton(
    isVisible: Boolean,
    onLoadMore: () -> Unit
) {
    if (!isVisible) return

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedButton(
            onClick = onLoadMore,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black),
            border = BorderStroke(width = 1.dp, color = Color.Black)
        ) {
            Text(text = stringResource(R.string.view_more))
        }
        Spacer(Modifier.height(BOTTOM_SPACER_HEIGHT))
    }
}