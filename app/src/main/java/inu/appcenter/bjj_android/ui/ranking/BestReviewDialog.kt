package inu.appcenter.bjj_android.ui.ranking

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.ui.menudetail.review.DynamicReviewImages
import inu.appcenter.bjj_android.ui.menudetail.review.StarRatingCalculator
import inu.appcenter.bjj_android.ui.navigate.AllDestination
import inu.appcenter.bjj_android.ui.review.component.formatter
import inu.appcenter.bjj_android.ui.theme.Gray_999999
import inu.appcenter.bjj_android.ui.theme.Gray_D9D9D9
import inu.appcenter.bjj_android.ui.theme.Orange_FF7800
import inu.appcenter.bjj_android.ui.theme.White_FFFFFF

@Composable
fun BestReviewDialog(
    reviewId: Long,
    rankingViewModel: RankingViewModel,
    navController: NavHostController,
    onDismiss: () -> Unit
) {
    val uiState by rankingViewModel.uiState.collectAsState()

    LaunchedEffect(key1 = true) {
        rankingViewModel.getBestReviewDetail(reviewId)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        // bestReview가 null이면 로딩 중
        if (uiState.bestReview == null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = White_FFFFFF
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Orange_FF7800)
                }
            }
            return@Dialog
        }

        val bestReview = uiState.bestReview!!

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = White_FFFFFF
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 14.dp, vertical = 19.5.dp)
            ) {
                // 프로필 섹션
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        modifier = Modifier.size(41.dp),
                        color = Gray_D9D9D9
                    ) {
                        // 프로필 이미지 자리
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    //유저 정보 (이름, 별점, 날짜)
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = bestReview.memberNickname ?: "잘못된 닉네임",
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
                            StarRatingCalculator(rating = bestReview.rating.toFloat() ?: 0f)
                            Spacer(Modifier.width(10.dp))
                            Text(
                                text = bestReview.createdDate.formatter() ?: "2025.00.00",
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
                        //베스트 리뷰 로고
                        Row(
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = Orange_FF7800,
                                    shape = RoundedCornerShape(size = 40.dp)
                                )
                                .width(75.dp)
                                .height(18.dp)
                                .padding(start = 4.dp, top = 1.dp, end = 4.dp, bottom = 1.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "best review",
                                style = LocalTypography.current.medium11.copy(
                                    lineHeight = 15.sp,
                                    color = Orange_FF7800,
                                    letterSpacing = 0.13.sp,
                                )
                            )
                        }
                    }
                }

                Spacer(Modifier.height(7.dp))

                // 텍스트 영역
                Text(
                    text = bestReview.comment ?: "잘못된 코멘트",
                    style = LocalTypography.current.medium13.copy(
                        letterSpacing = 0.13.sp,
                        lineHeight = 17.sp
                    ),
                    color = Color.Black
                )


                // 이미지 영역
                if (bestReview.imageNames.isNotEmpty()) {
                    Spacer(Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                    ) {
                        DynamicReviewImages(
                            reviewImages = bestReview.imageNames,
                            onClick = { imageList, index ->
                                navController.navigate(
                                    AllDestination.MenuDetailReviewDetailPush.createRoute(
                                        imageList = bestReview.imageNames,
                                        index = index,
                                        reviewId = reviewId,
                                        fromReviewDetail = false
                                    )
                                )

                            }
                        )
                    }
                }
            }
        }
    }
}