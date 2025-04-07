package inu.appcenter.bjj_android.utils

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

/**
 * 원 + 사각형을 결합한 커스텀 Shape
 * 상단에 원이 있고 하단에 둥근 모서리 사각형이 있는 형태
 * 파라미터를 통해 크기 조정이 가능하도록 설계됨
 *
 * @param circleDiameter 상단 원의 지름
 * @param circlePadding 원 주변의 패딩
 * @param visibleCircleHeight 원의 상단에 보이는 높이
 * @param cornerRadius 하단 사각형의 모서리 둥글기
 */
class CircleRectShape(
    private val circleDiameter: Dp = 79.dp,
    private val circlePadding: Dp = 1.dp,
    private val visibleCircleHeight: Dp = 46.dp,
    private val cornerRadius: Dp = 14.dp
) : Shape {
    override fun createOutline(
        size: androidx.compose.ui.geometry.Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        // 주어진 요구사항에 맞춘 값들 변환
        val circleRadiusPx = with(density) { circleDiameter.toPx() / 2 }
        val circlePaddingPx = with(density) { circlePadding.toPx() }
        val visibleCircleHeightPx = with(density) { visibleCircleHeight.toPx() }
        val cornerRadiusPx = with(density) { cornerRadius.toPx() }

        // 패딩 적용된 원의 반지름
        val paddedCircleRadiusPx = circleRadiusPx - circlePaddingPx

        // 원의 중심 X좌표
        val circleCenterX = size.width / 2

        // 사각형과 원의 겹치는 부분 계산
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
            // 사각형은 원의 위쪽 visibleCircleHeight만큼만 보이도록 배치
            addRoundRect(
                RoundRect(
                    rect = Rect(
                        left = 0f,
                        top = rectOverlapY,
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