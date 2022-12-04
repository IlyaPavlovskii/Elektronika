package by.bulba.watch.elektronika.editor.face

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import by.bulba.watch.elektronika.R
import by.bulba.watch.elektronika.complication.Complication
import by.bulba.watch.elektronika.databinding.FragmentPaletteConfigBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


internal class PaletteConfigFragment : Fragment(R.layout.fragment_palette_config) {
    private lateinit var binding: FragmentPaletteConfigBinding
    private val stateHolder: WatchFaceConfigStateHolder by lazy {
        WatchFaceConfigStateHolder(
            scope = lifecycleScope,
            isScreenRound = requireActivity().resources.configuration.isScreenRound,
        )
    }
    private val snapHelper: PagerSnapHelper by lazy(LazyThreadSafetyMode.NONE) {
        PagerSnapHelper()
    }
    private val paletteAdapter: PaletteConfigAdapter by lazy(LazyThreadSafetyMode.NONE) {
        PaletteConfigAdapter(
            leftComplicationClickListener = {
                stateHolder.chooseComplication(Complication.Left.id.value)
            },
            rightComplicationClickListener = {
                stateHolder.chooseComplication(Complication.Right.id.value)
            }
        )
    }
    private val adapterDataObserver = object : AdapterDataObserver() {
        override fun onChanged() {
            super.onChanged()
            (stateHolder.uiState.value as? WatchFaceConfigStateHolder.EditWatchFaceUiState.Success)
                ?.also { state ->
                    binding.recyclerView.goTo(state.getSelectedPosition())
                }
        }
    }
    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                val layoutManager = binding.recyclerView.layoutManager ?: return
                val centerView = snapHelper.findSnapView(layoutManager) ?: return
                val position: Int = layoutManager.getPosition(centerView)
                val identifier = paletteAdapter.getItem(position).domainMeta
                stateHolder.setPalette(identifier)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPaletteConfigBinding.bind(view)
        with(binding) {
            paletteAdapter.registerAdapterDataObserver(adapterDataObserver)
            this.recyclerView.setHasFixedSize(true)
            this.recyclerView.addOnScrollListener(onScrollListener)
            this.recyclerView.adapter = paletteAdapter

            snapHelper.attachToRecyclerView(this.recyclerView)
            this.indicator.attachToRecyclerView(this.recyclerView)
        }
        stateHolder.uiState
            .onEach { uiState: WatchFaceConfigStateHolder.EditWatchFaceUiState ->
                Log.d(TAG, "StateFlow Loading: $uiState")
                when (uiState) {
                    is WatchFaceConfigStateHolder.EditWatchFaceUiState.Loading -> {
                        binding.progress.visibility = View.VISIBLE
                        binding.recyclerView.visibility = View.INVISIBLE
                        binding.indicator.visibility = View.INVISIBLE
                    }

                    is WatchFaceConfigStateHolder.EditWatchFaceUiState.Success -> {
                        binding.progress.visibility = View.INVISIBLE
                        binding.recyclerView.visibility = View.VISIBLE
                        binding.indicator.visibility = View.VISIBLE

                        val items = uiState.previews.map { it.toPaletteItem() }
                        paletteAdapter.setItems(items)
                    }
                }
            }.launchIn(lifecycleScope)
    }

    override fun onDestroyView() {
        paletteAdapter.unregisterAdapterDataObserver(adapterDataObserver)
        binding.recyclerView.removeOnScrollListener(onScrollListener)
        super.onDestroyView()
    }

    private fun WatchFaceConfigStateHolder.UserStylesAndPreview.toPaletteItem(): PaletteItem {
        return PaletteItem(
            image = this.previewImage,
            domainMeta = this.paletteStyleId,
        )
    }

    private fun RecyclerView.goTo(selectedPosition: Int) = this.post {
        val layoutManager = this.layoutManager ?: run {
            this.scrollToPosition(selectedPosition)
            return@post
        }
        val view: View = layoutManager.findViewByPosition(selectedPosition)
            ?: run {
                this.scrollToPosition(selectedPosition)
                return@post
            }

        val snapDistance: IntArray = snapHelper.calculateDistanceToFinalSnap(layoutManager, view)
            ?: run {
                this.scrollToPosition(selectedPosition)
                return@post
            }
        if (snapDistance[0] != 0 || snapDistance[1] != 0) {
            this.scrollBy(snapDistance[0], snapDistance[1])
        }
    }

    companion object {
        private const val TAG = "WatchFaceConfigActivity"
    }
}