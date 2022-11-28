package by.bulba.watch.elektronika.renderer.impl

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import by.bulba.watch.elektronika.data.watchface.WatchFaceData
import by.bulba.watch.elektronika.renderer.RendererDrawer
import by.bulba.watch.elektronika.renderer.calcFactorByHeight
import by.bulba.watch.elektronika.renderer.createTextPaint
import java.time.ZonedDateTime

internal class BottomLabelRendererDrawer(
    private val context: Context,
    private val watchFaceData: WatchFaceData,
) : RendererDrawer {
    private var paint: Paint? = null
    private var labelBounds: Rect? = null

    override fun draw(canvas: Canvas, bounds: Rect, zonedDateTime: ZonedDateTime) {
        val textPaint: Paint = paint ?: context.createTextPaint(
            bounds = bounds,
            textColor = watchFaceData.getPalette().labelTextColor,
            textFactor = ElektronikaFactors.BottomLabel.TEXT_SIZE_FACTOR,
        ) { paint = it; it }
        val text = watchFaceData.bottomLabel.label.get(context).toString()
        if (labelBounds == null) {
            val newLabelBounds = Rect()
            textPaint.getTextBounds(text, 0, text.length, newLabelBounds)
            labelBounds = newLabelBounds
        }
        val safeLabelBounds = requireNotNull(labelBounds)

        canvas.drawText(
            text,
            bounds.centerX().toFloat() - (safeLabelBounds.width() / 2),
            bounds.calcFactorByHeight(ElektronikaFactors.BottomLabel.START_Y_FACTOR),
            textPaint,
        )
    }
}