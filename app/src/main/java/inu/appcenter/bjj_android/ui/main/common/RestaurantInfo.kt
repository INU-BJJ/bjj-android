package inu.appcenter.bjj_android.ui.main.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import inu.appcenter.bjj_android.LocalTypography
import inu.appcenter.bjj_android.ui.theme.Gray_999999
import inu.appcenter.bjj_android.utils.ImageLoader

@Composable
fun RestaurantInfo(
    modifier : Modifier = Modifier,
    restaurantName: String,
    location: String,
    weekSchedule: List<String>,
    weekendSchedule: List<String>,
    mapImageName: String,
){
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = restaurantName,
                style = LocalTypography.current.bold15.copy(
                    letterSpacing = 0.13.sp,
                    color = Gray_999999,
                    textAlign = TextAlign.Start
                ),
                modifier = Modifier
                    .weight(1f)
            )
            Text(
                text = location,
                style = LocalTypography.current.bold15.copy(
                    letterSpacing = 0.13.sp,
                    color = Gray_999999,
                    textAlign = TextAlign.Start
                ),
                modifier = Modifier
                    .weight(1f)
            )
        }
        Spacer(Modifier.height(12.dp))
        Text(
            text = "운영시간 (학기 중)",
            style = LocalTypography.current.bold15.copy(
                letterSpacing = 0.13.sp,
                color = Gray_999999
            )
        )
        Spacer(Modifier.height(8.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RectangleShape,
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                Column (
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = "평일",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 3.dp),
                        style = LocalTypography.current.medium15.copy(
                            color = Gray_999999,
                            letterSpacing = 0.13.sp,
                            textAlign = TextAlign.Center
                        )
                    )
                    HorizontalDivider(modifier = Modifier.fillMaxWidth(), color = Gray_999999, thickness = 1.dp)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        weekSchedule.forEachIndexed { index, schedule ->
                            val mealText = when (index) {
                                0 -> "중식 "
                                1 -> "석식 "
                                else -> ""
                            }
                            val displayText = if (schedule.isBlank()) "휴점" else "$mealText$schedule"
                            Text(
                                text = displayText,
                                style = LocalTypography.current.medium15.copy(
                                    color = Gray_999999,
                                    letterSpacing = 0.13.sp,
                                ),
                                modifier = Modifier
                            )
                        }
                    }
                }
                VerticalDivider(modifier = Modifier.fillMaxHeight(), color = Gray_999999, thickness = 1.dp)
                Column (
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = "주말",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 3.dp),
                        style = LocalTypography.current.medium15.copy(
                            color = Gray_999999,
                            letterSpacing = 0.13.sp,
                            textAlign = TextAlign.Center
                        )
                    )
                    HorizontalDivider(modifier = Modifier.fillMaxWidth(), color = Gray_999999, thickness = 1.dp)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        weekendSchedule.forEachIndexed { index, schedule ->
                            val mealText = when (index) {
                                0 -> "중식 "
                                1 -> "석식 "
                                else -> ""
                            }
                            val displayText = if (schedule.isBlank()) "휴점" else "$mealText$schedule"
                            Text(
                                text = displayText,
                                style = LocalTypography.current.medium15.copy(
                                    color = Gray_999999,
                                    letterSpacing = 0.13.sp,
                                ),
                                modifier = Modifier
                            )
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(212.dp)
                .clip(RoundedCornerShape(3.dp))
        ) {
            ImageLoader.MapImage(
                imageName = mapImageName,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}