package by.bulba.watch.elektronika.complication

import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.style.CurrentUserStyleRepository

internal class DefaultComplicationSlotsManagerFactory(
    private vararg val complicationSlotFactories: ComplicationSlotFactory,
) : ComplicationSlotsManagerFactory {
    override fun create(
        currentUserStyleRepository: CurrentUserStyleRepository
    ): ComplicationSlotsManager = ComplicationSlotsManager(
        complicationSlotFactories.map(ComplicationSlotFactory::create),
        currentUserStyleRepository,
    )
}