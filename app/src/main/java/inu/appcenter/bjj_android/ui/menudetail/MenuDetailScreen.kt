package inu.appcenter.bjj_android.ui.menudetail

import android.graphics.BlurMaskFilter
import android.os.Parcelable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.ui.menudetail.common.GrayHorizontalDivider
import inu.appcenter.bjj_android.ui.menudetail.review.ReviewItem
import inu.appcenter.bjj_android.ui.main.MainMenu
import inu.appcenter.bjj_android.ui.menudetail.menuinfo.HeaderSection
import inu.appcenter.bjj_android.ui.menudetail.review.ReviewHeaderSection
import inu.appcenter.bjj_android.ui.theme.Gray_F6F8F8
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class Review(
    val userName: String,
    val reviewStar: Float,
    val profileImage: Int?,
    val writtenTime: LocalDateTime,
    val goodCount: Int,
    val description: String,
    val reviewImages: List<Int>?,
    val tags: List<String>,
    val isGood: Boolean
) : Parcelable



@Composable
fun MenuDetailScreen(
    menu: MainMenu,
    navController: NavHostController
) {
    var onlyPhoto by remember { mutableStateOf(false) }
    val reviews = listOf<Review>(
        Review(
            userName = "떡볶이킬러나는최고야룰루",
            reviewStar = 5f,
            profileImage = null,
            writtenTime = LocalDateTime.of(2024, 8, 20, 0, 0),
            goodCount = 0,
            description = "핫도그는 냉동인데\n떡볶이는 맛있음\n맛도 있고 가격도 착해서 떡볶이 땡길 때 추천",
            reviewImages = listOf(R.drawable.example_menu_big_1),
            tags = listOf("우삼겹떡볶이*핫도그", "오뎅국"),
            isGood = false
        ),
        Review(
            userName = "부대찌개사랑",
            reviewStar = 4f,
            profileImage = null,
            writtenTime = LocalDateTime.of(2024, 8, 20, 0, 0),
            goodCount = 6,
            description = "핫도그는 냉동인데\n떡볶이는 맛있음\n맛도 있고 가격도 착해서 떡볶이 땡길 때 추천",
            reviewImages = listOf(R.drawable.example_menu_big_1, R.drawable.example_menu_big_1),
            tags = listOf("우삼겹떡볶이*핫도그", "오뎅국"),
            isGood = true
        ),
        Review(
            userName = "부대찌개사랑",
            reviewStar = 2.2f,
            profileImage = null,
            writtenTime = LocalDateTime.of(2024, 8, 20, 0, 0),
            goodCount = 6,
            description = "핫도그는 냉동인데\n떡볶이는 맛있음\n맛도 있고 가격도 착해서 떡볶이 땡길 때 추천\n핫도그는 냉동인데\n" +
                    "떡볶이는 맛있음\n" +
                    "맛도 있고 가격도 착해서 떡볶이 땡길 때 추천",
            reviewImages = null,
            tags = listOf("우삼겹떡볶이*핫도그", "오뎅국"),
            isGood = true
        ),
        Review(
            userName = "부대찌개사랑",
            reviewStar = 2.2f,
            profileImage = null,
            writtenTime = LocalDateTime.of(2024, 8, 20, 0, 0),
            goodCount = 6,
            description = "핫도그는 냉동인데\n떡볶이는 맛있음\n맛도 있고 가격도 착해서 떡볶이 땡길 때 추천\n핫도그는 냉동인데\n" +
                    "떡볶이는 맛있음\n" +
                    "맛도 있고 가격도 착해서 떡볶이 땡길 때 추천",
            reviewImages = listOf(R.drawable.example_menu_big_1, R.drawable.example_menu_big_1, R.drawable.example_menu_big_1,R.drawable.example_menu_big_1),
            tags = listOf("우삼겹떡볶이*핫도그", "오뎅국"),
            isGood = true
        )

    )

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        item { HeaderSection(menu, navController) }
        item { Spacer(Modifier.height(19.8.dp)) }
        item { GrayHorizontalDivider(Modifier.padding(horizontal = 21.3.dp)) }
        item { Spacer(Modifier.height(19.8.dp)) }
        item { ReviewHeaderSection(menu, onlyPhoto) { onlyPhoto = it } }

        items(reviews) { review ->
            ReviewItem(review = review, menu = menu)
            Spacer(Modifier.height(17.dp))
            HorizontalDivider(thickness = 7.dp, color = Gray_F6F8F8, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(28.dp))
        }
    }
}








