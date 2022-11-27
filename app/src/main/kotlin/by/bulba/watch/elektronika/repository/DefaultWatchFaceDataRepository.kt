package by.bulba.watch.elektronika.repository

import by.bulba.watch.elektronika.data.watchface.WatchFaceData
import by.bulba.watch.elektronika.factory.WatchFaceDataFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

internal class DefaultWatchFaceDataRepository(
    private val factory: WatchFaceDataFactory,
) : WatchFaceDataRepository {
    private val _state = MutableStateFlow(factory.create())

    override val state: StateFlow<WatchFaceData> = _state

    override fun update(data: WatchFaceData) {
        _state.value = data
    }

    override fun update(updateBlock: (WatchFaceData) -> WatchFaceData) {
        _state.value = updateBlock(_state.value)
    }
}