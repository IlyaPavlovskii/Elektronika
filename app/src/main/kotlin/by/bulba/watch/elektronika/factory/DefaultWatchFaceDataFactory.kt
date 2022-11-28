package by.bulba.watch.elektronika.factory

import by.bulba.watch.elektronika.data.watchface.PaletteStyle
import by.bulba.watch.elektronika.data.watchface.WatchFaceData
import by.bulba.watch.elektronika.data.watchface.toWatchFaceColorPalette

internal class DefaultWatchFaceDataFactory : WatchFaceDataFactory {
    // TODO: Pass selected palette from userData
    override fun create(): WatchFaceData = WatchFaceData(
        activePalette = PaletteStyle.PRIMARY.toWatchFaceColorPalette(),
        ambientPalette = PaletteStyle.AMBIENT.toWatchFaceColorPalette(),
        mode = WatchFaceData.Mode.ACTIVE,
    )
}