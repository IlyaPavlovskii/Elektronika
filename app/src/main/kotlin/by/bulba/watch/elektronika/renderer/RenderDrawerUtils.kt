package by.bulba.watch.elektronika.renderer

import android.content.Context
import android.graphics.Paint
import android.graphics.Rect
import androidx.annotation.ColorRes
import by.bulba.watch.elektronika.R
import by.bulba.watch.elektronika.data.RelativeFactor

internal fun Rect.calcFactorByHeight(factor: RelativeFactor): Float = this.height().calcFactor(factor)

internal fun Rect.calcFactorByWidth(factor: RelativeFactor): Float = this.width().calcFactor(factor)

internal fun Int.calcFactor(factor: RelativeFactor): Float = this * factor.factor

fun Context.createTextPaint(
    bounds: Rect,
    textFactor: RelativeFactor,
    @ColorRes textColor: Int,
    block: (Paint) -> Paint = { it },
): Paint = createTextPaint(
    textColor = textColor,
    textSize = bounds.calcFactorByHeight(textFactor),
    block = block,
)

fun Context.createTextPaint(
    @ColorRes textColor: Int,
    textSize: Float,
    block: (Paint) -> Paint = { it },
): Paint = Paint().apply {
    isAntiAlias = true
    color = this@createTextPaint.getColor(textColor)
    this.textSize = textSize
    this.textAlign = Paint.Align.LEFT
    typeface = this@createTextPaint.resources.getFont(R.font.technology_bold)
}.let(block)