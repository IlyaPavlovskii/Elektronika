package by.bulba.watch.elektronika.editor.face

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.bulba.watch.elektronika.R
import by.bulba.watch.elektronika.databinding.FragmentPaletteConfigBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class PaletteConfigFragment : Fragment(R.layout.fragment_palette_config) {
    private val stateHolder: WatchFaceConfigStateHolder by lazy {
        WatchFaceConfigStateHolder(lifecycleScope, requireActivity())
    }

    private val binding: FragmentPaletteConfigBinding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentPaletteConfigBinding.inflate(this.layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stateHolder.uiState
            .onEach { uiState: WatchFaceConfigStateHolder.EditWatchFaceUiState ->
                Log.d(TAG, "StateFlow Loading: $uiState")
//                    when (uiState) {
//                        is WatchFaceConfigStateHolder.EditWatchFaceUiState.Loading -> {
//                            Log.d(TAG, "StateFlow Loading")
//                        }
//                        is WatchFaceConfigStateHolder.EditWatchFaceUiState.Success -> {
//                            binding.leftFace.setImageBitmap(uiState.previews[0].previewImage)
//                            binding.rightFace.setImageBitmap(uiState.previews[1].previewImage)
//                            updateWatchFacePreview(uiState.previews.first {preview ->
//                                preview.paletteStyleId == uiState.selectedPaletteStyleId
//                            })
//                        }
//                    }
            }.launchIn(lifecycleScope)
    }

    companion object {
        private const val TAG = "WatchFaceConfigActivity"
    }
}