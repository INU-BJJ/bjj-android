package inu.appcenter.bjj_android.ui.menudetail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import inu.appcenter.bjj_android.model.todaydiet.TodayDietRes
import inu.appcenter.bjj_android.ui.menudetail.common.GrayHorizontalDivider
import inu.appcenter.bjj_android.ui.menudetail.menuinfo.HeaderSection
import inu.appcenter.bjj_android.ui.menudetail.menuinfo.NavigationButtons
import inu.appcenter.bjj_android.ui.menudetail.review.ReviewHeaderSection
import inu.appcenter.bjj_android.ui.menudetail.review.ReviewItem
import inu.appcenter.bjj_android.ui.theme.Gray_F6F8F8

private val VERTICAL_SPACER_HEIGHT = 19.8.dp
private val HORIZONTAL_PADDING = 21.3.dp
private val REVIEW_SPACER_HEIGHT = 17.dp
private val DIVIDER_THICKNESS = 7.dp
private val BOTTOM_SPACER_HEIGHT = 28.dp

@Composable
fun MenuDetailScreen(
    navController: NavHostController,
    menuDetailViewModel: MenuDetailViewModel
) {
    val menuDetailUiState by menuDetailViewModel.uiState.collectAsState()
    var pageNumber by remember { mutableIntStateOf(0) }

    LaunchedEffect(menuDetailUiState.selectedMenu?.menuPairId, menuDetailUiState.sort, menuDetailUiState.isWithImages) {
        menuDetailUiState.selectedMenu?.let { menu ->
            menuDetailViewModel.getReviewsByMenu(
                menuPairId = menu.menuPairId,
                sort = menuDetailUiState.sort,
                isWithImages = menuDetailUiState.isWithImages
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        when {
            menuDetailUiState.selectedMenu != null -> MenuContent(
                menuDetailUiState = menuDetailUiState,
                onPhotoFilterChange = { isWithImages ->
                    menuDetailViewModel.selectIsWithImages(isWithImages)
                    pageNumber = 0
                },
                onSortingChange = { sortRule ->
                    menuDetailViewModel.selectSortingRule(sortRule)
                    pageNumber = 0
                },
                onLoadMore = { selectedMenu ->
                    menuDetailViewModel.getMoreReviewsByMenu(
                        menuPairId = selectedMenu.menuPairId,
                        pageNumber = ++pageNumber,
                        sort = menuDetailUiState.sort
                    )
                },
                navController = navController
            )
            else -> NoMenuSelectedUI(navController)
        }
    }
}

@Composable
private fun MenuContent(
    menuDetailUiState: MenuDetailUiState,
    onPhotoFilterChange: (Boolean) -> Unit,
    onSortingChange: (SortingRules) -> Unit,
    onLoadMore: (TodayDietRes) -> Unit,
    navController: NavHostController
) {
    val selectedMenu = menuDetailUiState.selectedMenu ?: return

    LazyColumn {
        item { HeaderSection(menuDetailUiState.selectedMenu, navController) }
        item { Spacer(Modifier.height(VERTICAL_SPACER_HEIGHT)) }
        item { GrayHorizontalDivider(Modifier.padding(horizontal = HORIZONTAL_PADDING)) }
        item { Spacer(Modifier.height(VERTICAL_SPACER_HEIGHT)) }
        item {
            ReviewHeaderSection(
                menu = menuDetailUiState.selectedMenu,
                onlyPhoto = menuDetailUiState.isWithImages,
                onOnlyPhotoChanged = onPhotoFilterChange,
                selectedSortingRule = menuDetailUiState.sort,
                onSortingRUleChanged = onSortingChange
            )
        }

        items(menuDetailUiState.reviews?.reviewDetailList.orEmpty()) { review ->
            ReviewItem(review = review, menu = menuDetailUiState.selectedMenu)
            Spacer(Modifier.height(REVIEW_SPACER_HEIGHT))
            HorizontalDivider(
                thickness = DIVIDER_THICKNESS,
                color = Gray_F6F8F8,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(BOTTOM_SPACER_HEIGHT))
        }

        item {
            LoadMoreButton(
                isVisible = menuDetailUiState.reviews?.lastPage == false,
                onLoadMore = {
                    onLoadMore(menuDetailUiState.selectedMenu)
                }
            )
        }
    }
}

@Composable
private fun LoadMoreButton(
    isVisible: Boolean,
    onLoadMore: () -> Unit
) {
    if (!isVisible) return

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedButton(
            onClick = onLoadMore,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black),
            border = BorderStroke(width = 1.dp, color = Color.Black)
        ) {
            Text(text = "더보기")
        }
        Spacer(Modifier.height(BOTTOM_SPACER_HEIGHT))
    }
}

@Composable
private fun NoMenuSelectedUI(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        NavigationButtons(navController)
    }
}



