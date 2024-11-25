package inu.appcenter.bjj_android.ui.review.page

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import inu.appcenter.bjj_android.ui.review.ReviewViewModel
import inu.appcenter.bjj_android.ui.review.tool.DashedBorderBox
import inu.appcenter.bjj_android.ui.review.tool.DropdownMenuBox
import inu.appcenter.bjj_android.ui.review.tool.DropdownType
import inu.appcenter.bjj_android.ui.review.tool.ReviewTextField
import inu.appcenter.bjj_android.ui.review.tool.StarRatingCalculatorBig
import inu.appcenter.bjj_android.ui.review.tool.WriteComplete
import inu.appcenter.bjj_android.ui.theme.Gray_B9B9B9
import inu.appcenter.bjj_android.ui.theme.Red_FF3916

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewWriteScreen(navController: NavHostController, reviewViewModel: ReviewViewModel) {
    var cautionExpanded by remember { mutableStateOf(false) }
    var reviewComment by remember { mutableStateOf("") }
    var currentRating by remember { mutableIntStateOf(4) }


    DisposableEffect(Unit) {
        onDispose {
            reviewViewModel.resetSelectedReviewRestaurant()
            reviewViewModel.resetSelectedMenu()
            Log.d("Disposable", "now")
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
                    text = "리뷰 작성하기",
                    style = LocalTypography.current.semibold18.copy(
                        letterSpacing = 0.13.sp,
                        lineHeight = 15.sp
                    ),
                    color = Color.Black
                )
            }, navigationIcon = {
                Icon(
                    modifier = Modifier
                        .offset(x = 19.4.dp, y = 4.5.dp)
                        .clickable { navController.popBackStack() },
                    painter = painterResource(id = R.drawable.leftarrow),
                    contentDescription = "뒤로 가기",
                    tint = Color.Black
                )
            })

        Column(
            modifier = Modifier
                .padding(horizontal = 21.dp)
                .fillMaxWidth(),
        ) {
            // 식당 위치 드롭다운
            DropdownMenuBox(
                reviewViewModel,
                dropdownType = DropdownType.RESTAURANT,
                labelText = "식당 위치"
            )
            Spacer(modifier = Modifier.height(10.dp))

            // 메뉴 선택 드롭다운
            DropdownMenuBox(
                reviewViewModel,
                dropdownType = DropdownType.MENU,
                labelText = "메뉴 선택"
            )
            Spacer(modifier = Modifier.height(13.8.dp))

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),       // 선의 길이를 화면 가득 채우기
                thickness = 0.5.dp,             // 선의 두께
                color = Gray_B9B9B9
            )
            Spacer(modifier = Modifier.height(13.8.dp))

            Text(
                text = "음식 만족을 평가해주세요", style = LocalTypography.current.semibold15.copy(
                    letterSpacing = 0.13.sp,
                    lineHeight = 18.sp,
                    color = Color.Black
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                StarRatingCalculatorBig(
                    initialRating = 5f,
                    onRatingChanged = { newRating ->
                        currentRating = newRating
                        //reviewViewModel.setRating(newRating)
                    })
            }
            Spacer(modifier = Modifier.height(25.dp))

            Text(
                text = "자세한 리뷰를 작성해주세요", style = LocalTypography.current.semibold15.copy(
                    letterSpacing = 0.13.sp,
                    lineHeight = 18.sp,
                    color = Color.Black
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            ReviewTextField(
                reviewComment,
                onTextChange = { newText ->
                    reviewComment = newText // 상태 업데이트
                }
            )
            Spacer(modifier = Modifier.height(15.dp))

            DashedBorderBox()
            Spacer(modifier = Modifier.height((44.7.dp)))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "리뷰  작성 유의사항",
                    style = LocalTypography.current.semibold15.copy(
                        letterSpacing = 0.13.sp,
                        lineHeight = 18.sp,
                        color = Color.Black
                    )
                )
                Icon(
                    modifier = Modifier.clickable {
                        cautionExpanded = !cautionExpanded
                    },
                    painter = painterResource(R.drawable.underarrow),
                    contentDescription = "아래 화살표",
                    tint = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(23.dp))

            if (cautionExpanded) {
                AnimatedVisibility(
                    visible = cautionExpanded,
                    enter = fadeIn(), // 텍스트가 나타날 때 페이드 인 애니메이션
                    exit = fadeOut() // 텍스트가 사라질 때 페이드 아웃 애니메이션
                ) {
                    Text(
                        modifier = Modifier.padding(start = 5.dp),
                        text = "식당 오픈 전에는 식사 리뷰를 작성하실 수 없습니다.",
                        style = LocalTypography.current.regular13.copy(
                            letterSpacing = 0.13.sp,
                            lineHeight = 17.sp
                        ),
                        color = Red_FF3916
                    )
                }
            } else {
                AnimatedVisibility(
                    visible = cautionExpanded,
                    enter = fadeIn(), // 텍스트가 나타날 때 페이드 인 애니메이션
                    exit = fadeOut() // 텍스트가 사라질 때 페이드 아웃 애니메이션
                ) {
                    Text(
                        modifier = Modifier.padding(start = 5.dp),
                        text = "",
                        style = LocalTypography.current.regular13.copy(
                            letterSpacing = 0.13.sp,
                            lineHeight = 17.sp
                        ),
                        color = Red_FF3916
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))

            WriteComplete(reviewComment, currentRating, reviewViewModel,  onSuccess = { navController.popBackStack() })
        }
    }
}

