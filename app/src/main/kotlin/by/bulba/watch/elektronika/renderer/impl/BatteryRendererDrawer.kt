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
import by.bulba.watch.elektronika.renderer.createTextPaint
import java.time.ZonedDateTime

internal class BatteryRendererDrawer(
    private val context: Context,
    private val watchFaceData: WatchFaceData,
    private val corners: FloatArray = DEFAULT_CORNERS,
) : RendererDrawer {

    private val iconDrawable = watchFaceData.battery.getIcon().get(context)
    private val levelPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = watchFaceData.battery.levelColor.get(context)
    }
    private val layerPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = watchFaceData.battery.layerColor.get(context)
    }
    private var levelTextPaint: Paint? = null
    private var lastKnownLayerPath: Path? = null
    private var lastKnownLevelPath: Path? = null

    override fun draw(canvas: Canvas, bounds: Rect, zonedDateTime: ZonedDateTime) {
        val targetLayerPath = lastKnownLayerPath ?: run {
            val width = bounds.width().calcFactor(ElektronikaFactors.Battery.WIDTH_FACTOR)
            val height = bounds.height().calcFactor(ElektronikaFactors.Battery.HEIGHT_FACTOR)
            val startX = (bounds.centerX() - (width / 2))
            val startY = bounds.calcFactorByHeight(ElektronikaFactors.Battery.START_Y_FACTOR)
            val rect = RectF(
                startX,
                startY,
                (startX + width),
                (startY + height)
            )
            val path = Path()
            path.addRoundRect(rect, corners, Path.Direction.CW)
            this.lastKnownLayerPath = path
            path
        }
        val targetLevelPath = lastKnownLevelPath ?: run {
            val width = bounds.width().calcFactor(ElektronikaFactors.Battery.WIDTH_FACTOR)
            val height = bounds.height().calcFactor(ElektronikaFactors.Battery.HEIGHT_FACTOR)
            val startX = (bounds.centerX() - (width / 2))
            val startY = bounds.calcFactorByHeight(ElektronikaFactors.Battery.START_Y_FACTOR)
            val rect = RectF(
                startX,
                startY,
                (startX + (width * watchFaceData.battery.level.getFloatValue())),
                (startY + height)
            )
            val path = Path()
            path.addRoundRect(rect, corners, Path.Direction.CW)
            this.lastKnownLevelPath = path
            path
        }
        canvas.drawPath(targetLayerPath, layerPaint)
        canvas.drawPath(targetLevelPath, levelPaint)

        val iconSize = bounds.calcFactorByHeight(ElektronikaFactors.Battery.ICON_WIDTH_FACTOR)
            .toInt()
        val dx = bounds.width() / 2f - iconSize
        val dy = bounds.calcFactorByHeight(ElektronikaFactors.Battery.START_ICON_Y_FACTOR)
        canvas.translate(dx, dy)
        iconDrawable.setBounds(0, 0, iconSize, iconSize)
        iconDrawable.draw(canvas)
        canvas.translate(-dx, -dy)

        val percentagePaint = levelTextPaint ?: context.createTextPaint(
            textColor = watchFaceData.battery.levelColor,
            textSize = iconSize.toFloat(),
        ) { levelTextPaint = it;it }
        canvas.drawText(
            watchFaceData.battery.level.value.toString(),
            dx + (iconSize * ElektronikaFactors.Battery.BATTERY_LEVEL_ICON_WIDTH_FACTOR.factor),
            dy + (iconSize * ElektronikaFactors.Battery.BATTERY_LEVEL_ICON_HEIGHT_FACTOR.factor),
            percentagePaint
        )
    }

    companion object {
        private val DEFAULT_CORNERS = floatArrayOf(
            4f, 4f, // Top left radius in px
            4f, 4f, // Top right radius in px
            4f, 4f, // Bottom right radius in px
            4f, 4f, // Bottom left radius in px
        )
    }
}