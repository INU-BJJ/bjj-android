package inu.appcenter.bjj_android.ui.menudetail

import android.os.Parcelable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import inu.appcenter.bjj_android.ui.menudetail.common.GrayHorizontalDivider
import inu.appcenter.bjj_android.ui.menudetail.menuinfo.HeaderSection
import inu.appcenter.bjj_android.ui.menudetail.menuinfo.NavigationButtons
import inu.appcenter.bjj_android.ui.menudetail.review.ReviewHeaderSection
import inu.appcenter.bjj_android.ui.menudetail.review.ReviewItem
import inu.appcenter.bjj_android.ui.theme.Gray_F6F8F8
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Composable
fun MenuDetailScreen(
    navController: NavHostController,
    menuDetailViewModel: MenuDetailViewModel
) {
    val uiState by menuDetailViewModel.uiState.collectAsState()

    var pageNumber by remember {
        mutableIntStateOf(0)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        uiState.selectedMenu?.let { selectedMenu ->
            item { HeaderSection(selectedMenu, navController) }
            item { Spacer(Modifier.height(19.8.dp)) }
            item { GrayHorizontalDivider(Modifier.padding(horizontal = 21.3.dp)) }
            item { Spacer(Modifier.height(19.8.dp)) }
            item {
                ReviewHeaderSection(
                    menu = selectedMenu,
                    onlyPhoto = uiState.isWithImages,
                    onOnlyPhotoChanged = {
                        menuDetailViewModel.selectIsWithImages(it)
                        pageNumber = 0
                    },
                    selectedSortingRule = uiState.sort,
                    onSortingRUleChanged = {
                        menuDetailViewModel.selectSortingRule(it)
                        pageNumber = 0
                    }
                )
            }

            items(uiState.reviews?.reviewDetailList.orEmpty()) { review ->
                ReviewItem(review = review, menu = selectedMenu)
                Spacer(Modifier.height(17.dp))
                HorizontalDivider(
                    thickness = 7.dp,
                    color = Gray_F6F8F8,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(28.dp))
            }
            if (uiState.reviews?.lastPage == false){
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        OutlinedButton(
                            onClick = {
                                menuDetailViewModel.getMoreReviewsByMenu(
                                    menuPairId = selectedMenu.menuPairId,
                                    pageNumber = ++pageNumber,
                                    sort = uiState.sort
                                )
                            },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.Black
                            ),
                            border = BorderStroke(width = 1.dp, color = Color.Black)
                        ) {
                            Text(text = "더보기")
                        }
                    }
                    Spacer(Modifier.height(28.dp))
                }
            }
        } ?: item {
            // 선택된 메뉴가 없을 때 표시할 UI
            NoMenuSelectedUI(navController)
        }
    }
}

@Composable
fun NoMenuSelectedUI(
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            NavigationButtons(navController)
        }

    }

}







