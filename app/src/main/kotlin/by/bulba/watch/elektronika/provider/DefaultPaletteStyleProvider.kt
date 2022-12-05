package by.bulba.watch.elektronika.provider

import by.bulba.watch.elektronika.data.watchface.PaletteStyle

internal interface DefaultPaletteStyleProvider {
    fun collection(): List<PaletteStyle>

    fun getOrNull(id: PaletteStyle.Identifier): PaletteStyle? =
        collection().firstOrNull { it.id == id }

    fun default(): PaletteStyle = collection().first()

    companion object {
        fun create(): DefaultPaletteStyleProvider = DefaultPaletteStyleProviderImpl()
    }
}

private class DefaultPaletteStyleProviderImpl : DefaultPaletteStyleProvider {
    override fun collection(): List<PaletteStyle> = enumValues<PaletteStyle>().toList()

    override fun getOrNull(id: PaletteStyle.Identifier): PaletteStyle? =
        collection().firstOrNull { paletteStyle -> paletteStyle.id == id }

    override fun default(): PaletteStyle = collection().first()
}