package by.bulba.watch.elektronika.data.watchface

import androidx.annotation.StringRes
import java.time.format.DateTimeFormatter

internal data class DigitalClockTimeFormat(
    val id: Identifier,
    @StringRes val displayNameResourceId: Int,
    val dateTimeFormatter: DateTimeFormatter,
    val ambientTimeFormatter: DateTimeFormatter,
) {
    @JvmInline
    value class Identifier(val id: String)
}