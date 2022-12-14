package by.bulba.watch.elektronika.renderer.impl

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import by.bulba.watch.elektronika.data.watchface.WatchFaceData
import by.bulba.watch.elektronika.renderer.RendererDrawer
import by.bulba.watch.elektronika.renderer.calcFactorByHeight
import by.bulba.watch.elektronika.renderer.calcFactorByWidth
import by.bulba.watch.elektronika.renderer.createTextPaint
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.abs

internal class DigitalClockFaceRendererDrawer(
    private val context: Context,
    private val watchFaceData: WatchFaceData,
    private val dayFormatter: DateTimeFormatter = DEFAULT_DAY_FORMATTER,
    private val dateFormatter: DateTimeFormatter = DEFAULT_DATE_FORMATTER,
) : RendererDrawer {

    private var lastKnownClockPaint: Paint? = null
    private var formattedClockBounds: Rect? = null
    private var lastKnownDatePaint: Paint? = null

    override fun draw(canvas: Canvas, bounds: Rect, zonedDateTime: ZonedDateTime) {
        val clockPaint = lastKnownClockPaint ?: context.createTextPaint(
            bounds = bounds,
            textColor = watchFaceData.getPalette().clockTextColor,
            textFactor = watchFaceData.digitalClock.digitalClockTimeFormat.clockTextSizeFactor,
        ) { lastKnownClockPaint = it;it }

        val timeBounds: Rect = formattedClockBounds ?: run {
            formattedClockBounds = Rect()
            val snapshotTime = when (watchFaceData.mode) {
                WatchFaceData.Mode.AMBIENT ->
                    watchFaceData.digitalClock.digitalClockTimeFormat.ambientSnapshotTime

                WatchFaceData.Mode.ACTIVE ->
                    watchFaceData.digitalClock.digitalClockTimeFormat.snapshotTime
            }
            clockPaint.getTextBounds(snapshotTime, 0, snapshotTime.length, formattedClockBounds)
            requireNotNull(formattedClockBounds)
        }
        val formattedTime = formatTime(zonedDateTime)
        canvas.drawText(
            formattedTime,
            (bounds.width() - abs(timeBounds.width()))/2f,
            bounds.calcFactorByHeight(ElektronikaFactors.DigitalClock.START_CLOCK_Y_FACTOR),
            clockPaint,
        )
        val datePaint = lastKnownDatePaint ?: context.createTextPaint(
            bounds = bounds,
            textColor = watchFaceData.getPalette().clockTextColor,
            textFactor = ElektronikaFactors.DigitalClock.DATE_TEXT_SIZE_FACTOR,
        ) { lastKnownDatePaint = it;it }
        val formattedDay = zonedDateTime.format(dayFormatter)
        val formattedDate = zonedDateTime.format(dateFormatter)
        canvas.drawText(
            formattedDay,
            bounds.calcFactorByWidth(ElektronikaFactors.DigitalClock.START_DATE_X_FACTOR),
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
                watchFaceData.digitalClock.digitalClockTimeFormat.ambientTimeFormatter

            WatchFaceData.Mode.ACTIVE ->
                watchFaceData.digitalClock.digitalClockTimeFormat.dateTimeFormatter
        }
        return zonedDateTime.format(timeFormatter)
    }

    companion object {
        private val DEFAULT_DAY_FORMATTER = DateTimeFormatter.ofPattern("EEE")
        private val DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM d")
    }
}