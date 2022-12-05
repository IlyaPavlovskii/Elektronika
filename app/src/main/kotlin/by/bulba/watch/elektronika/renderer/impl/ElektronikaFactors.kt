package by.bulba.watch.elektronika.renderer.impl

import by.bulba.watch.elektronika.data.rf

internal object ElektronikaFactors {
    object Label {
        val START_Y_FACTOR = 0.1.rf
        val TEXT_SIZE_FACTOR = 0.05.rf
    }
    object CenterRect {
        val WIDTH_FACTOR = 0.66.rf
        val HEIGHT_FACTOR = 0.33.rf
        val START_Y_FACTOR = 0.33.rf
    }
    object Battery {
        val WIDTH_FACTOR = 0.66.rf
        val HEIGHT_FACTOR = 0.02.rf
        val START_Y_FACTOR = 0.7.rf
        val START_ICON_Y_FACTOR = 0.73.rf
        val ICON_WIDTH_FACTOR = 0.07.rf
        val BATTERY_LEVEL_ICON_WIDTH_FACTOR = 1.2.rf
        val BATTERY_LEVEL_ICON_HEIGHT_FACTOR = 0.85.rf
    }
    object DigitalClock {
        val START_DATE_Y_FACTOR = 0.43.rf
        val START_DATE_X_FACTOR = 0.3.rf
        val START_CLOCK_Y_FACTOR = 0.6.rf
        val DATE_TEXT_SIZE_FACTOR = 0.08.rf
    }
    object BottomLabel {
        val START_Y_FACTOR = 0.9.rf
        val TEXT_SIZE_FACTOR = 0.05.rf
    }
}
