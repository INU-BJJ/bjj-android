package inu.appcenter.bjj_android.shared.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

// 패딩 값들을 위한 클래스 정의
data class AppPadding(
    val xsmall: androidx.compose.ui.unit.Dp = 10.dp,
    val small: androidx.compose.ui.unit.Dp = 13.dp,
    val medium: androidx.compose.ui.unit.Dp = 15.dp,
    val large: androidx.compose.ui.unit.Dp = 21.dp,
    val xlarge: androidx.compose.ui.unit.Dp = 24.dp,
    val topBarPadding: androidx.compose.ui.unit.Dp = 20.dp,
    val iconOffset: androidx.compose.ui.unit.Dp = 4.5.dp
)

// CompositionLocal 생성
val LocalAppPadding = staticCompositionLocalOf { AppPadding() }

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun Bjj_androidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {


    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // AppPadding 제공
    CompositionLocalProvider(
        LocalAppPadding provides AppPadding(),
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

// MaterialTheme에 paddings 확장 프로퍼티 추가
val MaterialTheme.paddings: AppPadding
    @Composable
    get() = LocalAppPadding.current