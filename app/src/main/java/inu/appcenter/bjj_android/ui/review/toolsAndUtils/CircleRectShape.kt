package inu.appcenter.bjj_android.ui.review.toolsAndUtils

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

object CircleRectShape : Shape {
    override fun createOutline(
        size: androidx.compose.ui.geometry.Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        // 원하는 원의 반지름과 사각형 모서리 둥글림 정도를 dp로 정의
        val circleRadiusDp = 40.dp
        val cornerRadiusDp = 14.dp

        // 실제 px 단위로 변환
        val circleRadiusPx = with(density) { circleRadiusDp.toPx() }
        val cornerRadiusPx = with(density) { cornerRadiusDp.toPx() }

        // Path를 만들어서, 원 + 둥근사각형을 이어 붙임
        val path = Path().apply {
            // (1) 상단 원 (중앙)
            val circleCenterX = size.width / 2
            // top=0 ~ bottom=(circleRadiusPx*2) 영역에 원을 그릴 것
            addOval(
                Rect(
                    left = circleCenterX - circleRadiusPx,
                    top = 0f,
                    right = circleCenterX + circleRadiusPx,
                    bottom = circleRadiusPx * 2
                )
            )

            // (2) 원 바로 아래로 둥근 사각형
            // 원의 하단과 사각형의 상단이 맞닿도록 top = circleRadiusPx
            addRoundRect(
                RoundRect(
                    rect = Rect(
                        left = 0f,
                        top = circleRadiusPx, // 원의 절반 아래부터 사각형 시작
                        right = size.width,
                        bottom = size.height
                    ),
                    cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx)
                )
            )
        }

        return Outline.Generic(path)
    }
}
