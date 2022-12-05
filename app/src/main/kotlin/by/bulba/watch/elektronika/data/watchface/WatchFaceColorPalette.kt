package by.bulba.watch.elektronika.data.watchface

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

internal data class WatchFaceColorPalette(
    @ColorRes val labelTextColor: Int,
    @ColorRes val clockTextColor: Int,
    @ColorRes val strokeColor: Int,
    @ColorRes val backgroundColor: Int,
    @ColorRes val secondaryBackgroundColor: Int,
    @ColorRes val layerColor: Int,
    @DrawableRes val complicationStyleDrawableId: Int,
)

internal fun PaletteStyle.toWatchFaceColorPalette(): WatchFaceColorPalette {
    return WatchFaceColorPalette(
        labelTextColor = labelTextColor,
        clockTextColor = clockTextColor,
        strokeColor = strokeColor,
        backgroundColor = backgroundColor,
        secondaryBackgroundColor = secondaryBackgroundColor,
        layerColor = layerColor,
        complicationStyleDrawableId = complicationStyleDrawableId,
    )
}
