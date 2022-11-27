package by.bulba.watch.elektronika.factory

import by.bulba.watch.elektronika.data.watchface.WatchFaceData

internal class DefaultWatchFaceDataFactory : WatchFaceDataFactory {
    override fun create(): WatchFaceData = WatchFaceData()
}