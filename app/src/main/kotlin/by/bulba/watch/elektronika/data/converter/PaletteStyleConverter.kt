package by.bulba.watch.elektronika.data.converter

import android.content.Context
import androidx.wear.watchface.style.UserStyleSetting
import by.bulba.watch.elektronika.data.watchface.PaletteStyle

internal fun PaletteStyle.toOptionList(
    context: Context
): UserStyleSetting.ListUserStyleSetting.ListOption =
    UserStyleSetting.ListUserStyleSetting.ListOption(
        id = this.id.toOptionId(),
        resources = context.resources,
        displayNameResourceId = this.nameResourceId,
        icon = null,
    )

private fun PaletteStyle.Identifier.toOptionId(): UserStyleSetting.Option.Id =
    UserStyleSetting.Option.Id(this.value)
