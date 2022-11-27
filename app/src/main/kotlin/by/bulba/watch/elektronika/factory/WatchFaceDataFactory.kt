package by.bulba.watch.elektronika.factory

import by.bulba.watch.elektronika.data.watchface.WatchFaceData

internal fun interface WatchFaceDataFactory {
    fun create(): WatchFaceData
}
