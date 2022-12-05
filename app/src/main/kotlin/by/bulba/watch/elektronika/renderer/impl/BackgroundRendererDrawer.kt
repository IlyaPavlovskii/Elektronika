package by.bulba.watch.elektronika.renderer.impl

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import by.bulba.watch.elektronika.data.watchface.WatchFaceData
import by.bulba.watch.elektronika.renderer.RendererDrawer
import java.time.ZonedDateTime

internal class BackgroundRendererDrawer(
    context: Context,
    watchFaceData: WatchFaceData,
) : RendererDrawer {

    private val backgroundColor = context.getColor(watchFaceData.getPalette().backgroundColor)

    override fun draw(canvas: Canvas, bounds: Rect, zonedDateTime: ZonedDateTime) {
        canvas.drawColor(backgroundColor)
    }
}