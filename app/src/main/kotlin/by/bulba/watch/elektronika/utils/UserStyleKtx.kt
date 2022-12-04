package by.bulba.watch.elektronika.utils

import androidx.wear.watchface.style.UserStyle
import androidx.wear.watchface.style.UserStyleSetting

internal fun UserStyle.findKey(id: UserStyleSetting.Id): UserStyleSetting? =
    this.keys.firstOrNull { userStyleSetting -> userStyleSetting.id == id }

internal fun UserStyle.findSelectedOption(id: UserStyleSetting.Id): UserStyleSetting.Option? =
    this[findKey(id)]