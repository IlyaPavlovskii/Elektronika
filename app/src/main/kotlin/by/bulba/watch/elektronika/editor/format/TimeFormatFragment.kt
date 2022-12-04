package by.bulba.watch.elektronika.editor.format

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.wear.widget.WearableLinearLayoutManager
import by.bulba.watch.elektronika.R
import by.bulba.watch.elektronika.databinding.FragmentTimeFormatBinding
import by.bulba.watch.elektronika.repository.DefaultDigitalClockTimeFormatProvider
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class TimeFormatFragment : Fragment(R.layout.fragment_time_format) {

    private val holder: WatchTimeFormatStateHolder by lazy(LazyThreadSafetyMode.NONE) {
        WatchTimeFormatStateHolder(
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentTimeFormatBinding.bind(view)
        with(binding.wearRecyclerView) {
            this.setHasFixedSize(true)
            this.isEdgeItemsCenteringEnabled = true
            this.layoutManager = WearableLinearLayoutManager(this.context)
            this.adapter = timeFormatAdapter
        }
        holder.state.onEach { formatState ->
            Log.d("WatchTime", "onCreate3: $formatState")
            timeFormatAdapter.setItems(formatState.items)
        }.launchIn(lifecycleScope)
    }
}