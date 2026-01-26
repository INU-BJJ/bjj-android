package inu.appcenter.bjj_android.feature.review.presentation.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.shared.theme.Gray_B9B9B9

@Composable
fun PhotoCountingCalculator(photoCounting: Int) {
    Text(
        text = "$photoCounting/4",
        style = LocalTypography.current.regular13.copy(
            letterSpacing = 0.13.sp,
            lineHeight = 17.sp,
        ),
        color = Gray_B9B9B9
    )
}