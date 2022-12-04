package by.bulba.watch.elektronika.utils

import androidx.wear.watchface.style.UserStyle
import androidx.wear.watchface.style.UserStyleSetting
import kotlinx.coroutines.flow.MutableStateFlow

internal fun UserStyle.findKey(id: UserStyleSetting.Id): UserStyleSetting? =
    this.keys.firstOrNull { userStyleSetting -> userStyleSetting.id == id }

internal fun UserStyle.findSelectedOption(id: UserStyleSetting.Id): UserStyleSetting.Option? =
    this[findKey(id)]

internal fun MutableStateFlow<UserStyle>.setNewOptionId(
    id: UserStyleSetting.Id,
    newOptionPredicate: (UserStyleSetting.Option) -> Boolean,
) {
    val userStyle = this.value
    val key = requireNotNull(userStyle.findKey(id))
    val mutableUserStyle = userStyle.toMutableUserStyle()
    val newOption = key.options.first(newOptionPredicate)
    mutableUserStyle[key] = newOption
    this.value = mutableUserStyle.toUserStyle()
}