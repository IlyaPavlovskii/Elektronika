package by.bulba.watch.elektronika.editor.format

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.wear.watchface.editor.EditorSession
import androidx.wear.watchface.style.UserStyle
import androidx.wear.watchface.style.UserStyleSetting
import by.bulba.watch.elektronika.data.watchface.DigitalClockTimeFormat
import by.bulba.watch.elektronika.repository.DefaultDigitalClockTimeFormatProvider
import by.bulba.watch.elektronika.repository.platform.COLOR_STYLE_SETTING
import by.bulba.watch.elektronika.repository.platform.TIME_FORMAT_SETTING
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
    private val activity: ComponentActivity,
    scope: CoroutineScope,
    private val defaultDigitalClockTimeFormatProvider: DefaultDigitalClockTimeFormatProvider,
) {
    private lateinit var editorSession: EditorSession
    val state: StateFlow<WatchTimeFormatState> = flow<WatchTimeFormatState> {
        Log.d("WatchTime", "onCreate4")
        editorSession = EditorSession.createOnWatchEditorSession(activity = activity)
        Log.d("WatchTime", "onCreate5")
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
                Log.d("WatchTime", "State: $watchTimeFormatState ")
                val selectedId = userStyle[TIME_FORMAT_SETTING]
                    as? UserStyleSetting.ListUserStyleSetting.ListOption
                    ?: return@combine watchTimeFormatState
                Log.d("WatchTime", "SelectIf: $selectedId")
                val identifier = DigitalClockTimeFormat.Identifier(selectedId.id.toString())
                watchTimeFormatState.copy(
                    items = watchTimeFormatState.items.map { timeFormatItem ->
                        timeFormatItem.copy(
                            selected = timeFormatItem.domainMetaData == identifier,
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
        val mutableUserStyle = editorSession.userStyle.value
        val options = mutableUserStyle[TIME_FORMAT_SETTING]
    }

    private fun DigitalClockTimeFormat.toTimeFormatItem(
        id: DigitalClockTimeFormat.Identifier
    ): TimeFormatItem = TimeFormatItem(
        text = zonedDateTime.format(this.dateTimeFormatter),
        selected = this.id == id,
        domainMetaData = id,
    )

    companion object {
        private val zonedDateTime = ZonedDateTime.of(1985, 10, 26, 9, 0, 0, 0, ZoneOffset.UTC)
    }
}

internal data class WatchTimeFormatState(
    val items: List<TimeFormatItem> = emptyList()
)