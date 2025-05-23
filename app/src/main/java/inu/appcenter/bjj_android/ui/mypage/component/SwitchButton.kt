package inu.appcenter.bjj_android.ui.mypage.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import inu.appcenter.bjj_android.ui.theme.Orange_FF7800

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwitchButton(
    checked: Boolean = true,
    onCheckedChange: (Boolean) -> Unit = {}
) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Orange_FF7800
            )
        )
    }
}