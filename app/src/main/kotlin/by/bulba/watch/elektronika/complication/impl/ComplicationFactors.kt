package by.bulba.watch.elektronika.complication.impl

import by.bulba.watch.elektronika.complication.ComplicationRelativeFactor
import by.bulba.watch.elektronika.data.rf

internal object ComplicationFactors {

    private val complicationsTopBound = 0.13.rf
    private val complicationsBottomBound = 0.28.rf

    val left = ComplicationRelativeFactor(
        left = 0.25.rf,
        top = complicationsTopBound,
        right = 0.4.rf,
        bottom = complicationsBottomBound,
    )
    val right = ComplicationRelativeFactor(
        left = 0.6.rf,
        top = complicationsTopBound,
        right = 0.75.rf,
        bottom = complicationsBottomBound,
    )
}
