package by.bulba.watch.elektronika.editor.face

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import by.bulba.watch.elektronika.R
import by.bulba.watch.elektronika.databinding.ActivityWatchFaceConfigBinding

internal class WatchFaceConfigActivity : ComponentActivity(R.layout.activity_watch_face_config) {
    private val stateHolder: WatchFaceConfigStateHolder by lazy {
        WatchFaceConfigStateHolder(lifecycleScope, this)
    }

    private val binding: ActivityWatchFaceConfigBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityWatchFaceConfigBinding.inflate(this.layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
//        stateHolder.uiState
//                .onEach { uiState: WatchFaceConfigStateHolder.EditWatchFaceUiState ->
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
//                }.launchIn(lifecycleScope)
    }
}