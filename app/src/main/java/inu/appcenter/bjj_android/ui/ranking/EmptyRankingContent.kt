package inu.appcenter.bjj_android.ui.ranking

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.R
import inu.appcenter.bjj_android.ui.theme.Gray_D9D9D9

@Composable
fun EmptyRankingContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 50.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.warning),
            contentDescription = "랭킹 없음 경고",
            modifier = Modifier
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "현재 랭킹 정보가 없어요!",
            style = LocalTypography.current.bold18.copy(
                color = Gray_D9D9D9,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "더 많은 리뷰를 작성해주세요.\n랭킹에 오를 수 있어요.",
            style = LocalTypography.current.regular13.copy(
                color = Gray_D9D9D9,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(horizontal = 20.dp)
        )
    }
}