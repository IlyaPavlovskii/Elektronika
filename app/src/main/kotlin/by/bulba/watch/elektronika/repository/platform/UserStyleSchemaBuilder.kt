package by.bulba.watch.elektronika.repository.platform

import android.content.Context
import android.graphics.drawable.Icon
import androidx.wear.watchface.style.UserStyleSchema
import androidx.wear.watchface.style.UserStyleSetting
import androidx.wear.watchface.style.WatchFaceLayer
import by.bulba.watch.elektronika.R
import by.bulba.watch.elektronika.data.converter.toListOption
import by.bulba.watch.elektronika.data.converter.toOptionList
import by.bulba.watch.elektronika.provider.DefaultPaletteStyleProvider
import by.bulba.watch.elektronika.repository.DefaultDigitalClockTimeFormatProvider

internal val COLOR_STYLE_SETTING = UserStyleSetting.Id("color_style_setting")
internal val TIME_FORMAT_SETTING = UserStyleSetting.Id("time_format_setting")

internal class UserStyleSchemaBuilder(
    private val defaultPaletteStyleProvider: DefaultPaletteStyleProvider = DefaultPaletteStyleProvider.create(),
    private val defaultDigitalClockTimeFormatProvider: DefaultDigitalClockTimeFormatProvider = DefaultDigitalClockTimeFormatProvider.create()
) {

    fun createUserStyleSchema(context: Context): UserStyleSchema {
        val colorStyleSetting =
            UserStyleSetting.ListUserStyleSetting(
                id = COLOR_STYLE_SETTING,
                resources = context.resources,
                displayNameResourceId = R.string.settings_display_name__watch_face_setting,
                descriptionResourceId = R.string.settings_description__watch_face_setting,
                icon = Icon.createWithResource(context, R.drawable.baseline_watch_24),
                options = defaultPaletteStyleProvider.collection().map { paletteStyle ->
                    paletteStyle.toOptionList(context)
                },
                affectsWatchFaceLayers = listOf(
                    WatchFaceLayer.BASE,
                    WatchFaceLayer.COMPLICATIONS,
                    WatchFaceLayer.COMPLICATIONS_OVERLAY
                )
            )

        val timeFormatSettings = UserStyleSetting.ListUserStyleSetting(
            id = TIME_FORMAT_SETTING,
            resources = context.resources,
            displayNameResourceId = R.string.settings_display_name__time_format_setting,
            descriptionResourceId = R.string.settings_description__time_format,
            icon = Icon.createWithResource(context, R.drawable.baseline_watch_later_24),
            options = defaultDigitalClockTimeFormatProvider.collection().map { timeFormatType ->
                timeFormatType.toListOption(context)
            },
            affectsWatchFaceLayers = listOf(
                WatchFaceLayer.BASE,
            )
        )

        return UserStyleSchema(
            listOf(
                colorStyleSetting,
                timeFormatSettings,
            )
        )
    }

    companion object {
        val INSTANCE = UserStyleSchemaBuilder()
    }
}
