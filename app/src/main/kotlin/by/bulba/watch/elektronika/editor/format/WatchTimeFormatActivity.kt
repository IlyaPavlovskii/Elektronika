package by.bulba.watch.elektronika.editor.format

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.wear.widget.WearableLinearLayoutManager
import by.bulba.watch.elektronika.R
import by.bulba.watch.elektronika.databinding.ActivityWatchTimeFormatBinding
import by.bulba.watch.elektronika.repository.DefaultDigitalClockTimeFormatProvider
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class WatchTimeFormatActivity : ComponentActivity(R.layout.activity_watch_time_format) {

    private val binding: ActivityWatchTimeFormatBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityWatchTimeFormatBinding.inflate(this.layoutInflater)
    }
    private val holder: WatchTimeFormatStateHolder by lazy(LazyThreadSafetyMode.NONE) {
        WatchTimeFormatStateHolder(
            activity = this,
            scope = lifecycleScope,
            defaultDigitalClockTimeFormatProvider = DefaultDigitalClockTimeFormatProvider.create(),
        )
    }
    private val timeFormatAdapter: DigitalClockTimeFormatAdapter by lazy(LazyThreadSafetyMode.NONE) {
        DigitalClockTimeFormatAdapter(
            callback = { timeFormatItem ->
                holder.setDigitalClockTimeFormat(timeFormatItem.domainMetaData)
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("WatchTime", "onCreate1")
        setContentView(binding.root)
        with(binding.wearRecyclerView) {
            this.setHasFixedSize(true)
            this.isEdgeItemsCenteringEnabled = true
            this.layoutManager = WearableLinearLayoutManager(this.context)
            this.adapter = timeFormatAdapter
        }
        Log.d("WatchTime", "onCreate2")
        holder.state.onEach { formatState ->
            Log.d("WatchTime", "onCreate3: $formatState")
            timeFormatAdapter.setItems(formatState.items)
        }.launchIn(lifecycleScope)
    }
}