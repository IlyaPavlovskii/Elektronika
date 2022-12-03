package by.bulba.watch.elektronika.repository

import by.bulba.watch.elektronika.data.watchface.WatchFaceData
import by.bulba.watch.elektronika.data.watchface.toWatchFaceColorPalette
import by.bulba.watch.elektronika.factory.DefaultWatchFaceDataFactory
import by.bulba.watch.elektronika.provider.PaletteStyleStateProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

internal interface WatchFaceDataStateRepository {
    val state: StateFlow<WatchFaceData>

    fun update(updateBlock: (WatchFaceData) -> WatchFaceData)
}

internal class WatchFaceDataStateRepositoryImpl(
    scope: CoroutineScope,
    defaultWatchFaceDataFactory: DefaultWatchFaceDataFactory,
    private val paletteStyleStateProvider: PaletteStyleStateProvider,
    private val digitalClockTimeFormatStateProvider: DigitalClockTimeFormatStateProvider,
) : WatchFaceDataStateRepository {
    private val watchFaceState = MutableStateFlow(defaultWatchFaceDataFactory.create())
    override val state: StateFlow<WatchFaceData> = flow {
        emitAll(
            combine(
                watchFaceState,
                paletteStyleStateProvider.state,
                digitalClockTimeFormatStateProvider.state
            ) { watchFaceData, paletteStyle, digitalClockTimeFormat ->
                watchFaceData.copy(
                    activePalette = paletteStyle.toWatchFaceColorPalette(),
                    digitalClock = watchFaceData.digitalClock.copy(
                        digitalClockTimeFormat = digitalClockTimeFormat,
                    )
                )
            }
        )
    }.stateIn(
        scope = scope,
        started = SharingStarted.Eagerly,
        initialValue = watchFaceState.value
    )

    override fun update(updateBlock: (WatchFaceData) -> WatchFaceData) {
        watchFaceState.value = updateBlock(watchFaceState.value)
    }
}