package by.bulba.watch.elektronika.renderer

import android.content.Context
import android.graphics.Paint
import android.graphics.Rect
import by.bulba.watch.elektronika.R
import by.bulba.watch.elektronika.data.RelativeFactor
import by.bulba.watch.elektronika.utils.wrapper.ColorWrapper

internal fun Rect.calcFactorByHeight(factor: RelativeFactor): Float = this.height().calcFactor(factor)

internal fun Int.calcFactor(factor: RelativeFactor): Float = this * factor.factor

fun Context.createTextPaint(
    bounds: Rect,
    textFactor: RelativeFactor,
    textColor: ColorWrapper,
    block: (Paint) -> Paint = { it },
): Paint = createTextPaint(
    textColor = textColor,
    textSize = bounds.calcFactorByHeight(textFactor),
    block = block,
)

fun Context.createTextPaint(
    textColor: ColorWrapper,
    textSize: Float,
    block: (Paint) -> Paint = { it },
): Paint = Paint().apply {
    isAntiAlias = true
    color = textColor.get(this@createTextPaint)
    this.textSize = textSize
    typeface = this@createTextPaint.resources.getFont(R.font.technology_bold)
}.let(block)