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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.ui.component.noRippleClickable
import inu.appcenter.bjj_android.ui.menudetail.review.StarRatingCalculator
import inu.appcenter.bjj_android.ui.review.ReviewViewModel
import inu.appcenter.bjj_android.ui.review.component.DynamicReviewDetailImages
import inu.appcenter.bjj_android.ui.review.component.formatter
import inu.appcenter.bjj_android.ui.theme.Gray_999999
import inu.appcenter.bjj_android.ui.theme.Gray_D9D9D9
import inu.appcenter.bjj_android.ui.theme.Gray_F6F6F8
import inu.appcenter.bjj_android.ui.theme.Orange_FF7800
import inu.appcenter.bjj_android.ui.theme.Red_FF3916
import inu.appcenter.bjj_android.ui.theme.paddings
import inu.appcenter.bjj_android.utils.ImageLoader

private val ACTIONICONPADDING = 26.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewDetailScreen(
    onNavigateBack: () -> Unit,
    onNavigateToImageDetail: (Int) -> Unit,
    reviewViewModel: ReviewViewModel
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val reviewUiState by reviewViewModel.uiState.collectAsState()
    val reviewDetail = reviewUiState.selectedReviewDetail

    LaunchedEffect(reviewDetail) {
        val images = reviewDetail?.imageNames ?: emptyList()
        reviewViewModel.setImageNames(images)
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            containerColor = Color.White,
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            dragHandle = { /* 드래그 핸들을 빈 상태로 만듦, 즉 핸들을 없앰 */ }
        ) {
            // Sheet content
            Column {
                Spacer(modifier = Modifier.height(31.5.dp))
                Text(
                    text = stringResource(id = R.string.delete_text),
                    color = Red_FF3916,
                    modifier = Modifier
                        .clickable {
                            reviewViewModel.deleteReview(
                                reviewId = reviewDetail?.reviewId ?: -1,
                                onSuccess = {
                                    // 삭제 성공 후 이전 화면으로 이동
                                    onNavigateBack()
                                }
                            )
                            showBottomSheet = false
                        }
                        .fillMaxWidth()
                        .padding(horizontal = 31.dp)
                )
                Spacer(modifier = Modifier.height(64.5.dp))
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.review_detail_title),
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
                actions = {
                    Icon(
                        painter = painterResource(id = R.drawable.pencil),
                        contentDescription = stringResource(id = R.string.delete_description),
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .padding(end = ACTIONICONPADDING)
                            .noRippleClickable { showBottomSheet = true },
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black,
                    actionIconContentColor = Color.Black
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(innerPadding)
                .padding(vertical = MaterialTheme.paddings.small)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 29.5.dp)
                    .fillMaxWidth()
            ) {
                // 본문: 유저 프로필, 이름, 별점, 날짜, 좋아요 버튼 및 숫자
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 유저 프로필
                    ImageLoader.ProfileImage(
                        imageName = reviewDetail?.memberImageName,
                        modifier = Modifier.size(41.dp),
                        shape = CircleShape
                    )

                    Spacer(Modifier.width(10.dp))

                    // 유저 정보 (이름, 별점, 날짜)
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = reviewDetail?.memberNickname
                                ?: stringResource(id = R.string.invalid_nickname),
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
                                text = reviewDetail?.createdDate?.formatter()
                                    ?: stringResource(id = R.string.default_date),
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
                            contentDescription = stringResource(id = R.string.like_description),
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

                // 텍스트 영역
                Text(
                    text = reviewDetail?.comment ?: stringResource(id = R.string.invalid_comment),
                    style = LocalTypography.current.medium13.copy(
                        letterSpacing = 0.13.sp,
                        lineHeight = 17.sp
                    ),
                    color = Color.Black
                )
            } // 여기서 Column(좌우 29.5dp) 종료

            Spacer(Modifier.height(12.dp))

            // 이미지 영역
            if (reviewUiState.imageNames.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 29.5.dp) // 왼쪽만 29.5dp
                ) {
                    DynamicReviewDetailImages(
                        imageNames = reviewUiState.imageNames,
                        onImageClick = { index ->
                            // 클릭 콜백
                            onNavigateToImageDetail(index)
                        }
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 29.5.dp)
            ) {
                Row {
                    Box(
                        modifier = Modifier
                            .background(
                                color = Orange_FF7800,
                                shape = RoundedCornerShape(3.dp)
                            )
                            .padding(horizontal = 7.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = reviewDetail?.mainMenuName
                                ?: stringResource(id = R.string.invalid_main_menu),
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
                                color = Gray_F6F6F8,
                                shape = RoundedCornerShape(3.dp)
                            )
                            .padding(horizontal = 7.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = reviewDetail?.subMenuName
                                ?: stringResource(id = R.string.invalid_sub_menu),
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
}