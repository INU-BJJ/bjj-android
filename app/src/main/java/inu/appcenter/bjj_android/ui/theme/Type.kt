package inu.appcenter.bjj_android.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import inu.appcenter.bjj_android.R

private val pretendard_bold = FontFamily(
    Font(R.font.pretendard_bold)
)

private val pretendard_semibold = FontFamily(
    Font(R.font.pretendard_semibold)
)

private val pretendard_medium = FontFamily(
    Font(R.font.pretendard_medium)
)

private val pretendard_regular = FontFamily(
    Font(R.font.pretendard_regular)
)


// Set of Material typography styles to start with
val Typography = Typography(
    headlineLarge = TextStyle(
        fontFamily = pretendard_semibold,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = pretendard_bold,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        letterSpacing = 0.13.sp
    )
)

data class AppTypography(
    val main_headline: TextStyle = TextStyle(
        fontFamily = pretendard_semibold,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
    ),
    val main_restaurantButton: TextStyle = TextStyle(
        fontFamily = pretendard_medium,
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp,
        letterSpacing = 0.13.sp,
        lineHeight = 15.sp
    ),
    val main_menuName: TextStyle = TextStyle(
        fontFamily = pretendard_bold,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        letterSpacing = 0.13.sp,
        lineHeight = 15.sp
    ),
    val main_menuPrice: TextStyle = TextStyle(
        fontFamily = pretendard_regular,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        letterSpacing = 0.13.sp,
        lineHeight = 17.sp
    ),
    val main_menuRestaurant: TextStyle = TextStyle(
        fontFamily = pretendard_regular,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        letterSpacing = 0.13.sp,
        lineHeight = 15.sp
    ),
    val main_menuInfo: TextStyle = TextStyle(
        fontFamily = pretendard_semibold,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        letterSpacing = 0.13.sp,
        lineHeight = 15.sp
    ),
    val main_menuReviewStar: TextStyle = TextStyle(
        fontFamily = pretendard_regular,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        letterSpacing = 0.13.sp,
        lineHeight = 17.sp
    ),
)