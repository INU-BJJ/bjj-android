package inu.appcenter.bjj_android.ui.review.reviewPagePart

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.ui.component.HorizontalDivider
import inu.appcenter.bjj_android.ui.navigate.AllDestination
import inu.appcenter.bjj_android.ui.review.ReviewViewModel
import inu.appcenter.bjj_android.ui.review.component.ReviewItemCard
import inu.appcenter.bjj_android.ui.theme.Gray_999999
import inu.appcenter.bjj_android.ui.theme.paddings

@Composable
fun ReviewFrameScreen(
    navController: NavHostController,
    selectRestaurantName: String,
    reviewViewModel: ReviewViewModel
) {
    val reviewUiState by reviewViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .padding(horizontal = MaterialTheme.paddings.medium)
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = MaterialTheme.paddings.xlarge - MaterialTheme.paddings.medium)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectRestaurantName,
                style = LocalTypography.current.semibold15.copy(
                    letterSpacing = 0.13.sp,
                    lineHeight = 18.sp,
                ),
                color = Gray_999999
            )
            if ((reviewUiState.reviews?.myReviewDetailList?.get(selectRestaurantName)?.size
                    ?: -1) >= 3
            ) {
                Text(
                    modifier = Modifier.clickable {
                        reviewViewModel.setSelectedRestaurant(selectRestaurantName)
                        navController.navigate(AllDestination.ReviewMore.route)
                    },
                    text = stringResource(R.string.view_more),
                    style = LocalTypography.current.regular11.copy(
                        letterSpacing = 0.13.sp,
                        lineHeight = 15.sp
                    ),
                    color = Gray_999999
                )
            }
        }
        Spacer(Modifier.height(7.dp))

        Column {
            reviewUiState.reviews?.myReviewDetailList?.get(selectRestaurantName)?.take(3)?.forEach { item ->
                ReviewItemCard(
                    reviewItem = item,
                    onClick = {
                        reviewViewModel.setSelectedReviewDetail(item)
                        navController.navigate(AllDestination.ReviewDetail.route)
                    }
                )
                Spacer(Modifier.height(10.dp))
            }
        }
    }
    Spacer(Modifier.height(24.dp))
    HorizontalDivider()
    Spacer(Modifier.height(22.dp))
}