package inu.appcenter.bjj_android.ui.menudetail.common

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.model.review.ReviewDetailRes
import inu.appcenter.bjj_android.model.todaydiet.TodayDietRes
import inu.appcenter.bjj_android.ui.component.error.ErrorHandler
import inu.appcenter.bjj_android.ui.component.LoadingIndicator
import inu.appcenter.bjj_android.ui.menudetail.review.DynamicReviewImages
import inu.appcenter.bjj_android.ui.menudetail.review.StarRatingCalculator
import inu.appcenter.bjj_android.ui.navigate.AllDestination
import inu.appcenter.bjj_android.ui.review.ReviewViewModel
import inu.appcenter.bjj_android.ui.review.component.formatter
import inu.appcenter.bjj_android.ui.theme.Gray_999999
import inu.appcenter.bjj_android.ui.theme.Gray_D9D9D9
import inu.appcenter.bjj_android.ui.theme.Gray_F6F6F8
import inu.appcenter.bjj_android.ui.theme.Orange_FF7800

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewDetailScreen(
    navController: NavHostController,
    reviewId: Long,
    reviewViewModel: ReviewViewModel,
    currentMenu: TodayDietRes? = null
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val uiState by reviewViewModel.reviewDetailUiState.collectAsState()

    LoadingIndicator(reviewViewModel)
    ErrorHandler(reviewViewModel)

    LaunchedEffect(key1 = true) {
        reviewViewModel.getReviewDetail(reviewId)
    }

    val review = uiState.reviewDetail ?: return

    Column(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize()
    ) {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(Color.White),
            title = {
                Text(
                    text = "리뷰 상세",
                    style = LocalTypography.current.bold18.copy(
                        letterSpacing = 0.13.sp,
                        lineHeight = 15.sp
                    ),
                    color = Color.Black,
                )
            },
            navigationIcon = {
                Icon(
                    modifier = Modifier
                        .offset(x = 20.dp, y = 4.5.dp)
                        .clickable {
                            navController.popBackStack()
                        },
                    painter = painterResource(id = R.drawable.leftarrow),
                    contentDescription = "뒤로 가기",
                    tint = Color.Black
                )
            },
            actions = {
                Icon(
                    modifier = Modifier
                        .offset(x = -(26).dp)
                        .clickable { showBottomSheet = true },
                    painter = painterResource(id = R.drawable.pencil),
                    contentDescription = "삭제 하기",
                    tint = Color.Black
                )
            }
        )

        if (showBottomSheet) {
            ModalBottomSheet(
                containerColor = Color.White,
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
                dragHandle = { }
            ) {
                Column(modifier = Modifier) {
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "신고하기",
                        color = Color.Black,
                        modifier = Modifier
                            .clickable { showBottomSheet = false }
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp)
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        text = "차단하기",
                        color = Color.Black,
                        modifier = Modifier
                            .clickable { showBottomSheet = false }
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp)
                    )
                    Spacer(modifier = Modifier.height(63.5.dp))
                }
            }
        }

        // 리뷰 컨텐츠
        ReviewContent(review = review, reviewId = reviewId, navController = navController, currentMenu = currentMenu)
    }
}

@Composable
private fun ReviewContent(
    review: ReviewDetailRes,
    reviewId: Long,
    navController: NavHostController,
    currentMenu: TodayDietRes? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 29.5.dp)
    ) {
        Spacer(Modifier.height(13.dp))

        // 프로필 섹션
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(41.dp)
                    .background(Gray_D9D9D9, shape = CircleShape)
            )

            Spacer(Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = review.memberNickname,
                    style = LocalTypography.current.bold15.copy(
                        letterSpacing = 0.13.sp,
                        lineHeight = 15.sp
                    ),
                    color = Color.Black
                )
                Spacer(Modifier.height(3.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    StarRatingCalculator(rating = review.rating.toFloat())
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = review.createdDate.formatter(),
                        style = LocalTypography.current.regular13.copy(
                            letterSpacing = 0.13.sp,
                            lineHeight = 17.sp
                        ),
                        color = Gray_999999
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.thumbs),
                    contentDescription = "좋아요",
                    tint = Orange_FF7800
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    text = review.likeCount.toString(),
                    style = LocalTypography.current.regular11.copy(
                        letterSpacing = 0.13.sp,
                        lineHeight = 15.sp
                    ),
                    color = Color.Black
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        Text(
            text = review.comment,
            style = LocalTypography.current.medium13.copy(
                letterSpacing = 0.13.sp,
                lineHeight = 17.sp
            ),
            color = Color.Black
        )

        Spacer(Modifier.height(12.dp))

        if (review.imageNames.isNotEmpty()) {
            DynamicReviewImages(
                reviewImages = review.imageNames,
                onClick = { imageList, index ->
                    navController.navigate(
                        AllDestination.MenuDetailReviewDetailPush.createRoute(
                            imageList = review.imageNames,
                            index = index,
                            reviewId = reviewId,
                            fromReviewDetail = true
                        )
                    )
                }
            )
        }

        Spacer(Modifier.height(12.dp))

        Row {
            // 메인메뉴 비교 - 현재 메뉴가 있고 메인메뉴 이름이 일치하는 경우
            val isMainMenuSame = currentMenu != null && review.mainMenuName == currentMenu.mainMenuName

            Box(
                modifier = Modifier
                    .background(
                        if (isMainMenuSame) Orange_FF7800 else Gray_F6F6F8,
                        RoundedCornerShape(3.dp)
                    )
                    .padding(horizontal = 7.dp, vertical = 5.dp)
            ) {
                Text(
                    text = review.mainMenuName,
                    style = LocalTypography.current.medium11.copy(
                        letterSpacing = 0.13.sp,
                        lineHeight = 15.sp
                    ),
                    color = Color.Black
                )
            }

            Spacer(Modifier.width(5.dp))

            // 서브메뉴 비교 - 현재 메뉴가 있고 서브메뉴 이름이 일치하는 경우
            val isSubMenuSame = currentMenu != null &&
                    currentMenu.restMenu?.split(" ")?.firstOrNull() == review.subMenuName

            Box(
                modifier = Modifier
                    .background(
                        if (isSubMenuSame) Orange_FF7800 else Gray_F6F6F8,
                        RoundedCornerShape(3.dp)
                    )
                    .padding(horizontal = 7.dp, vertical = 5.dp)
            ) {
                Text(
                    text = review.subMenuName,
                    style = LocalTypography.current.medium11.copy(
                        letterSpacing = 0.13.sp,
                        lineHeight = 15.sp
                    ),
                    color = Color.Black
                )
            }
        }
    }
}