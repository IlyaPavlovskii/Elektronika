package by.bulba.watch.elektronika.complication

import androidx.wear.watchface.ComplicationSlot

interface ComplicationSlotFactory {
    fun create(): ComplicationSlot
}