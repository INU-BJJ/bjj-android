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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.model.review.MyReviewDetailRes
import inu.appcenter.bjj_android.model.todaydiet.TodayDietRes
import inu.appcenter.bjj_android.ui.navigate.AppBottomBar
import inu.appcenter.bjj_android.ui.navigate.shadowCustom
import inu.appcenter.bjj_android.ui.theme.Brown_C09470
import inu.appcenter.bjj_android.ui.theme.Gray_B9B9B9
import inu.appcenter.bjj_android.ui.theme.Gray_F6F6F8
import inu.appcenter.bjj_android.ui.theme.Orange_FF7800
import inu.appcenter.bjj_android.ui.theme.Orange_FFF4DF
import inu.appcenter.bjj_android.ui.theme.RacingSansOne
import inu.appcenter.bjj_android.ui.theme.Yellow_FFC421
import java.util.Date
import kotlin.math.round

@Composable
fun RankingScreen(
    navController: NavHostController

) {

    val rankingList = listOf(
        TodayDietRes(
            todayDietId = 6129,
            price = "moderatius",
            kcal = "odio",
            date = Date(20241231),
            menuPairId = 7857,
            mainMenuId = 9362,
            mainMenuName = "우삼겹떡볶이*핫도그",
            subMenuId = 9290,
            restMenu = null,
            cafeteriaName = "학생식당",
            cafeteriaCorner = "2코너",
            reviewCount = 9321,
            reviewRatingAverage = 2.3f,
            reviewImageName = null,
            likedMenu = false
        ),
        TodayDietRes(
            todayDietId = 6129,
            price = "moderatius",
            kcal = "odio",
            date = Date(20241231),

            menuPairId = 7857,
            mainMenuId = 9362,
            mainMenuName = "우삼겹떡볶이*핫도그",
            subMenuId = 9290,
            restMenu = null,
            cafeteriaName = "학생식당",
            cafeteriaCorner = "2코너",
            reviewCount = 9321,
            reviewRatingAverage = 2.3f,
            reviewImageName = null,
            likedMenu = false
        ),
        TodayDietRes(
            todayDietId = 6129,
            price = "moderatius",
            kcal = "odio",
            date = Date(20241231),

            menuPairId = 7857,
            mainMenuId = 9362,
            mainMenuName = "우삼겹떡볶이*핫도그",
            subMenuId = 9290,
            restMenu = null,
            cafeteriaName = "학생식당",
            cafeteriaCorner = "2코너",
            reviewCount = 9321,
            reviewRatingAverage = 2.3f,
            reviewImageName = null,
            likedMenu = false
        ),
        TodayDietRes(
            todayDietId = 6129,
            price = "moderatius",
            kcal = "odio",
            date = Date(20241231),

            menuPairId = 7857,
            mainMenuId = 9362,
            mainMenuName = "우삼겹떡볶이*핫도그",
            subMenuId = 9290,
            restMenu = null,
            cafeteriaName = "학생식당",
            cafeteriaCorner = "2코너",
            reviewCount = 9321,
            reviewRatingAverage = 2.3f,
            reviewImageName = null,
            likedMenu = false
        ),
        TodayDietRes(
            todayDietId = 6129,
            price = "moderatius",
            kcal = "odio",
            date = Date(20241231),

            menuPairId = 7857,
            mainMenuId = 9362,
            mainMenuName = "우삼겹떡볶이*핫도그",
            subMenuId = 9290,
            restMenu = null,
            cafeteriaName = "학생식당",
            cafeteriaCorner = "2코너",
            reviewCount = 9321,
            reviewRatingAverage = 2.3f,
            reviewImageName = null,
            likedMenu = false
        ),
        TodayDietRes(
            todayDietId = 6129,
            price = "moderatius",
            kcal = "odio",
            date = Date(20241231),

            menuPairId = 7857,
            mainMenuId = 9362,
            mainMenuName = "우삼겹떡볶이*핫도그",
            subMenuId = 9290,
            restMenu = null,
            cafeteriaName = "학생식당",
            cafeteriaCorner = "2코너",
            reviewCount = 9321,
            reviewRatingAverage = 2.3f,
            reviewImageName = null,
            likedMenu = false
        ),
        TodayDietRes(
            todayDietId = 6129,
            price = "moderatius",
            kcal = "odio",
            date = Date(20241231),

            menuPairId = 7857,
            mainMenuId = 9362,
            mainMenuName = "우삼겹떡볶이*핫도그",
            subMenuId = 9290,
            restMenu = null,
            cafeteriaName = "학생식당",
            cafeteriaCorner = "2코너",
            reviewCount = 9321,
            reviewRatingAverage = 2.3f,
            reviewImageName = null,
            likedMenu = false
        ),
        TodayDietRes(
            todayDietId = 6129,
            price = "moderatius",
            kcal = "odio",
            date = Date(20241231),

            menuPairId = 7857,
            mainMenuId = 9362,
            mainMenuName = "우삼겹떡볶이*핫도그",
            subMenuId = 9290,
            restMenu = null,
            cafeteriaName = "학생식당",
            cafeteriaCorner = "2코너",
            reviewCount = 9321,
            reviewRatingAverage = 2.3f,
            reviewImageName = null,
            likedMenu = false
        ),
        TodayDietRes(
            todayDietId = 6129,
            price = "moderatius",
            kcal = "odio",
            date = Date(20241231),

            menuPairId = 7857,
            mainMenuId = 9362,
            mainMenuName = "우삼겹떡볶이*핫도그",
            subMenuId = 9290,
            restMenu = null,
            cafeteriaName = "학생식당",
            cafeteriaCorner = "2코너",
            reviewCount = 9321,
            reviewRatingAverage = 2.3f,
            reviewImageName = null,
            likedMenu = false
        ),
        TodayDietRes(
            todayDietId = 6129,
            price = "moderatius",
            kcal = "odio",
            date = Date(20241231),

            menuPairId = 7857,
            mainMenuId = 9362,
            mainMenuName = "우삼겹떡볶이*핫도그",
            subMenuId = 9290,
            restMenu = null,
            cafeteriaName = "학생식당",
            cafeteriaCorner = "2코너",
            reviewCount = 9321,
            reviewRatingAverage = 2.3f,
            reviewImageName = null,
            likedMenu = false
        ), TodayDietRes(
            todayDietId = 6129,
            price = "moderatius",
            kcal = "odio",
            date = Date(20241231),

            menuPairId = 7857,
            mainMenuId = 9362,
            mainMenuName = "우삼겹떡볶이*핫도그",
            subMenuId = 9290,
            restMenu = null,
            cafeteriaName = "학생식당",
            cafeteriaCorner = "2코너",
            reviewCount = 9321,
            reviewRatingAverage = 2.3f,
            reviewImageName = null,
            likedMenu = false
        ), TodayDietRes(
            todayDietId = 6129,
            price = "moderatius",
            kcal = "odio",
            date = Date(20241231),

            menuPairId = 7857,
            mainMenuId = 9362,
            mainMenuName = "우삼겹떡볶이*핫도그",
            subMenuId = 9290,
            restMenu = null,
            cafeteriaName = "학생식당",
            cafeteriaCorner = "2코너",
            reviewCount = 9321,
            reviewRatingAverage = 2.3f,
            reviewImageName = null,
            likedMenu = false
        ),
        TodayDietRes(
            todayDietId = 6129,
            price = "moderatius",
            kcal = "odio",
            date = Date(20241231),

            menuPairId = 7857,
            mainMenuId = 9362,
            mainMenuName = "우삼겹떡볶이*핫도그",
            subMenuId = 9290,
            restMenu = null,
            cafeteriaName = "학생식당",
            cafeteriaCorner = "2코너",
            reviewCount = 9321,
            reviewRatingAverage = 2.3f,
            reviewImageName = null,
            likedMenu = false
        ),
        TodayDietRes(
            todayDietId = 6129,
            price = "moderatius",
            kcal = "odio",
            date = Date(20241231),

            menuPairId = 7857,
            mainMenuId = 9362,
            mainMenuName = "우삼겹떡볶이*핫도그",
            subMenuId = 9290,
            restMenu = null,
            cafeteriaName = "학생식당",
            cafeteriaCorner = "2코너",
            reviewCount = 9321,
            reviewRatingAverage = 2.3f,
            reviewImageName = null,
            likedMenu = false
        ),
        TodayDietRes(
            todayDietId = 6129,
            price = "moderatius",
            kcal = "odio",
            date = Date(20241231),

            menuPairId = 7857,
            mainMenuId = 9362,
            mainMenuName = "우삼겹떡볶이*핫도그",
            subMenuId = 9290,
            restMenu = null,
            cafeteriaName = "학생식당",
            cafeteriaCorner = "2코너",
            reviewCount = 9321,
            reviewRatingAverage = 2.3f,
            reviewImageName = null,
            likedMenu = false
        )


    )

    // 다이얼로그 표시 여부를 관리하는 상태
    var showDialog by remember { mutableStateOf(false) }
// 선택된 메뉴 아이템을 저장하는 상태
    var reviewDetail : MyReviewDetailRes? = MyReviewDetailRes(
        reviewId = 8383,
        comment = "핫도그는 냉동인데\n떡볶이는 맛있음\n맛도 있고 가격도 착해서 떡볶이 땡길 때 추천",
        rating = 5,
        imageNames = listOf(
            "83760dd9-6f5f-4892-8ee8-7c82426e1c5d.jpg"
        ),
        likeCount = 5501,
        createdDate = "2024.08.20",
        menuPairId = 4420,
        mainMenuName = "떡볶이",
        subMenuName = "핫도그",
        memberId = 7013,
        memberNickname = "떡볶이킬러나는최고야룰루",
        memberImageName = null
    )

    if (showDialog && reviewDetail != null) {
        BestReviewDialog(
            review = reviewDetail!!,
            onDismiss = {
                showDialog = false
                reviewDetail = null
            }
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
                verticalAlignment = Alignment.CenterVertically
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
                        .size(23.dp)
                )
            }
            Spacer(Modifier.height(25.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "2024.12.26",
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
            Spacer(Modifier.height(7.dp))
            LazyColumn {
                itemsIndexed(rankingList) { index, rankingItem ->
                    if (index + 1 <= 3) {
                        TopThreeRankingItem(
                            menu = rankingItem,
                            ranking = index + 1,
                            itemClick = {
                                showDialog = true
                            }
                        )
                    } else {
                        NormalRankingItem(
                            menu = rankingItem,
                            ranking = index + 1,
                            itemClick = {
                                showDialog = true
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NormalRankingItem(
    modifier: Modifier = Modifier,
    menu: TodayDietRes,
    ranking: Int,
    itemClick: () -> Unit
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
                itemClick()
            },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.width(12.dp))
        Text(
            text = ranking.toString(),
            style = LocalTypography.current.bold18.copy(
                lineHeight = 15.sp,
                color = Color(0xFF000000),
                letterSpacing = 0.13.sp,
            )
        )
        Spacer(Modifier.width(17.dp))
        Row(
            modifier = Modifier
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = menu.mainMenuName,
                style = LocalTypography.current.bold15.copy(
                    letterSpacing = 0.13.sp,
                    lineHeight = 15.sp
                ),
                color = Color.Black
            )
            Column(
                modifier = Modifier
                    .padding(top = 6.dp, bottom = 7.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {
                Row(
                    modifier = Modifier
                        .width(53.dp)
                        .height(21.dp)
                        .background(color = Orange_FFF4DF, shape = RoundedCornerShape(32.dp)),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.star),
                        contentDescription = "별점",
                        tint = Color.Unspecified
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = (round(menu.reviewRatingAverage * 10) / 10).toString(),
                        style = LocalTypography.current.regular13.copy(
                            letterSpacing = 0.13.sp,
                            lineHeight = 17.sp
                        ),
                        color = Color.Black
                    )
                }
                Spacer(Modifier.height(5.dp))
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
    menu: TodayDietRes,
    ranking: Int,
    itemClick: () -> Unit
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
                itemClick()
            },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.width(8.dp))
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
        Spacer(Modifier.width(8.dp))
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
                .padding(top = 13.dp, bottom = 8.dp)
                .fillMaxHeight()
                .weight(1f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
            ) {
                Text(
                    text = menu.mainMenuName,
                    style = LocalTypography.current.bold15.copy(
                        letterSpacing = 0.13.sp,
                        lineHeight = 15.sp
                    ),
                    color = Color.Black
                )
            }
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 9.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .width(53.dp)
                        .height(21.dp)
                        .background(color = Orange_FFF4DF, shape = RoundedCornerShape(32.dp)),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.star),
                        contentDescription = "별점",
                        tint = Color.Unspecified
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = (round(menu.reviewRatingAverage * 10) / 10).toString(),
                        style = LocalTypography.current.regular13.copy(
                            letterSpacing = 0.13.sp,
                            lineHeight = 17.sp
                        ),
                        color = Color.Black
                    )
                }
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