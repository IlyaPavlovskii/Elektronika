package by.bulba.watch.elektronika.data.watchface

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import by.bulba.watch.elektronika.R

internal enum class PaletteStyle(
    val id: Identifier,
    @StringRes val nameResourceId: Int,
    @DrawableRes val complicationStyleDrawableId: Int,
    @DrawableRes val iconResId: Int,
    @ColorRes val labelTextColor: Int,
    @ColorRes val clockTextColor: Int,
    @ColorRes val strokeColor: Int,
    @ColorRes val backgroundColor: Int,
    @ColorRes val secondaryBackgroundColor: Int,
    @ColorRes val layerColor: Int,
) {
    PRIMARY(
        id = Identifier("primary_style_id"),
        nameResourceId = R.string.primary_style_name,
        complicationStyleDrawableId = R.drawable.complication_primary_style,
        iconResId = R.drawable.watch_face_preview__primary,
        labelTextColor = R.color.elektronika__primary__label_text_color,
        clockTextColor = R.color.elektronika__primary__clock_text_color,
        strokeColor = R.color.elektronika__primary__stroke_color,
        secondaryBackgroundColor = R.color.elektronika__primary__secondary_background_color,
        backgroundColor = R.color.elektronika__primary__background_color,
        layerColor = R.color.elektronika__primary__layer_color,
    ),

    SECONDARY(
        id = Identifier("secondary_style_id"),
        nameResourceId = R.string.secondary_style_name,
        complicationStyleDrawableId = R.drawable.complication_secondary_style,
        iconResId = R.drawable.watch_face_preview__secondary,
        labelTextColor = R.color.elektronika__secondary__label_text_color,
        clockTextColor = R.color.elektronika__secondary__clock_text_color,
        strokeColor = R.color.elektronika__secondary__stroke_color,
        secondaryBackgroundColor = R.color.elektronika__secondary__secondary_background_color,
        backgroundColor = R.color.elektronika__secondary__background_color,
        layerColor = R.color.elektronika__secondary__layer_color,
    ),
    AMBIENT(
        id = Identifier("ambient_style_id"),
        nameResourceId = R.string.ambient_style_name,
        complicationStyleDrawableId = R.drawable.complication_ambient_style,
        iconResId = R.drawable.watch_face_preview__ambient,
        labelTextColor = R.color.elektronika__ambient__label_text_color,
        clockTextColor = R.color.elektronika__ambient__clock_text_color,
        strokeColor = R.color.elektronika__ambient__stroke_color,
        secondaryBackgroundColor = R.color.elektronika__ambient__secondary_background_color,
        backgroundColor = R.color.elektronika__ambient__background_color,
        layerColor = R.color.elektronika__ambient__layer_color,
    ),
    ;

    @JvmInline
    value class Identifier(val value: String)
}
