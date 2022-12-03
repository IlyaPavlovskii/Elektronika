package by.bulba.watch.elektronika.repository

import by.bulba.watch.elektronika.R
import by.bulba.watch.elektronika.data.watchface.DigitalClockTimeFormat
import java.time.format.DateTimeFormatter

internal interface DefaultDigitalClockTimeFormatProvider {
    fun collection(): List<DigitalClockTimeFormat>

    fun getOrNull(id: DigitalClockTimeFormat.Identifier): DigitalClockTimeFormat? =
        collection().firstOrNull { it.id == id }

    fun default(): DigitalClockTimeFormat = collection().first()

    companion object {
        fun create(): DefaultDigitalClockTimeFormatProvider = DefaultDigitalClockTimeFormatProviderImpl()
    }
}

private class DefaultDigitalClockTimeFormatProviderImpl :
    DefaultDigitalClockTimeFormatProvider {
    override fun collection(): List<DigitalClockTimeFormat> = digitalClockTimeFormats

    companion object {
        private val digitalClockTimeFormats = listOf(
            DigitalClockTimeFormat(
                id = DigitalClockTimeFormat.Identifier("hours_24"),
                displayNameResourceId = R.string.time_format__hours_24,
                dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss"),
                ambientTimeFormatter = DateTimeFormatter.ofPattern("HH:mm"),
            ),
            DigitalClockTimeFormat(
                id = DigitalClockTimeFormat.Identifier("hours_12"),
                displayNameResourceId = R.string.time_format__hours_12,
                dateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss"),
                ambientTimeFormatter = DateTimeFormatter.ofPattern("hh:mm"),
            )
        )
    }
}