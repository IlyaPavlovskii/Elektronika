package by.bulba.watch.elektronika.complication

import android.graphics.RectF
import by.bulba.watch.elektronika.data.RelativeFactor

data class ComplicationRelativeFactor(
    val left: RelativeFactor,
    val top: RelativeFactor,
    val right: RelativeFactor,
    val bottom: RelativeFactor,
) {
    val rectF = RectF(
        left.factor,
        top.factor,
        right.factor,
        bottom.factor,
    )
}