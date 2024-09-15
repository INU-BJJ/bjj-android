package inu.appcenter.bjj_android.ui.main

import android.annotation.SuppressLint
import android.app.Activity
import android.view.Surface
import android.view.Window
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavHostController
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.ui.common.AppBottomBar
import inu.appcenter.bjj_android.ui.common.MainCardNews
import inu.appcenter.bjj_android.ui.common.MainMenuItem
import inu.appcenter.bjj_android.ui.common.MainRestaurantButton
import inu.appcenter.bjj_android.ui.theme.AppTypography
import inu.appcenter.bjj_android.ui.theme.Background
import inu.appcenter.bjj_android.ui.theme.Gray_999999
import inu.appcenter.bjj_android.ui.theme.Gray_F8F8F8
import inu.appcenter.bjj_android.ui.theme.Orange_FF7800
import inu.appcenter.bjj_android.ui.theme.Orange_FFF4DF
import kotlinx.coroutines.launch

internal val LocalTypography = staticCompositionLocalOf { AppTypography() }

data class MainMenu(
    val menu: String,
    val price: Int,
    val reviewStar: Float,
    val menuRestaurant: String,
    val menuImage: Int,
    val isLiked: Boolean
)

@SuppressLint("InvalidColorHexValue")
@Composable
fun MainScreen(
    navController: NavHostController
) {
    val restaurants = listOf(
        "인천대 학생식당",
        "인천대 교직원식당",
        "제 1 기숙사식당",
        "사범대식당",
        "27호관 식당"
    )

    val menus = listOf(
        MainMenu(
            menu = "차슈 덮밥",
            price = 7500,
            reviewStar = 4.4f,
            menuRestaurant = "학생식당 2코너",
            menuImage = R.drawable.example_menu_1,
            isLiked = false
        ),
        MainMenu(
            menu = "우삼겹떡볶이*핫도그",
            price = 5500,
            reviewStar = 4.2f,
            menuRestaurant = "학생식당 2코너",
            menuImage = R.drawable.example_menu_2,
            isLiked = true
        ),
        MainMenu(
            menu = "짜장면*짬뽕국",
            price = 7500,
            reviewStar = 4.3f,
            menuRestaurant = "학생식당 2코너",
            menuImage = R.drawable.example_menu_3,
            isLiked = false
        ),
        MainMenu(
            menu = "우삼겹떡볶이*핫도그",
            price = 5500,
            reviewStar = 4.2f,
            menuRestaurant = "학생식당 2코너",
            menuImage = R.drawable.example_menu_4,
            isLiked = true
        ),

        )


    var selectedButton by remember { mutableStateOf(restaurants[0]) }
    var restaurantInfo by remember { mutableStateOf(false) }
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val transparent = Color(0xFF00000000)

    //시스템 UI 설정
    val view = LocalView.current
    val window = (view.context as Activity).window
    if (!view.isInEditMode) {
        DisposableEffect(Unit) {

            val insetsController = WindowCompat.getInsetsController(window, view)

//            window.statusBarColor = Color.Transparent.toArgb()

            WindowCompat.setDecorFitsSystemWindows(window, false)
            insetsController.apply {
                hide(WindowInsetsCompat.Type.navigationBars())
                systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                isAppearanceLightStatusBars = true
            }

            onDispose {
//                WindowCompat.setDecorFitsSystemWindows(window, false)
            }
        }
    }
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ){
        Scaffold(
            bottomBar = { AppBottomBar(navController) },
        ) { innerPadding ->
            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Background)
                    .padding(),
            ) {
                item {
                    MainCardNews(
                        innerPadding = innerPadding
                    ) {
                        Text(
                            text = "오늘의 인기 메뉴를 \n알아볼까요?",
                            style = LocalTypography.current.main_headline,
                            color = Color.Black,
                            lineHeight = 35.sp
                        )
                    }
                }

                item {
                    LazyRow(
                        modifier = Modifier
                            .padding(top = 18.dp, bottom = 10.dp)
                    ) {
                        items(restaurants) { restaurant ->
                            MainRestaurantButton(
                                restaurant = restaurant,
                                restaurants = restaurants,
                                selectedButton = selectedButton,
                                onClick = {
                                    selectedButton = it
                                }
                            )
                        }
                    }
                }

                items(menus) { menu ->
                    MainMenuItem(menu = menu)
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
                            style = LocalTypography.current.main_menuInfo,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.width(7.4.dp))
                        Icon(
                            imageVector = if (restaurantInfo) Icons.Default.KeyboardArrowUp  else Icons.Default.KeyboardArrowDown,
                            contentDescription = "식당정보 더보기",
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .clickable {
                                    restaurantInfo = !restaurantInfo

                                    if (restaurantInfo) {
                                        coroutineScope.launch {
                                            lazyListState.animateScrollToItem(lazyListState.layoutInfo.totalItemsCount)
                                        }
                                    }
                                }
                        )
                    }
                }
                if (restaurantInfo){
                    item {
                        Spacer(modifier = Modifier.height(300.dp))
                    }
                }
            }
        }
    }

}

