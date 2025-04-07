package inu.appcenter.bjj_android.ui.main

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.ui.component.error.ErrorHandler
import inu.appcenter.bjj_android.ui.login.AuthViewModel
import inu.appcenter.bjj_android.ui.main.common.MainCardNews
import inu.appcenter.bjj_android.ui.main.common.MainMenuItem
import inu.appcenter.bjj_android.ui.main.common.MainRestaurantButton
import inu.appcenter.bjj_android.ui.main.common.RestaurantInfo
import inu.appcenter.bjj_android.ui.menudetail.MenuDetailViewModel
import inu.appcenter.bjj_android.ui.navigate.AllDestination
import inu.appcenter.bjj_android.ui.navigate.AppBottomBar
import inu.appcenter.bjj_android.ui.theme.Background
import inu.appcenter.bjj_android.ui.theme.Orange_FF7800
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@SuppressLint("InvalidColorHexValue")
@Composable
fun MainScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    mainViewModel: MainViewModel,
    menuDetailViewModel: MenuDetailViewModel,
    onTokenExpired : () -> Unit
) {
    val mainUiState by mainViewModel.uiState.collectAsState()
    val authUiState by authViewModel.uiState.collectAsState()

    ErrorHandler(viewModel = mainViewModel, navController = navController)

    // hasToken 상태를 관찰하여 API 호출
    LaunchedEffect(authUiState.hasToken) {
        if (authUiState.hasToken == true) {
            delay(300) // 토큰 저장이 완전히 완료될 때까지 잠시 대기
            mainViewModel.getCafeterias()
        }
    }

    var restaurantInfo by remember { mutableStateOf(false) }
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val dummyCardNews = listOf(Color.Red, Color.Blue, Color.Yellow, Color.White)
    //카드뉴스 페이저
    var isAutoScrolling by remember {
        mutableStateOf(false)
    }
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { dummyCardNews.size }
    )
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()
    LaunchedEffect(key1 = pagerState.currentPage) {
        if (isDragged) {
            isAutoScrolling = false
        } else {
            isAutoScrolling = true
            delay(5000)
            with(pagerState) {
                val target = if (currentPage < dummyCardNews.size - 1) currentPage + 1 else 0
                scrollToPage(target)
            }
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            bottomBar = { AppBottomBar(navController) },
        ) { innerPadding ->
            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Background)
                    .padding(bottom = innerPadding.calculateBottomPadding()),
            ) {
                item {
                    HorizontalPager(
                        state = pagerState,
                        pageSize = PageSize.Fill,
                    ) { page ->
                        if (isAutoScrolling) {
                            AnimatedContent(
                                targetState = page,
                                label = ""
                            ) { index ->
                                MainCardNews(
                                    backgroundColor = dummyCardNews[index],
                                    innerPadding = innerPadding
                                ) {
                                    Text(
                                        text = "오늘의 인기 메뉴를 \n알아볼까요? ${index+1}",
                                        style = LocalTypography.current.semibold24,
                                        color = Color.Black,
                                        lineHeight = 35.sp
                                    )
                                }
                            }
                        } else {
                            AnimatedContent(
                                targetState = page,
                                label = ""
                            ) { index ->
                                MainCardNews(
                                    backgroundColor = Orange_FF7800,
                                    innerPadding = innerPadding
                                ) {
                                    Text(
                                        text = "오늘의 인기 메뉴를 \n알아볼까요? ${index}",
                                        style = LocalTypography.current.semibold24,
                                        color = Color.Black,
                                        lineHeight = 35.sp
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    LazyRow(
                        modifier = Modifier
                            .padding(top = 18.dp, bottom = 10.dp)
                    ) {
                        items(mainUiState.cafeterias) { restaurant ->
                            MainRestaurantButton(
                                restaurant = restaurant,
                                restaurants = mainUiState.cafeterias,
                                selectedButton = mainUiState.selectedCafeteria ?: "",
                                onClick = {
                                    mainViewModel.selectCafeteria(it)
                                }
                            )
                        }
                    }
                }

                if (mainUiState.menus.isEmpty()){
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(350.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.warning),
                                contentDescription = "메뉴 없음 경고",
                                tint = Color.Unspecified
                            )
                            Spacer(Modifier.height(20.dp))
                            Text(
                                text = "오늘은 운영을 안해요!",
                                style = LocalTypography.current.bold18.copy(
                                    color = Color(0xFFD9D9D9),
                                    textAlign = TextAlign.Center,
                                    letterSpacing = 0.13.sp,
                                )
                            )
                        }
                    }
                } else {
                    items(mainUiState.menus) { menu ->
                        MainMenuItem(
                            menu = menu,
                            clickMenuDetail = {
                                menuDetailViewModel.selectMenu(menu = menu)
                                navController.navigate(AllDestination.MenuDetail.route)
                            },
                            mainViewModel = mainViewModel
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(57.dp))
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth(),
                        thickness = 7.dp,
                        color = Color.White
                    )
                }

                item {
                    Row(
                        modifier = Modifier
                            .padding(vertical = 18.dp, horizontal = 20.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "식당 정보",
                            style = LocalTypography.current.semibold18.copy(
                                letterSpacing = 0.13.sp,
                                lineHeight = 15.sp
                            ),
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.width(7.4.dp))
                        Icon(
                            imageVector = if (restaurantInfo) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "식당정보 더보기",
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .clickable {
                                    restaurantInfo = !restaurantInfo

                                    if (restaurantInfo) {
                                        coroutineScope.launch {
                                            delay(100)
                                            lazyListState.animateScrollToItem(lazyListState.layoutInfo.totalItemsCount)
                                        }
                                    }
                                }
                        )
                    }
                }
                if (restaurantInfo) {
                    item {
                        // RestaurantInfoWithMap 컴포넌트 사용
                        RestaurantInfo(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 25.dp, start = 20.dp, end = 20.dp),
                            restaurantName = mainUiState.selectedCafeteria ?: "",
                            location = "11호관 (복지회관) 1층",
                            weekSchedule = listOf("11:00 ~ 13:30", "17:00 ~ 18:10"),
                            weekendSchedule = listOf("11:00 ~ 13:30", "17:00 ~ 18:10"),
                            mapImageName = ""
                        )
                    }
                }
            }
        }
    }
}