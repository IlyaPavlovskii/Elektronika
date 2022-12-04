package by.bulba.watch.elektronika.editor.root

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.wear.widget.WearableLinearLayoutManager
import by.bulba.watch.elektronika.R
import by.bulba.watch.elektronika.databinding.ActivityWatchSettingsRootBinding
import by.bulba.watch.elektronika.editor.face.WatchFaceConfigActivity
import by.bulba.watch.elektronika.editor.format.WatchTimeFormatActivity
import by.bulba.watch.elektronika.editor.format.WatchTimeFormatStateHolder
import by.bulba.watch.elektronika.repository.DefaultDigitalClockTimeFormatProvider
import by.bulba.watch.elektronika.utils.activityIntent
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class WatchSettingsRootActivity : ComponentActivity(R.layout.activity_watch_settings_root) {

    private val binding: ActivityWatchSettingsRootBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityWatchSettingsRootBinding.inflate(layoutInflater)
    }
    private val adapterCallback: MainMenuAdapter.AdapterCallback =
        MainMenuAdapter.AdapterCallback { menuItem ->
            val intent = when (menuItem) {
                MenuItem.WATCH_FACE -> activityIntent<WatchFaceConfigActivity>()
                MenuItem.TIME_FORMAT -> activityIntent<WatchTimeFormatActivity>()
            }
            startActivity(intent)
        }
    private val rootAdapter: MainMenuAdapter by lazy(LazyThreadSafetyMode.NONE) {
        MainMenuAdapter(callback = adapterCallback)
    }

    private val holder: WatchTimeFormatStateHolder by lazy(LazyThreadSafetyMode.NONE) {
        WatchTimeFormatStateHolder(
            activity = this,
            scope = lifecycleScope,
            defaultDigitalClockTimeFormatProvider = DefaultDigitalClockTimeFormatProvider.create(),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        with(binding.wearRecyclerView) {
            this.setHasFixedSize(true)
            this.isEdgeItemsCenteringEnabled = true
            this.layoutManager = WearableLinearLayoutManager(this@WatchSettingsRootActivity)
            this.adapter = rootAdapter
        }

        holder.state.onEach { formatState ->
            Log.d("WatchTime", "onCreate. FormatState: $formatState")
        }.launchIn(lifecycleScope)
    }
}
