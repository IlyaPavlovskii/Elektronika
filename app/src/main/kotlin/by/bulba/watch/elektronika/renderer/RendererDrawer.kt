package by.bulba.watch.elektronika.renderer

import android.graphics.Canvas
import android.graphics.Rect
import java.time.ZonedDateTime

internal fun interface RendererDrawer {
    fun draw(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
    )
}