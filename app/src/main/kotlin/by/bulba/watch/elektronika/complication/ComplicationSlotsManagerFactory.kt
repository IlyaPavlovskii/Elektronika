package by.bulba.watch.elektronika.complication

import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.style.CurrentUserStyleRepository

interface ComplicationSlotsManagerFactory {
    fun create(
        currentUserStyleRepository: CurrentUserStyleRepository,
    ): ComplicationSlotsManager
}