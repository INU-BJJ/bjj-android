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
        // 주어진 요구사항에 맞춘 값들
        val circleRadiusDp = 79.dp / 2 // 지름 79dp의 원 반지름
        val circlePaddingDp = 1.dp
        val visibleCircleHeightDp = 46.dp  // 원의 위쪽 부분만 46dp 보임
        val cornerRadiusDp = 14.dp

        // 실제 px 단위로 변환
        val circleRadiusPx = with(density) { circleRadiusDp.toPx() }
        val circlePaddingPx = with(density) { circlePaddingDp.toPx() }
        val visibleCircleHeightPx = with(density) { visibleCircleHeightDp.toPx() }
        val cornerRadiusPx = with(density) { cornerRadiusDp.toPx() }

        // 패딩 적용된 원의 반지름
        val paddedCircleRadiusPx = circleRadiusPx - circlePaddingPx

        // 원의 중심 X좌표
        val circleCenterX = size.width / 2

        // 사각형과 원의 겹치는 부분 계산
        // 원의 위쪽 46dp만 보이게 하려면, 원의 전체 높이에서 보이는 부분을 뺀 만큼 사각형이 원을 덮어야 함
        val rectOverlapY = visibleCircleHeightPx

        // Path를 만들어서, 원 + 둥근사각형을 이어 붙임
        val path = Path().apply {
            // (1) 상단 원 (중앙) - 완벽한 원 모양
            addOval(
                Rect(
                    left = circleCenterX - paddedCircleRadiusPx,
                    top = 0f,
                    right = circleCenterX + paddedCircleRadiusPx,
                    bottom = paddedCircleRadiusPx * 2
                )
            )

            // (2) 원 아래로 둥근 사각형
            // 사각형은 원의 위쪽 46dp만 보이도록 배치
            addRoundRect(
                RoundRect(
                    rect = Rect(
                        left = 0f,
                        top = rectOverlapY, // 원의 위쪽 46dp만 보이도록 조정
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