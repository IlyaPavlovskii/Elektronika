package by.bulba.watch.elektronika.repository

import androidx.wear.watchface.style.CurrentUserStyleRepository
import androidx.wear.watchface.style.UserStyleSetting
import by.bulba.watch.elektronika.data.watchface.DigitalClockTimeFormat
import by.bulba.watch.elektronika.repository.platform.TIME_FORMAT_SETTING
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

internal interface DigitalClockTimeFormatStateProvider {
    val state: Flow<DigitalClockTimeFormat>
}

internal class DigitalClockTimeFormatProviderImpl(
    userStyleRepository: CurrentUserStyleRepository,
    private val defaultDigitalClockTimeFormatProvider: DefaultDigitalClockTimeFormatProvider
) : DigitalClockTimeFormatStateProvider {

    override val state: Flow<DigitalClockTimeFormat> = userStyleRepository.userStyle
        .mapNotNull { userStyle ->
            val option = userStyle[TIME_FORMAT_SETTING]
                as? UserStyleSetting.ListUserStyleSetting.ListOption
                ?: return@mapNotNull null
            val id = DigitalClockTimeFormat.Identifier(option.id.toString())
            defaultDigitalClockTimeFormatProvider.getOrNull(id)
        }
}