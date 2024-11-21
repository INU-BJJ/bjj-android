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
    val medium10: TextStyle = TextStyle(
        fontFamily = pretendard_medium,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
    ),
    val semibold24: TextStyle = TextStyle(
        fontFamily = pretendard_semibold,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
    ),
    val medium15: TextStyle = TextStyle(
        fontFamily = pretendard_medium,
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp,
    ),
    val bold15: TextStyle = TextStyle(
        fontFamily = pretendard_bold,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
    ),
    val regular13: TextStyle = TextStyle(
        fontFamily = pretendard_regular,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
    ),
    val regular11: TextStyle = TextStyle(
        fontFamily = pretendard_regular,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
    ),
    val semibold18: TextStyle = TextStyle(
        fontFamily = pretendard_semibold,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
    ),
    val semibold15: TextStyle = TextStyle(
        fontFamily = pretendard_semibold,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
    ),
    val regular20: TextStyle = TextStyle(
        fontFamily = pretendard_regular,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
    ),
    val bold18: TextStyle = TextStyle(
        fontFamily = pretendard_bold,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
    ),
    val medium13: TextStyle = TextStyle(
        fontFamily = pretendard_medium,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
    ),
    val medium11: TextStyle = TextStyle(
        fontFamily = pretendard_medium,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
    ),

)