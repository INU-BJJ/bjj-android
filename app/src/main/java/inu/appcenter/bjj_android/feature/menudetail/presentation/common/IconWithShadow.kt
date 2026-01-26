package inu.appcenter.bjj_android.feature.menudetail.presentation.common

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb


@Composable
fun IconWithShadow(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified,
    shadowColor: Color = Color.Black.copy(alpha = 0.25f),
    shadowOffset: Pair<Float, Float> = Pair(4f, 4f),  // x and y offset in pixels
    shadowRadius: Float = 8f
) {
    Canvas(modifier = modifier) {
        drawIntoCanvas { canvas ->
            val paint = Paint().asFrameworkPaint()
            paint.color = shadowColor.toArgb()
            paint.maskFilter = BlurMaskFilter(shadowRadius, BlurMaskFilter.Blur.NORMAL)

            // Draw shadow
            canvas.nativeCanvas.save()
            canvas.nativeCanvas.translate(shadowOffset.first, shadowOffset.second)
            with(painter) {
                draw(size = size, colorFilter = ColorFilter.tint(shadowColor))
            }
            canvas.nativeCanvas.restore()

            // Reset paint for icon
            paint.maskFilter = null
        }

        // Draw icon
        with(painter) {
            draw(
                size = size,
                colorFilter = if (tint == Color.Unspecified) null else ColorFilter.tint(tint)
            )
        }
    }
}