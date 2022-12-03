package by.bulba.watch.elektronika.factory

import by.bulba.watch.elektronika.data.watchface.PaletteStyle
import by.bulba.watch.elektronika.data.watchface.WatchFaceData
import by.bulba.watch.elektronika.data.watchface.toWatchFaceColorPalette
import by.bulba.watch.elektronika.provider.DefaultPaletteStyleProvider
import by.bulba.watch.elektronika.repository.DefaultDigitalClockTimeFormatProvider

internal class DefaultWatchFaceDataFactory(
    private val defaultPaletteStyleProvider: DefaultPaletteStyleProvider,
    private val defaultDigitalClockTimeFormatProvider: DefaultDigitalClockTimeFormatProvider,
) : WatchFaceDataFactory {
    override fun create(): WatchFaceData = WatchFaceData(
        activePalette = defaultPaletteStyleProvider.default().toWatchFaceColorPalette(),
        ambientPalette = PaletteStyle.AMBIENT.toWatchFaceColorPalette(),
        mode = WatchFaceData.Mode.ACTIVE,
        digitalClock = WatchFaceData.DigitalClock(
            digitalClockTimeFormat = defaultDigitalClockTimeFormatProvider.default()
        )
    )
}