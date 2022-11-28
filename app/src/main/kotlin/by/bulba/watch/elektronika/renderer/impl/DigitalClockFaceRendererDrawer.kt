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
import java.time.format.DateTimeFormatter

internal class DigitalClockFaceRendererDrawer(
    private val context: Context,
    private val watchFaceData: WatchFaceData,
    private val dayFormatter: DateTimeFormatter = DEFAULT_DAY_FORMATTER,
    private val dateFormatter: DateTimeFormatter = DEFAULT_DATE_FORMATTER,
) : RendererDrawer {

    private var lastKnownClockPaint: Paint? = null
    private var formattedClockBounds: Rect? = null
    private var lastKnownDatePaint: Paint? = null
    private var formattedDayBounds: Rect? = null

    override fun draw(canvas: Canvas, bounds: Rect, zonedDateTime: ZonedDateTime) {
        val clockPaint = lastKnownClockPaint ?: context.createTextPaint(
            bounds = bounds,
            textColor = watchFaceData.getPalette().clockTextColor,
            textFactor = ElektronikaFactors.DigitalClock.CLOCK_TEXT_SIZE_FACTOR,
        ) { lastKnownClockPaint = it;it }

        val timeBounds: Rect = formattedClockBounds ?: run {
            formattedClockBounds = Rect()
            val snapshotTime = watchFaceData.mode.snapshotTime
            clockPaint.getTextBounds(snapshotTime, 0, snapshotTime.length, formattedClockBounds)
            requireNotNull(formattedClockBounds)
        }
        val formattedTime = formatTime(zonedDateTime)
        canvas.drawText(
            formattedTime,
            bounds.centerX().toFloat() - (timeBounds.width() / 2),
            bounds.calcFactorByHeight(ElektronikaFactors.DigitalClock.START_CLOCK_Y_FACTOR),
            clockPaint,
        )
        val datePaint = lastKnownDatePaint ?: context.createTextPaint(
            bounds = bounds,
            textColor = watchFaceData.getPalette().clockTextColor,
            textFactor = ElektronikaFactors.DigitalClock.DATE_TEXT_SIZE_FACTOR,
        ) { lastKnownDatePaint = it;it }
        val dayBounds: Rect = formattedClockBounds ?: run {
            formattedDayBounds = Rect()
            datePaint.getTextBounds(SNAPSHOT_DAY, 0, SNAPSHOT_DAY.length, formattedDayBounds)
            requireNotNull(formattedDayBounds)
        }

        val formattedDay = zonedDateTime.format(dayFormatter)
        val formattedDate = zonedDateTime.format(dateFormatter)
        canvas.drawText(
            formattedDay,
            bounds.centerX().toFloat() - (dayBounds.width() / 2),
            bounds.calcFactorByHeight(ElektronikaFactors.DigitalClock.START_DATE_Y_FACTOR),
            datePaint,
        )
        canvas.drawText(
            formattedDate,
            bounds.centerX().toFloat(),
            bounds.calcFactorByHeight(ElektronikaFactors.DigitalClock.START_DATE_Y_FACTOR),
            datePaint,
        )

    }

    private fun formatTime(zonedDateTime: ZonedDateTime): String {
        val timeFormatter = when (watchFaceData.mode) {
            WatchFaceData.Mode.AMBIENT ->
                watchFaceData.digitalClock.timeFormatType.ambientTimeFormatter

            WatchFaceData.Mode.ACTIVE ->
                watchFaceData.digitalClock.timeFormatType.dateTimeFormatter
        }
        return zonedDateTime.format(timeFormatter)
    }

    companion object {
        private const val SNAPSHOT_DAY = "MON"
        private val DEFAULT_DAY_FORMATTER = DateTimeFormatter.ofPattern("EEE")
        private val DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM d")
    }
}