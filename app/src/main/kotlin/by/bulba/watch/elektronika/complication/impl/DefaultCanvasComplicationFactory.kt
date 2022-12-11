package by.bulba.watch.elektronika.complication.impl

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.wear.watchface.CanvasComplication
import androidx.wear.watchface.CanvasComplicationFactory
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.complications.rendering.CanvasComplicationDrawable
import androidx.wear.watchface.complications.rendering.ComplicationDrawable
import by.bulba.watch.elektronika.R

internal class DefaultCanvasComplicationFactory(
    private val context: Context,
    @DrawableRes private val drawableId: Int,
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