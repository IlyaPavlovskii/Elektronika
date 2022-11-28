package by.bulba.watch.elektronika.renderer

import android.graphics.Canvas
import android.graphics.Rect
import java.time.ZonedDateTime

internal class CompositeRendererDrawer(
    private vararg val rendererDrawers: RendererDrawer,
) : RendererDrawer {
    override fun draw(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
    ) {
        rendererDrawers.forEach { renderer ->
            renderer.draw(canvas, bounds, zonedDateTime)
        }
    }
}

internal fun compositeRendererDrawer(
    vararg rendererDrawers: RendererDrawer
): RendererDrawer = CompositeRendererDrawer(*rendererDrawers)

internal fun compositeRendererDrawer(
    rendererDrawers: List<RendererDrawer>
): RendererDrawer = CompositeRendererDrawer(*rendererDrawers.toTypedArray())