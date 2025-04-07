package inu.appcenter.bjj_android.ui.menudetail.moreimage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.ui.component.error.ErrorHandler
import inu.appcenter.bjj_android.ui.component.LoadingIndicator
import inu.appcenter.bjj_android.ui.menudetail.MenuDetailViewModel
import inu.appcenter.bjj_android.ui.navigate.AllDestination
import inu.appcenter.bjj_android.utils.ImageLoader

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

    LoadingIndicator(menuDetailViewModel)
    ErrorHandler(menuDetailViewModel)

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
                            // ImageLoader 사용
                            ImageLoader.ReviewImage(
                                imageName = imageDetail.imageName,
                                modifier = Modifier.fillMaxSize(),
                                clickable = true,
                                onClick = {
                                    navController.navigate(
                                        AllDestination.MenuDetailReviewDetailPush.createRoute(
                                            imageList = listOf(imageDetail.imageName),
                                            index = 0,
                                            reviewId = imageDetail.reviewId,
                                            fromReviewDetail = false,
                                            menuId = menuPairId
                                        )
                                    )
                                }
                            )
                        }
                    }

                    if (images.isNotEmpty() && !isLastPage) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            if (!isLoadingMore) {
                                LaunchedEffect(Unit) {
                                    isLoadingMore = true
                                    currentPage++
                                    menuDetailViewModel.getMoreReviewImages(
                                        menuPairId = menuPairId,
                                        pageNumber = currentPage,
                                        pageSize = PAGE_SIZE
                                    )
                                    if (images.size % PAGE_SIZE != 0) {
                                        isLastPage = true
                                    }
                                    isLoadingMore = false
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


