package inu.appcenter.bjj_android.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Scale
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.ui.navigate.AllDestination
import inu.appcenter.bjj_android.ui.review.ReviewViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewImageDetailScreen(
    navController: NavHostController,
    // 첫 번째 사용 방식: 직접 이미지 리스트와 인덱스 전달 (기존 메뉴 디테일 용)
    imageList: List<String>? = null,
    index: Int = 0,
    reviewId: Long = -1L,
    fromReviewDetail: Boolean = false,
    // 두 번째 사용 방식: ReviewViewModel 사용 (기존 리뷰 상세보기 용)
    reviewViewModel: ReviewViewModel? = null
) {
    // ReviewViewModel이 있으면 ViewModel의 상태를 수집
    val reviewUiState = reviewViewModel?.uiState?.collectAsState()

    // 데이터 소스 결정: 직접 파라미터 또는 viewModel
    val images = if (reviewViewModel != null && reviewUiState != null) {
        // ReviewViewModel에서 데이터 가져오기
        reviewUiState.value.imageNames
    } else {
        // 직접 전달된 이미지 리스트 사용
        imageList ?: emptyList()
    }

    // 초기 인덱스 설정
    var currentIndex by remember {
        mutableIntStateOf(
            if (reviewViewModel != null && reviewUiState != null) {
                reviewUiState.value.selectedImageIndex
            } else {
                index
            }
        )
    }

    var offsetX by remember { mutableFloatStateOf(0f) }
    val imageName = images.getOrNull(currentIndex)
    val totalImages = images.size
    val displayText = if (totalImages > 0) "${currentIndex + 1}/$totalImages" else "0/0"

    Column(
        modifier = Modifier
            .background(color = Color.Black)
            .fillMaxSize()
    ) {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(Color.Black),
            title = {
                Text(
                    text = displayText,
                    style = LocalTypography.current.medium15.copy(
                        letterSpacing = 0.13.sp,
                        lineHeight = 15.sp
                    ),
                    color = Color.White
                )
            },
            navigationIcon = {
                Icon(
                    modifier = Modifier
                        .offset(x = 19.4.dp, y = 4.5.dp)
                        .clickable { navController.popBackStack() },
                    painter = painterResource(id = R.drawable.leftarrow),
                    contentDescription = "뒤로 가기",
                    tint = Color.White
                )
            },
            actions = {
                Icon(
                    modifier = Modifier
                        .offset(x = -(12.1).dp)
                        .clickable { navController.popBackStack() },
                    painter = painterResource(id = R.drawable.xbutton),
                    contentDescription = "닫기",
                    tint = Color.White
                )
            }
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragStart = { },
                        onDragEnd = {
                            // 드래그가 끝났을 때 offsetX의 값에 따라 페이지 전환 여부 결정
                            when {
                                offsetX > size.width * 0.1f && currentIndex > 0 -> {
                                    currentIndex--
                                }
                                offsetX < -size.width * 0.1f && currentIndex < totalImages - 1 -> {
                                    currentIndex++
                                }
                            }
                            // offset 초기화
                            offsetX = 0f
                        },
                        onDragCancel = {
                            offsetX = 0f
                        },
                        onHorizontalDrag = { change, dragAmount ->
                            change.consume()

                            // 현재 페이지가 첫 페이지이고 오른쪽으로 드래그하거나
                            // 마지막 페이지이고 왼쪽으로 드래그할 때는 저항감 추가
                            val resistance = when {
                                currentIndex == 0 && dragAmount > 0 -> 0.05f
                                currentIndex == totalImages - 1 && dragAmount < 0 -> 0.05f
                                else -> 1f
                            }

                            offsetX = (offsetX + dragAmount * resistance).coerceIn(
                                -size.width.toFloat(),
                                size.width.toFloat()
                            )
                        }
                    )
                }
        ) {
            val offset by animateFloatAsState(
                targetValue = offsetX,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://bjj.inuappcenter.kr/images/review/${imageName}")
                    .size(500) // 적절한 크기로 조정 (픽셀 단위)
                    .scale(Scale.FILL)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .memoryCacheKey(imageName)
                    .diskCacheKey(imageName)
                    .crossfade(true)
                    .build(),
                contentDescription = "Selected Food Image",
                modifier = Modifier
                    .fillMaxSize()
                    .offset(x = offset.dp),
                contentScale = ContentScale.Fit
            )
        }

        // 버튼 영역 - 본문 보기 버튼이 필요한 경우만 표시
        if (!fromReviewDetail) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 20.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.seemainbutton),
                    contentDescription = "본문 보기",
                    tint = Color.White,
                    modifier = Modifier.clickable {
                        // 사용 방식에 따라 다른 네비게이션 처리
                        if (reviewViewModel != null) {
                            navController.navigate(AllDestination.ReviewDetail.route)
                        } else {
                            navController.navigate(
                                AllDestination.MenuDetailReviewDetail.createRoute(
                                    reviewId = reviewId
                                )
                            )
                        }
                    }
                )
            }
        }
    }
}