package inu.appcenter.bjj_android.ui.ranking

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.model.menu.MenuRankingDetail
import inu.appcenter.bjj_android.ui.component.error.ErrorHandler
import inu.appcenter.bjj_android.ui.navigate.AppBottomBar
import inu.appcenter.bjj_android.ui.navigate.shadowCustom
import inu.appcenter.bjj_android.ui.review.component.formatter
import inu.appcenter.bjj_android.ui.theme.Gray_999999
import inu.appcenter.bjj_android.ui.theme.Gray_F6F6F8
import inu.appcenter.bjj_android.ui.theme.Orange_FF7800
import inu.appcenter.bjj_android.ui.theme.Orange_FFF4DF
import inu.appcenter.bjj_android.ui.theme.RacingSansOne

@Composable
fun RankingScreen(
    navController: NavHostController,
    rankingViewModel: RankingViewModel
) {
    val rankingUiState by rankingViewModel.uiState.collectAsStateWithLifecycle()

    var rankingInfoDialog by rememberSaveable { mutableStateOf(false) }

    ErrorHandler(viewModel = rankingViewModel, navController = navController)

    LaunchedEffect(key1 = true) {
        rankingViewModel.getMenuRankingList()
    }

    // 다이얼로그 표시 로직을 아이템 클릭 핸들러 안으로 이동
    val handleItemClick: (Long) -> Unit = { bestReviewId ->
        rankingViewModel.getBestReviewDetail(bestReviewId)
        rankingViewModel.selectBestReviewId(bestReviewId)
    }

    if (rankingUiState.selectedReviewId != null && rankingUiState.bestReview != null) {
        BestReviewDialog(
            reviewId = rankingUiState.selectedReviewId!!,
            navController = navController,
            rankingViewModel = rankingViewModel,
            onDismiss = {
                rankingViewModel.resetSelectedBestReviewId()
            }
        )
    }

    if (rankingInfoDialog){
        RankingInfoDialog(
            onDismiss = {rankingInfoDialog = false}
        )
    }

    Scaffold(
        bottomBar = { AppBottomBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Gray_F6F6F8)
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 23.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
            ) {
                Text(
                    text = "Menu Ranking",
                    style = TextStyle(
                        fontSize = 30.sp,
                        lineHeight = 20.sp,
                        fontFamily = RacingSansOne,
                        fontWeight = FontWeight(400),
                        color = Color(0xFFFF7800),
                        letterSpacing = 0.13.sp,
                    )
                )
                Spacer(Modifier.width(11.dp))
                Icon(
                    painter = painterResource(R.drawable.tier),
                    contentDescription = "tier",
                    tint = Orange_FF7800,
                    modifier = Modifier
                        .width(23.dp)
                )
            }
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Row(
                    modifier = Modifier
                        .clickable {
                            rankingInfoDialog = true
                        }
                ) {
                    Text(
                        text = rankingUiState.lastUpdatedAt?.formatter() ?: "",
                        style = LocalTypography.current.medium11.copy(
                            lineHeight = 15.sp,
                            fontWeight = FontWeight(500),
                            color = Color(0xFF999999),
                            letterSpacing = 0.13.sp,
                        )
                    )
                    Spacer(Modifier.width(5.dp))
                    Text(
                        text = "업데이트",
                        style = LocalTypography.current.medium11.copy(
                            lineHeight = 15.sp,
                            fontWeight = FontWeight(500),
                            color = Color(0xFF999999),
                            letterSpacing = 0.13.sp,
                        )
                    )
                    Spacer(Modifier.width(7.dp))
                    Icon(
                        painter = painterResource(R.drawable.info),
                        contentDescription = "Info",
                        tint = Color.Unspecified,
                    )
                }
            }
            Spacer(Modifier.height(7.dp))
            LazyColumn {
                if (rankingUiState.rankingList.isEmpty()) {
                    item {
                        EmptyRankingContent()
                    }
                } else {
                    itemsIndexed(rankingUiState.rankingList) { index, rankingItem ->
                        if (index + 1 <= 3) {
                            TopThreeRankingItem(
                                menu = rankingItem,
                                ranking = index + 1,
                                itemClick = handleItemClick
                            )
                        } else {
                            NormalRankingItem(
                                menu = rankingItem,
                                ranking = index + 1,
                                itemClick = handleItemClick
                            )
                        }

                        if (index == rankingUiState.rankingList.size - 1 && !rankingUiState.isLoading) {
                            LaunchedEffect(Unit) {
                                rankingViewModel.getMenuRankingList()
                            }
                        }
                    }
                }

                if (rankingUiState.isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NormalRankingItem(
    modifier: Modifier = Modifier,
    menu: MenuRankingDetail,
    ranking: Int,
    itemClick: (Long) -> Unit
) {
    Row(
        modifier = modifier
            .shadowCustom(
                offsetX = 0.dp,
                offsetY = 1.dp,
                blurRadius = 4.dp,
                color = Color(0xFF0C0C0C).copy(alpha = 0.05f)
            )
            .fillMaxWidth()
            .height(54.dp)
            .background(color = Color.White, shape = RoundedCornerShape(3.dp))
            .clickable {
                itemClick(menu.bestReviewId)
            },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = ranking.toString(),
            style = LocalTypography.current.bold18.copy(
                lineHeight = 15.sp,
                color = Color(0xFF000000),
                letterSpacing = 0.13.sp,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.width(50.dp)
        )
        Row(
            modifier = Modifier
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = menu.menuName,
                style = LocalTypography.current.bold15.copy(
                    letterSpacing = 0.13.sp,
                    lineHeight = 15.sp
                ),
                color = Color.Black
            )
            Column(
                modifier = Modifier
                    .padding(top = 11.dp, bottom = 7.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = menu.menuRating.toString(),
                        style = LocalTypography.current.medium13.copy(
                            letterSpacing = 0.13.sp,
                            lineHeight = 17.sp
                        ),
                        color = Color.Black
                    )
                    Text(
                        text = " / 10",
                        style = LocalTypography.current.medium13.copy(
                            letterSpacing = 0.13.sp,
                            lineHeight = 17.sp
                        ),
                        color = Gray_999999
                    )
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "${menu.cafeteriaName} ${menu.cafeteriaCorner}",
                    style = LocalTypography.current.regular11.copy(
                        letterSpacing = 0.13.sp,
                        lineHeight = 15.sp
                    ),
                    color = Color.Black.copy(alpha = 0.5f)
                )
            }
        }
        Spacer(Modifier.width(11.dp))
    }
    Spacer(Modifier.height(13.dp))
}

@Composable
fun TopThreeRankingItem(
    modifier: Modifier = Modifier,
    menu: MenuRankingDetail,
    ranking: Int,
    itemClick: (Long) -> Unit
) {
    Row(
        modifier = modifier
            .shadowCustom(
                offsetX = 0.dp,
                offsetY = 1.dp,
                blurRadius = 4.dp,
                color = Color(0xFF0C0C0C).copy(alpha = 0.05f)
            )
            .fillMaxWidth()
            .height(69.dp)
            .background(color = Color.White, shape = RoundedCornerShape(3.dp))
            .clickable {
                itemClick(menu.bestReviewId)
            },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.width(10.dp))
        Box(
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(
                    when(ranking){
                        1 -> {
                            R.drawable.ranking_1
                        }
                        2 -> {
                            R.drawable.ranking_2
                        }
                        else -> {
                            R.drawable.ranking_3
                        }
                    }
                ),
                contentDescription = "랭킹 메달",
                tint = Color.Unspecified
            )
        }
        Spacer(Modifier.width(12.dp))
        if (menu.reviewImageName != null) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://bjj.inuappcenter.kr/images/review/${menu.reviewImageName}")
                    .memoryCacheKey(menu.reviewImageName)
                    .diskCacheKey(menu.reviewImageName)
                    .crossfade(true)
                    .listener(
                        onError = { _, result ->
                            Log.e(
                                "ImageLoading",
                                "Error loading image: ${result.throwable.message}",
                                result.throwable
                            )
                        },
                        onSuccess = { _, _ ->
                            Log.d("ImageLoading", "Successfully loaded image")
                        }
                    )
                    .build(),
                placeholder = painterResource(R.drawable.placeholder),
                contentDescription = "메인 페이지 리뷰 이미지",
                contentScale = ContentScale.Crop, // Crop으로 변경
                modifier = Modifier
                    .padding(top = 6.4.dp, bottom = 6.4.dp)
                    .width(71.dp)
                    .height(53.dp)
                    .clip(RoundedCornerShape(3.dp))  // 라운드 처리 추가
            )
        } else {
            Image(
                painter = painterResource(R.drawable.placeholder),
                contentDescription = "리뷰 이미지 없을 경우 대체 이미지",
                contentScale = ContentScale.Crop, // Crop으로 변경
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp)
                    .fillMaxHeight()
                    .width(71.dp)
                    .height(53.dp)
                    .clip(RoundedCornerShape(3.dp))  // 라운드 처리 추가
            )
        }
        Column(
            modifier = Modifier
                .padding(top = 13.dp, bottom = 8.dp, start = 12.dp)
                .fillMaxHeight()
                .weight(1f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
            ) {
                Text(
                    text = menu.menuName,
                    style = LocalTypography.current.bold15.copy(
                        letterSpacing = 0.13.sp,
                        lineHeight = 15.sp
                    ),
                    color = Color.Black
                )
            }
            Spacer(Modifier.height(5.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 30.dp)
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = menu.menuRating.toString(),
                        style = LocalTypography.current.semibold15.copy(
                            letterSpacing = 0.13.sp,
                            lineHeight = 18.sp
                        ),
                        color = Orange_FF7800
                    )
                    Text(
                        text = " / 10",
                        style = LocalTypography.current.medium13.copy(
                            letterSpacing = 0.13.sp,
                            lineHeight = 17.sp
                        ),
                        color = Gray_999999
                    )
                }
                Text(
                    text = "${menu.cafeteriaName} ${menu.cafeteriaCorner}",
                    style = LocalTypography.current.regular11.copy(
                        letterSpacing = 0.13.sp,
                        lineHeight = 15.sp
                    ),
                    color = Color.Black.copy(alpha = 0.5f),
                    modifier = Modifier
                        .align(Alignment.BottomEnd),
                )
            }
        }
        Spacer(Modifier.width(11.dp))
    }
    Spacer(Modifier.height(13.dp))
}