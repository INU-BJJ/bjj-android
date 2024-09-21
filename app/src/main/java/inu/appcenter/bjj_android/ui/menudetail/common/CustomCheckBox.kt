package inu.appcenter.bjj_android.ui.menudetail.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.ui.theme.Gray_999999
import inu.appcenter.bjj_android.ui.theme.Orange_FF7800

@Composable
fun CheckBox(
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit
) {
    var checkBoxState by remember { mutableStateOf(checked) }

    Card(
        modifier = modifier
            .size(15.dp)
            .clickable {
                checkBoxState = !checkBoxState
                onCheckedChange(checkBoxState)
            },
        shape = RoundedCornerShape(3.dp),
    ) {
        if (checkBoxState) CheckBoxSelected()
        else CheckBoxUnSelected()
    }
}

@Composable
private fun CheckBoxSelected() {
    Box(
        modifier = Modifier
            .size(15.dp)
            .clip(RoundedCornerShape(3.dp))
            .background(Orange_FF7800),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.check),
            contentDescription = "체크 아이콘",
            tint = Color.Unspecified
        )
    }
}

@Composable
private fun CheckBoxUnSelected() {
    Box(
        modifier = Modifier
            .size(15.dp)
            .clip(RoundedCornerShape(3.dp))
            .background(Color.White)
            .border(
                width = 1.5.dp,
                color = Gray_999999,
                shape = RoundedCornerShape(3.dp)
            ),
    )
}