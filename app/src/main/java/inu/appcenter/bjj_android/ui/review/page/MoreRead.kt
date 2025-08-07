package inu.appcenter.bjj_android.ui.review.page

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import inu.appcenter.bjj_android.ui.component.noRippleClickable
import inu.appcenter.bjj_android.ui.review.ReviewViewModel
import inu.appcenter.bjj_android.ui.review.component.ReviewItemCard
import inu.appcenter.bjj_android.ui.theme.paddings

private val BOTTOM_SPACER_HEIGHT = 28.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreReadScreen(
    onNavigateBack: () -> Unit,
    onNavigateToReviewDetail: (MyReviewDetailRes) -> Unit,
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
                        style = LocalTypography.current.bold18.copy(
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
                            .noRippleClickable { onNavigateBack() },
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
                        ReviewItemCard(
                            reviewItem = item,
                            onClick = {
                                onNavigateToReviewDetail(item)
                            }
                        )
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