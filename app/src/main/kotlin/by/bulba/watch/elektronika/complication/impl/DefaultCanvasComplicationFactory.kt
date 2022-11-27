package by.bulba.watch.elektronika.complication.impl

import android.content.Context
import androidx.wear.watchface.CanvasComplication
import androidx.wear.watchface.CanvasComplicationFactory
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.complications.rendering.CanvasComplicationDrawable
import androidx.wear.watchface.complications.rendering.ComplicationDrawable
import by.bulba.watch.elektronika.R

private const val DEFAULT_COMPLICATION_STYLE_DRAWABLE_ID = R.drawable.complication_red_style

internal class DefaultCanvasComplicationFactory(
    private val context: Context,
    private val drawableId: Int = DEFAULT_COMPLICATION_STYLE_DRAWABLE_ID,
) : CanvasComplicationFactory {
    override fun create(
        watchState: WatchState,
        invalidateCallback: CanvasComplication.InvalidateCallback
    ): CanvasComplication = CanvasComplicationDrawable(
        requireNotNull(ComplicationDrawable.getDrawable(context, drawableId)),
        watchState,
        invalidateCallback
    )
}