package by.bulba.watch.elektronika.editor.format

import android.util.Log
import androidx.wear.watchface.editor.EditorSession
import by.bulba.watch.elektronika.data.converter.toOptionId
import by.bulba.watch.elektronika.data.watchface.DigitalClockTimeFormat
import by.bulba.watch.elektronika.editor.root.WatchSettingsRootHolder
import by.bulba.watch.elektronika.repository.DefaultDigitalClockTimeFormatProvider
import by.bulba.watch.elektronika.repository.platform.TIME_FORMAT_SETTING
import by.bulba.watch.elektronika.utils.findKey
import by.bulba.watch.elektronika.utils.findSelectedOption
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.plus
import java.time.ZoneOffset
import java.time.ZonedDateTime

internal class WatchTimeFormatStateHolder(
    scope: CoroutineScope,
    private val editorSession: EditorSession = WatchSettingsRootHolder.getEditorSession(),
    private val defaultDigitalClockTimeFormatProvider: DefaultDigitalClockTimeFormatProvider,
) {

    val state: StateFlow<WatchTimeFormatState> = flow {
        emitAll(
            combine(
                flowOf(
                    WatchTimeFormatState(
                        items = defaultDigitalClockTimeFormatProvider.collection()
                            .map {
                                it.toTimeFormatItem(defaultDigitalClockTimeFormatProvider.default().id)
                            }
                    )
                ),
                editorSession.userStyle
            ) { watchTimeFormatState, userStyle ->
                val selectedId = userStyle.findSelectedOption(TIME_FORMAT_SETTING)
                    ?.id?.toString()?.let(DigitalClockTimeFormat::Identifier)
                    ?: defaultDigitalClockTimeFormatProvider.default().id

                Log.d("SelectedId", "State: $watchTimeFormatState ")

                watchTimeFormatState.copy(
                    items = watchTimeFormatState.items.map { timeFormatItem ->
                        timeFormatItem.copy(
                            selected = timeFormatItem.domainMetaData == selectedId,
                        )
                    }
                )
            }
        )
    }.stateIn(
        scope = scope + Dispatchers.Main.immediate,
        started = SharingStarted.Eagerly,
        initialValue = WatchTimeFormatState()
    )

    fun setDigitalClockTimeFormat(id: DigitalClockTimeFormat.Identifier) {
        val userStyle = editorSession.userStyle.value
        val key = requireNotNull(userStyle.findKey(TIME_FORMAT_SETTING))
        val mutableUserStyle = editorSession.userStyle.value.toMutableUserStyle()
        val newOption = key.options.first { option ->
            option.id == id.toOptionId()
        }
        mutableUserStyle[key] = newOption
        editorSession.userStyle.value = mutableUserStyle.toUserStyle()
    }

    private fun DigitalClockTimeFormat.toTimeFormatItem(
        selectedId: DigitalClockTimeFormat.Identifier
    ): TimeFormatItem = TimeFormatItem(
        text = zonedDateTime.format(this.dateTimeFormatter),
        selected = this.id == id,
        domainMetaData = id,
    )

    companion object {
        private val zonedDateTime = ZonedDateTime.of(1997, 8, 29, 2, 14, 39, 0, ZoneOffset.UTC)
    }
}

internal data class WatchTimeFormatState(
    val items: List<TimeFormatItem> = emptyList()
)