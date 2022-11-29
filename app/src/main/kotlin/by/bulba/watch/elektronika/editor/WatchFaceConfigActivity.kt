/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package by.bulba.watch.elektronika.editor

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.wear.widget.WearableLinearLayoutManager
import by.bulba.watch.elektronika.complication.Complication
import by.bulba.watch.elektronika.data.watchface.PaletteStyle
import by.bulba.watch.elektronika.databinding.ActivityWatchFaceConfigBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * Allows user to edit certain parts of the watch face (color style, ticks displayed, minute arm
 * length) by using the [WatchFaceConfigStateHolder]. (All widgets are disabled until data is
 * loaded.)
 */
internal class WatchFaceConfigActivity : ComponentActivity() {

    private val stateHolder: WatchFaceConfigStateHolder by lazy {
        WatchFaceConfigStateHolder(
            lifecycleScope,
            this@WatchFaceConfigActivity
        )
    }

    private val binding: ActivityWatchFaceConfigBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityWatchFaceConfigBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()

        stateHolder.uiState
            .onEach { uiState: WatchFaceConfigStateHolder.EditWatchFaceUiState ->
                when (uiState) {
                    is WatchFaceConfigStateHolder.EditWatchFaceUiState.Loading -> {
                        Log.d(TAG, "StateFlow Loading")
                    }
                    is WatchFaceConfigStateHolder.EditWatchFaceUiState.Success -> {
                        binding.leftFace.setImageBitmap(uiState.previews[0].previewImage)
                        binding.rightFace.setImageBitmap(uiState.previews[1].previewImage)
                        updateWatchFacePreview(uiState.previews.first {preview ->
                            preview.paletteStyleId == uiState.selectedPaletteStyleId
                        })
                    }
                }
            }.launchIn(lifecycleScope)
    }

    private fun initView() {
        with(binding) {
            preview.leftComplication.setOnClickListener {
                Log.d(TAG, "onClickLeftComplicationButton()")
                stateHolder.setComplication(Complication.Left.id.value)
                //stateHolder.setColorStyle(PaletteStyle.PRIMARY.id)
            }
            preview.rightComplication.setOnClickListener {
                Log.d(TAG, "onClickRightComplicationButton()")
                stateHolder.setComplication(Complication.Right.id.value)
                //stateHolder.setColorStyle(PaletteStyle.SECONDARY.id)
            }
        }
    }

    private fun updateWatchFacePreview(
        userStylesAndPreview: WatchFaceConfigStateHolder.UserStylesAndPreview
    ) {
        binding.preview.watchFaceBackground.setImageBitmap(userStylesAndPreview.previewImage)
    }

    fun onClickColorStylePickerButton(view: View) {
        val paletteStyleList = enumValues<PaletteStyle>()
        val newColorStyle: PaletteStyle = paletteStyleList.random()
        stateHolder.setColorStyle(newColorStyle.id)
    }

    companion object {
        const val TAG = "WatchFaceConfigActivity"
    }
}
