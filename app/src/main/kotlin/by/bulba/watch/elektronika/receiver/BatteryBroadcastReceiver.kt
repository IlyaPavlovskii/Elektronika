package by.bulba.watch.elektronika.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.os.BatteryManager.BATTERY_STATUS_CHARGING
import android.os.BatteryManager.BATTERY_STATUS_UNKNOWN
import android.util.Log
import by.bulba.watch.elektronika.data.watchface.WatchFaceData
import by.bulba.watch.elektronika.repository.WatchFaceDataStateRepository

internal class BatteryBroadcastReceiver(
    private val repository: WatchFaceDataStateRepository,
) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, 0) ?: 0
        val status = intent?.getIntExtra(BatteryManager.EXTRA_STATUS, BATTERY_STATUS_UNKNOWN)
            ?: BATTERY_STATUS_UNKNOWN
        repository.update { watchFaceData ->
            val battery = watchFaceData.battery.copy(
                level = WatchFaceData.Battery.Level(level.toUInt()),
                state = when (status) {
                    BATTERY_STATUS_CHARGING -> WatchFaceData.Battery.State.CHARGING
                    else -> WatchFaceData.Battery.State.NOT_CHARGING
                }
            )
            Log.d("Elektronika", "Battery: $battery")
            watchFaceData.copy(battery = battery)
        }
    }
}