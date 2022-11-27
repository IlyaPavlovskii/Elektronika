/*
 * Copyright 2020 The Android Open Source Project
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
package by.bulba.watch.elektronika

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.util.Log
import android.view.SurfaceHolder
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.complications.rendering.CanvasComplicationDrawable
import androidx.wear.watchface.complications.rendering.ComplicationDrawable
import androidx.wear.watchface.style.CurrentUserStyleRepository
import androidx.wear.watchface.style.UserStyle
import androidx.wear.watchface.style.UserStyleSetting
import by.bulba.watch.elektronika.data.watchface.ColorStyleIdAndResourceIds
import by.bulba.watch.elektronika.data.watchface.WatchFaceColorPalette.Companion.convertToWatchFaceColorPalette
import by.bulba.watch.elektronika.data.watchface.WatchFaceData
import by.bulba.watch.elektronika.renderer.impl.BackgroundRendererDrawer
import by.bulba.watch.elektronika.renderer.impl.BottomLabelRendererDrawer
import by.bulba.watch.elektronika.renderer.impl.CenterRectRendererDrawer
import by.bulba.watch.elektronika.renderer.impl.DigitalClockFaceRendererDrawer
import by.bulba.watch.elektronika.renderer.impl.LabelRendererDrawer
import by.bulba.watch.elektronika.renderer.RendererDrawer
import by.bulba.watch.elektronika.renderer.compositeRendererDrawer
import by.bulba.watch.elektronika.utils.COLOR_STYLE_SETTING
import by.bulba.watch.elektronika.utils.DRAW_HOUR_PIPS_STYLE_SETTING
import by.bulba.watch.elektronika.utils.WATCH_HAND_LENGTH_STYLE_SETTING
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.time.ZonedDateTime


// Default for how long each frame is displayed at expected frame rate.
private const val FRAME_PERIOD_MS_DEFAULT: Long = 16L

internal class ElektronikaWatchCanvasRenderer(
    private val context: Context,
    surfaceHolder: SurfaceHolder,
    watchState: WatchState,
    private val complicationSlotsManager: ComplicationSlotsManager,
    currentUserStyleRepository: CurrentUserStyleRepository,
    canvasType: Int,
) : Renderer.CanvasRenderer2<ElektronikaWatchCanvasRenderer.DigitalSharedAssets>(
    surfaceHolder,
    currentUserStyleRepository,
    watchState,
    canvasType,
    FRAME_PERIOD_MS_DEFAULT,
    clearWithBackgroundTintBeforeRenderingHighlightLayer = false
) {
    class DigitalSharedAssets : SharedAssets {
        override fun onDestroy() {
        }
    }

    private val scope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private var watchFaceData: WatchFaceData = WatchFaceData()
    private var watchFaceColors = convertToWatchFaceColorPalette(
        context,
        watchFaceData.activeColorStyle,
        watchFaceData.ambientColorStyle
    )
    private var rendererDrawer: RendererDrawer = createRendererDrawer(watchFaceData)

    init {
        scope.launch {
            currentUserStyleRepository.userStyle.collect { userStyle ->
                updateWatchFaceData(userStyle)
            }
        }
    }

    override suspend fun createSharedAssets(): DigitalSharedAssets {
        return DigitalSharedAssets()
    }

    /*
     * Triggered when the user makes changes to the watch face through the settings activity. The
     * function is called by a flow.
     */
    private fun updateWatchFaceData(userStyle: UserStyle) {
        Log.d(TAG, "updateWatchFace(): $userStyle")

        var newWatchFaceData: WatchFaceData = watchFaceData

        // Loops through user style and applies new values to watchFaceData.
        for (options in userStyle) {
            when (options.key.id.toString()) {
                COLOR_STYLE_SETTING -> {
                    val listOption = options.value as
                        UserStyleSetting.ListUserStyleSetting.ListOption

                    newWatchFaceData = newWatchFaceData.copy(
                        activeColorStyle = ColorStyleIdAndResourceIds.getColorStyleConfig(
                            listOption.id.toString()
                        )
                    )
                }
                DRAW_HOUR_PIPS_STYLE_SETTING -> {
                    val booleanValue = options.value as
                        UserStyleSetting.BooleanUserStyleSetting.BooleanOption

                    newWatchFaceData = newWatchFaceData.copy(
                        drawHourPips = booleanValue.value
                    )
                }
                WATCH_HAND_LENGTH_STYLE_SETTING -> {
                    val doubleValue = options.value as
                        UserStyleSetting.DoubleRangeUserStyleSetting.DoubleRangeOption

                    // Updates length of minute hand based on edits from user.
                    val newMinuteHandDimensions = newWatchFaceData.minuteHandDimensions.copy(
                        lengthFraction = doubleValue.value.toFloat()
                    )

                    newWatchFaceData = newWatchFaceData.copy(
                        minuteHandDimensions = newMinuteHandDimensions
                    )
                }
            }
        }

        // Only updates if something changed.
        if (watchFaceData != newWatchFaceData) {
            watchFaceData = newWatchFaceData
            rendererDrawer = createRendererDrawer(newWatchFaceData)

            // Recreates Color and ComplicationDrawable from resource ids.
            watchFaceColors = convertToWatchFaceColorPalette(
                context,
                watchFaceData.activeColorStyle,
                watchFaceData.ambientColorStyle
            )

            // Applies the user chosen complication color scheme changes. ComplicationDrawables for
            // each of the styles are defined in XML so we need to replace the complication's
            // drawables.
            for ((_, complication) in complicationSlotsManager.complicationSlots) {
                ComplicationDrawable.getDrawable(
                    context,
                    watchFaceColors.complicationStyleDrawableId
                )?.let {
                    (complication.renderer as CanvasComplicationDrawable).drawable = it
                }
            }
        }
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy()")
        scope.cancel("AnalogWatchCanvasRenderer scope clear() request")
        super.onDestroy()
    }

    override fun renderHighlightLayer(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: DigitalSharedAssets
    ) {
        canvas.drawColor(
            Color.RED
            //renderParameters.highlightLayer!!.backgroundTint
        )

        for ((_, complication) in complicationSlotsManager.complicationSlots) {
            if (complication.enabled) {
                complication.renderHighlightLayer(canvas, zonedDateTime, renderParameters)
            }
        }
    }

    override fun render(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: DigitalSharedAssets
    ) {
//        val backgroundColor = when (renderParameters.drawMode) {
//            DrawMode.AMBIENT -> watchFaceColors.ambientBackgroundColor
//            DrawMode.INTERACTIVE,
//            DrawMode.LOW_BATTERY_INTERACTIVE,
//            DrawMode.MUTE -> watchFaceColors.activeBackgroundColor
//        }

        rendererDrawer.draw(
            canvas = canvas,
            bounds = bounds,
            zonedDateTime = zonedDateTime,
        )

        // CanvasComplicationDrawable already obeys rendererParameters.
         drawComplications(canvas, zonedDateTime)

//        if (renderParameters.drawMode == DrawMode.INTERACTIVE &&
//            renderParameters.watchFaceLayers.contains(WatchFaceLayer.BASE) &&
//            watchFaceData.drawHourPips
//        ) {
//        }
    }

    private fun drawComplications(canvas: Canvas, zonedDateTime: ZonedDateTime) {
        for ((_, complication) in complicationSlotsManager.complicationSlots) {
            if (complication.enabled) {
                complication.render(canvas, zonedDateTime, renderParameters)
            }
        }
    }

    private fun createRendererDrawer(watchFaceData: WatchFaceData): RendererDrawer =
        compositeRendererDrawer(
            BackgroundRendererDrawer(context, watchFaceData),
            CenterRectRendererDrawer(context, watchFaceData),
            LabelRendererDrawer(context, watchFaceData),
            BottomLabelRendererDrawer(context, watchFaceData),
            DigitalClockFaceRendererDrawer(context, watchFaceData)
        )

    companion object {
        private const val TAG = "AnalogWatchCanvasRenderer"
    }
}
