package by.bulba.watch.elektronika.data.watchface

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import by.bulba.watch.elektronika.R
import by.bulba.watch.elektronika.utils.wrapper.ColorWrapper

internal enum class PaletteStyle(
    val id: Identifier,
    @StringRes val nameResourceId: Int,
    @DrawableRes val complicationStyleDrawableId: Int,
    val labelTextColor: ColorWrapper,
    val clockTextColor: ColorWrapper,
    val strokeColor: ColorWrapper,
    val backgroundColor: ColorWrapper,
    val secondaryBackgroundColor: ColorWrapper,
    val layerColor: ColorWrapper,
) {
    AMBIENT(
        id = Identifier("ambient_style_id"),
        nameResourceId = R.string.ambient_style_name,
        complicationStyleDrawableId = R.drawable.complication_ambient_style,
        labelTextColor = ColorWrapper.Id(R.color.elektronika__ambient__label_text_color),
        clockTextColor = ColorWrapper.Id(R.color.elektronika__ambient__clock_text_color),
        strokeColor = ColorWrapper.Id(R.color.elektronika__ambient__stroke_color),
        secondaryBackgroundColor = ColorWrapper.Id(R.color.elektronika__ambient__secondary_background_color),
        backgroundColor = ColorWrapper.Id(R.color.elektronika__ambient__background_color),
        layerColor = ColorWrapper.Id(R.color.elektronika__ambient__layer_color)
    ),

    PRIMARY(
        id = Identifier("primary_style_id"),
        nameResourceId = R.string.primary_style_name,
        complicationStyleDrawableId = R.drawable.complication_primary_style,
        labelTextColor = ColorWrapper.Id(R.color.elektronika__primary__label_text_color),
        clockTextColor = ColorWrapper.Id(R.color.elektronika__primary__clock_text_color),
        strokeColor = ColorWrapper.Id(R.color.elektronika__primary__stroke_color),
        secondaryBackgroundColor = ColorWrapper.Id(R.color.elektronika__primary__secondary_background_color),
        backgroundColor = ColorWrapper.Id(R.color.elektronika__primary__background_color),
        layerColor = ColorWrapper.Id(R.color.elektronika__primary__layer_color)
    ),

    SECONDARY(
        id = Identifier("secondary_style_id"),
        nameResourceId = R.string.secondary_style_name,
        complicationStyleDrawableId = R.drawable.complication_secondary_style,
        labelTextColor = ColorWrapper.Id(R.color.elektronika__secondary__label_text_color),
        clockTextColor = ColorWrapper.Id(R.color.elektronika__secondary__clock_text_color),
        strokeColor = ColorWrapper.Id(R.color.elektronika__secondary__stroke_color),
        secondaryBackgroundColor = ColorWrapper.Id(R.color.elektronika__secondary__secondary_background_color),
        backgroundColor = ColorWrapper.Id(R.color.elektronika__secondary__background_color),
        layerColor = ColorWrapper.Id(R.color.elektronika__secondary__layer_color)
    ),
    ;

    @JvmInline
    value class Identifier(val value: String)
}
