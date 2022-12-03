package by.bulba.watch.elektronika.provider

import androidx.wear.watchface.style.CurrentUserStyleRepository
import androidx.wear.watchface.style.UserStyleSetting
import by.bulba.watch.elektronika.data.watchface.PaletteStyle
import by.bulba.watch.elektronika.repository.platform.COLOR_STYLE_SETTING
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

internal interface PaletteStyleStateProvider {
    val state: Flow<PaletteStyle>
}

internal class PaletteStyleStateProviderImpl(
    userStyleRepository: CurrentUserStyleRepository,
    private val defaultPaletteStyleProvider: DefaultPaletteStyleProvider =
        DefaultPaletteStyleProvider.create(),
) : PaletteStyleStateProvider {
    override val state: Flow<PaletteStyle> = userStyleRepository.userStyle
        .mapNotNull {userStyle ->
            val option = userStyle[COLOR_STYLE_SETTING]
                as? UserStyleSetting.ListUserStyleSetting.ListOption
                ?: return@mapNotNull null
            val id = PaletteStyle.Identifier(option.id.toString())
            defaultPaletteStyleProvider.getOrNull(id)
        }
}