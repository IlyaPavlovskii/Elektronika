package by.bulba.watch.elektronika.editor.root

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.wear.widget.WearableLinearLayoutManager
import by.bulba.watch.elektronika.R
import by.bulba.watch.elektronika.databinding.FragmentSettingsRootBinding

internal class RootSettingsFragment : Fragment(R.layout.fragment_settings_root) {

    private val adapterCallback: MainMenuAdapter.AdapterCallback =
        MainMenuAdapter.AdapterCallback { menuItem ->
            findNavController().navigate(
                when (menuItem) {
                    MenuItem.WATCH_FACE -> R.id.to_palette_config
                    MenuItem.TIME_FORMAT -> R.id.to_time_format
                }
            )
        }
    private val rootAdapter: MainMenuAdapter by lazy(LazyThreadSafetyMode.NONE) {
        MainMenuAdapter(callback = adapterCallback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSettingsRootBinding.bind(view)
        with(binding.wearRecyclerView) {
            this.setHasFixedSize(true)
            this.isEdgeItemsCenteringEnabled = true
            this.layoutManager = WearableLinearLayoutManager(view.context)
            this.adapter = rootAdapter
        }
    }
}