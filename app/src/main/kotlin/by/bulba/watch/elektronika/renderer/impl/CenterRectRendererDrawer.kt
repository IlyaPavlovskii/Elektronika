package by.bulba.watch.elektronika.renderer.impl

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import by.bulba.watch.elektronika.data.watchface.WatchFaceData
import by.bulba.watch.elektronika.renderer.RendererDrawer
import by.bulba.watch.elektronika.renderer.calcFactor
import by.bulba.watch.elektronika.renderer.calcFactorByHeight
import java.time.ZonedDateTime

internal class CenterRectRendererDrawer(
    private val context: Context,
    private val watchFaceData: WatchFaceData,
    private val corners: FloatArray = DEFAULT_CORNERS,
) : RendererDrawer {

    private val fillPaint = Paint().apply {
        style = Paint.Style.FILL
        color = watchFaceData.centerRect.fillColor.get(context)
    }
    private val strokePaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = watchFaceData.centerRect.strokeWidth.getFloat(context)
        color = watchFaceData.centerRect.strokeColor.get(context)
    }
    private var lastKnownPath: Path? = null

    override fun draw(canvas: Canvas, bounds: Rect, zonedDateTime: ZonedDateTime) {
        val targetPath = lastKnownPath ?: run {
            val width = bounds.width().calcFactor(ElektronikaFactors.CenterRect.WIDTH_FACTOR)
            val height = bounds.height().calcFactor(ElektronikaFactors.CenterRect.HEIGHT_FACTOR)
            val startX = (bounds.centerX() - (width / 2))
            val startY = bounds.calcFactorByHeight(ElektronikaFactors.CenterRect.START_Y_FACTOR)
            val rect = RectF(
                startX,
                startY,
                (startX + width),
                (startY + height)
            )
            val path = Path()
            path.addRoundRect(rect, corners, Path.Direction.CW)
            this.lastKnownPath = path
            path
        }

        canvas.drawPath(targetPath, fillPaint)
        canvas.drawPath(targetPath, strokePaint)
    }

    companion object {
        private val DEFAULT_CORNERS = floatArrayOf(
            16f, 16f, // Top left radius in px
            16f, 16f, // Top right radius in px
            16f, 16f, // Bottom right radius in px
            16f, 16f, // Bottom left radius in px
        )
    }
}

