package by.bulba.watch.elektronika.data.watchface

import androidx.annotation.DrawableRes
import by.bulba.watch.elektronika.utils.wrapper.ColorWrapper

internal data class WatchFaceColorPalette(
    val labelTextColor: ColorWrapper,
    val clockTextColor: ColorWrapper,
    val strokeColor: ColorWrapper,
    val backgroundColor: ColorWrapper,
    val secondaryBackgroundColor: ColorWrapper,
    val layerColor: ColorWrapper,
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
