package inu.appcenter.bjj_android.ui.review.reviewPagePart

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import inu.appcenter.bjj_android.ui.navigate.AllDestination
import inu.appcenter.bjj_android.ui.review.ReviewViewModel
import inu.appcenter.bjj_android.ui.review.toolsAndUtils.formatter
import inu.appcenter.bjj_android.ui.theme.Gray_999999
import inu.appcenter.bjj_android.ui.theme.Gray_B9B9B9
import inu.appcenter.bjj_android.ui.theme.Gray_F6F6F8
import inu.appcenter.bjj_android.ui.theme.Orange_FF7800

@Composable
fun ReviewFrameScreen(
    navController: NavHostController,
    selectRestaurantName: String,
    reviewViewModel: ReviewViewModel
) {
    val reviewUiState by reviewViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 9.dp)
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(63.dp)
                        .border(0.5.dp, Gray_B9B9B9, shape = RoundedCornerShape(3.dp))
                        .clickable {
                            reviewViewModel.setSelectedReviewDetail(item)
                            navController.navigate(AllDestination.ReviewDetail.route)
                        }
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 9.dp)
                            .fillMaxWidth()
                    ) {
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = item.mainMenuName.toString(),//임시
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
                                text = item.createdDate.formatter(),
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
        }
    }
    Spacer(Modifier.height(24.dp))

    Box(
        modifier = Modifier
            .height(7.dp)
            .background(color = Gray_F6F6F8)
            .fillMaxWidth()
    )
    Spacer(Modifier.height(22.dp))
}