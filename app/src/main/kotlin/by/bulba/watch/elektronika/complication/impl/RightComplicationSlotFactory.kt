package by.bulba.watch.elektronika.complication.impl

import androidx.wear.watchface.CanvasComplicationFactory
import androidx.wear.watchface.ComplicationSlot
import androidx.wear.watchface.complications.ComplicationSlotBounds
import androidx.wear.watchface.complications.DefaultComplicationDataSourcePolicy
import androidx.wear.watchface.complications.SystemDataSources
import androidx.wear.watchface.complications.data.ComplicationType
import by.bulba.watch.elektronika.complication.Complication
import by.bulba.watch.elektronika.complication.ComplicationRelativeFactor
import by.bulba.watch.elektronika.complication.ComplicationSlotFactory

internal class RightComplicationSlotFactory(
    private val canvasComplicationFactory: CanvasComplicationFactory,
    private val complicationRelativeFactor: ComplicationRelativeFactor = ComplicationFactors.right,
    private val id: Int = Complication.Right.id.value,
    private val supportedTypes: List<ComplicationType> = SUPPORTED_TYPES,
) : ComplicationSlotFactory {
    override fun create(): ComplicationSlot {
        return ComplicationSlot.createRoundRectComplicationSlotBuilder(
            id = id,
            canvasComplicationFactory = canvasComplicationFactory,
            supportedTypes = supportedTypes,
            defaultDataSourcePolicy = DefaultComplicationDataSourcePolicy(
                SystemDataSources.DATA_SOURCE_STEP_COUNT,
                ComplicationType.SHORT_TEXT
            ),
            bounds = ComplicationSlotBounds(complicationRelativeFactor.rectF)
        ).build()
    }

    companion object {
        private val SUPPORTED_TYPES = listOf(
            ComplicationType.RANGED_VALUE,
            ComplicationType.MONOCHROMATIC_IMAGE,
            ComplicationType.SHORT_TEXT,
            ComplicationType.SMALL_IMAGE
        )
    }
}