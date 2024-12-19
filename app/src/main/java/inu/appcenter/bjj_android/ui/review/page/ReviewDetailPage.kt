package inu.appcenter.bjj_android.ui.review.page


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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.ui.menudetail.review.StarRatingCalculator
import inu.appcenter.bjj_android.ui.navigate.AllDestination
import inu.appcenter.bjj_android.ui.review.ReviewViewModel
import inu.appcenter.bjj_android.ui.theme.Gray_999999
import inu.appcenter.bjj_android.ui.theme.Gray_D9D9D9
import inu.appcenter.bjj_android.ui.theme.Gray_F6F6F8
import inu.appcenter.bjj_android.ui.theme.Orange_FF7800
import inu.appcenter.bjj_android.ui.theme.Red_FF3916


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewDetailScreen(navController: NavHostController, reviewViewModel : ReviewViewModel) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val reviewUiState by reviewViewModel.uiState.collectAsState()
    val reviewDetail = reviewUiState.selectedReviewDetail

    LaunchedEffect(reviewDetail) {
        reviewDetail?.imageNames?.let {
            reviewViewModel.setImageNames(it)
        }
    }

    Column(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize()
    ) {
        // 상단바
        CenterAlignedTopAppBar(colors = TopAppBarDefaults.centerAlignedTopAppBarColors(Color.White),
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
                        .offset(x = 19.4.dp, y = 4.5.dp)
                        .clickable {
                            //reviewViewModel.resetSelectedReviewDetail() // 상태 초기화
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
            })
        if (showBottomSheet) {
            ModalBottomSheet(
                containerColor = Color.White,
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
                dragHandle = { /* 드래그 핸들을 빈 상태로 만듦, 즉 핸들을 없앰 */ }) {
                // Sheet content
                Column(
                    modifier = Modifier
                ) {
                    Spacer(modifier = Modifier.height(31.5.dp))
                    Text(text = "삭제하기",
                        color = Red_FF3916,
                        modifier = Modifier
                            .clickable {
                                reviewViewModel.deleteReview(
                                    reviewId = reviewDetail?.reviewId ?: -1, // null 일때 -1을 주어 지워지지 않게함
                                    onSuccess = {
                                        // 삭제 성공 후 처리 (예: 이전 화면으로 이동)
                                        navController.popBackStack()
                                    }
                                )
                                showBottomSheet = false
                            }
                            .fillMaxWidth()
                            .padding(horizontal = 31.dp))
                    Spacer(modifier = Modifier.height(64.5.dp))
                }
            }
        }

        Spacer(Modifier.height(13.dp))

        Column(
            modifier = Modifier
                .padding(horizontal = 29.5.dp)
                .fillMaxWidth(),
        ) {

            // 본문: 유저 프로필, 이름, 별점, 날짜, 좋아요 버튼 및 숫자
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                // 유저 프로필 (회색 동그라미)
                Box(
                    modifier = Modifier
                        .size(41.dp)
                        .background(Gray_D9D9D9, shape = CircleShape)
                )

                Spacer(Modifier.width(10.dp))

                // 유저 정보 (이름, 별점, 날짜)
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = reviewDetail?.memberNickname ?: "잘못된 닉네임",
                        style = LocalTypography.current.bold15.copy(
                            letterSpacing = 0.13.sp,
                            lineHeight = 15.sp
                        ),
                        color = Color.Black
                    )
                    Spacer(Modifier.height(3.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StarRatingCalculator(rating = reviewDetail?.rating?.toFloat() ?: 0f)
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = reviewDetail?.createdDate ?: "2024-00-00",
                            style = LocalTypography.current.regular13.copy(
                                letterSpacing = 0.13.sp,
                                lineHeight = 17.sp,
                                color = Color(0xFF999999)
                            ),
                            color = Gray_999999
                        )
                    }
                }

                // 좋아요 버튼 및 숫자
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        modifier = Modifier,
                        painter = painterResource(id = R.drawable.thumbs),
                        contentDescription = "좋아요",
                        tint = Orange_FF7800
                    )
                    Spacer(Modifier.height(3.dp))
                    Text(
                        text = reviewDetail?.likeCount.toString(),
                        style = LocalTypography.current.regular11.copy(
                            letterSpacing = 0.13.sp,
                            lineHeight = 15.sp
                        ),
                        color = Color.Black
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // 텍스트 영역
                Text(
                    text = reviewDetail?.comment ?: "잘못된 코멘트",
                    style = LocalTypography.current.medium13.copy(
                        letterSpacing = 0.13.sp,
                        lineHeight = 17.sp
                    ),
                    color = Color.Black
                )

                Spacer(Modifier.height(12.dp))

                // 이미지 영역
                if (reviewUiState.imageNames.isNotEmpty()) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(reviewUiState.imageNames) { imageName ->
                            AsyncImage(
                                model = "https://bjj.inuappcenter.kr/images/review/$imageName",
                                contentDescription = "Food Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .height(250.dp)
                                    .width(301.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable {
                                        reviewViewModel.selectImageName(imageName)
                                        navController.navigate(AllDestination.ReviewDetailPush.route) }
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(12.dp))

            Row {
                Box(
                    modifier = Modifier
                        .background(
                            color = Orange_FF7800, shape = RoundedCornerShape(3.dp)
                        )
                        .padding(horizontal = 7.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = reviewDetail?.mainMenuName ?: "잘못된 메인메뉴 이름",
                        style = LocalTypography.current.medium11.copy(
                            letterSpacing = 0.13.sp,
                            lineHeight = 15.sp
                        ),
                        color = Color.Black
                    )
                }
                Spacer(Modifier.width(5.dp))

                Box(
                    modifier = Modifier
                        .background(
                            color = Gray_F6F6F8, shape = RoundedCornerShape(3.dp)
                        )
                        .padding(horizontal = 7.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = reviewDetail?.subMenuName ?: "잘못된 서브메뉴 이름",
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
}