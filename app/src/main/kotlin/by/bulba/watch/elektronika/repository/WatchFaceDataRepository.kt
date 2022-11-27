package by.bulba.watch.elektronika.repository

import by.bulba.watch.elektronika.data.watchface.WatchFaceData
import kotlinx.coroutines.flow.StateFlow

internal interface WatchFaceDataRepository {
    val state: StateFlow<WatchFaceData>

    fun update(data: WatchFaceData)

    fun update(updateBlock: (WatchFaceData) -> WatchFaceData)
}