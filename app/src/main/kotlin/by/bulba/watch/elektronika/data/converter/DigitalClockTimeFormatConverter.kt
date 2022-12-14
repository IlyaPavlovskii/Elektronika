package by.bulba.watch.elektronika.data.converter

import android.content.Context
import android.graphics.drawable.Icon
import androidx.wear.watchface.style.UserStyleSetting
import by.bulba.watch.elektronika.data.watchface.DigitalClockTimeFormat

internal fun DigitalClockTimeFormat.toListOption(
    context: Context
) = UserStyleSetting.ListUserStyleSetting.ListOption(
    id = this.id.toOptionId(),
    resources = context.resources,
    displayNameResourceId = this.displayNameResourceId,
    icon = Icon.createWithResource(context, this.iconResId)
)

internal fun DigitalClockTimeFormat.Identifier.toOptionId() = UserStyleSetting.Option.Id(this.id)
