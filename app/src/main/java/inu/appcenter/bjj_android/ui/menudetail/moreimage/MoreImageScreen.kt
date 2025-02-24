package inu.appcenter.bjj_android.ui.menudetail.moreimage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.ui.menudetail.MenuDetailViewModel
import inu.appcenter.bjj_android.ui.navigate.AllDestination
import inu.appcenter.bjj_android.ui.navigate.AppBottomBar

private val GRID_SPACING = 4.dp
private const val PAGE_SIZE = 18

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreImageScreen(
    navController: NavHostController,
    menuDetailViewModel: MenuDetailViewModel,
    menuPairId: Long
) {
    val uiState by menuDetailViewModel.uiState.collectAsState()
    var currentPage by remember { mutableIntStateOf(0) }
    var isLoadingMore by remember { mutableStateOf(false) }
    var isLastPage by remember { mutableStateOf(false) }


    LaunchedEffect(menuPairId) {
        menuDetailViewModel.getMoreReviewImages(
            menuPairId = menuPairId,
            pageNumber = 0,
            pageSize = PAGE_SIZE
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                ),
                title = {
                    Text(
                        text = "리뷰 사진 모아보기",
                        style = LocalTypography.current.semibold18.copy(
                            letterSpacing = 0.13.sp,
                            lineHeight = 15.sp
                        ),
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(R.drawable.arrowback),
                            contentDescription = "뒤로가기",
                            tint = Color.Black
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            when {
                uiState.isLoading && currentPage == 0 -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                uiState.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        Text(uiState.error!!)
                    }
                }
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        horizontalArrangement = Arrangement.spacedBy(GRID_SPACING),
                        verticalArrangement = Arrangement.spacedBy(GRID_SPACING),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        uiState.reviewImages?.let { images ->
                            items(images) { imageDetail ->
                                Box(
                                    modifier = Modifier
                                        .aspectRatio(1f)
                                        .fillMaxWidth()
                                ) {
                                    SubcomposeAsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data("https://bjj.inuappcenter.kr/images/review/${imageDetail.imageName}")
                                            .memoryCacheKey(imageDetail.imageName)
                                            .diskCacheKey(imageDetail.imageName)
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clickable {
                                                navController.navigate(
                                                    AllDestination.MenuDetailReviewDetailPush.createRoute(
                                                        imageList = listOf(imageDetail.imageName),
                                                        index = 0,
                                                        reviewId = imageDetail.reviewId,
                                                        fromReviewDetail = false
                                                    )
                                                )
                                            },
                                        contentScale = ContentScale.Crop,
                                        loading = {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(10.dp),
                                                strokeWidth = 1.dp
                                            )
                                        }
                                    )
                                }
                            }

                            // 이미지가 있고, 마지막 페이지가 아닐 때만 추가 로딩
                            if (images.isNotEmpty() && !isLastPage) {
                                item(span = { GridItemSpan(maxLineSpan) }) {
                                    if (!isLoadingMore && !uiState.isLoading) {
                                        LaunchedEffect(Unit) {
                                            isLoadingMore = true
                                            currentPage++
                                            menuDetailViewModel.getMoreReviewImages(
                                                menuPairId = menuPairId,
                                                pageNumber = currentPage,
                                                pageSize = PAGE_SIZE
                                            )
                                            // 빈 리스트가 오면 마지막 페이지로 처리
                                            if (images.size % PAGE_SIZE != 0) {
                                                isLastPage = true
                                            }
                                            isLoadingMore = false
                                        }

                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(24.dp),
                                                strokeWidth = 2.dp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}