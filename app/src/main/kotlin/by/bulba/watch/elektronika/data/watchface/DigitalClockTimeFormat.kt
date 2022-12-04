package by.bulba.watch.elektronika.data.watchface

import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import by.bulba.watch.elektronika.data.RelativeFactor
import java.time.format.DateTimeFormatter

internal data class DigitalClockTimeFormat(
    val id: Identifier,
    @StringRes val displayNameResourceId: Int,
    val snapshotTime: String,
    val ambientSnapshotTime: String,
    val dateTimeFormatter: DateTimeFormatter,
    val ambientTimeFormatter: DateTimeFormatter,
    val clockTextSizeFactor: RelativeFactor,
) {
    @JvmInline
    value class Identifier(val id: String)
}