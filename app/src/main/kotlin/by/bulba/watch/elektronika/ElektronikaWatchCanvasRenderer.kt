package by.bulba.watch.elektronika

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.util.Log
import android.view.SurfaceHolder
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.DrawMode
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.complications.rendering.CanvasComplicationDrawable
import androidx.wear.watchface.complications.rendering.ComplicationDrawable
import androidx.wear.watchface.style.CurrentUserStyleRepository
import by.bulba.watch.elektronika.data.watchface.WatchFaceData
import by.bulba.watch.elektronika.factory.DefaultWatchFaceDataFactory
import by.bulba.watch.elektronika.provider.DefaultPaletteStyleProvider
import by.bulba.watch.elektronika.provider.PaletteStyleStateProviderImpl
import by.bulba.watch.elektronika.renderer.RendererDrawer
import by.bulba.watch.elektronika.renderer.compositeRendererDrawer
import by.bulba.watch.elektronika.renderer.impl.BackgroundRendererDrawer
import by.bulba.watch.elektronika.renderer.impl.BatteryRendererDrawer
import by.bulba.watch.elektronika.renderer.impl.BottomLabelRendererDrawer
import by.bulba.watch.elektronika.renderer.impl.CenterRectRendererDrawer
import by.bulba.watch.elektronika.renderer.impl.DigitalClockFaceRendererDrawer
import by.bulba.watch.elektronika.renderer.impl.LabelRendererDrawer
import by.bulba.watch.elektronika.renderer.impl.RendererDrawerType
import by.bulba.watch.elektronika.repository.DefaultDigitalClockTimeFormatProvider
import by.bulba.watch.elektronika.repository.DigitalClockTimeFormatProviderImpl
import by.bulba.watch.elektronika.repository.WatchFaceDataStateRepository
import by.bulba.watch.elektronika.repository.WatchFaceDataStateRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.ZonedDateTime
import kotlin.time.Duration.Companion.milliseconds

internal class ElektronikaWatchCanvasRenderer(
    private val context: Context,
    surfaceHolder: SurfaceHolder,
    watchState: WatchState,
    private val complicationSlotsManager: ComplicationSlotsManager,
    currentUserStyleRepository: CurrentUserStyleRepository,
    canvasType: Int,
    private val scope: CoroutineScope,
    private val dataRepository: WatchFaceDataStateRepository,
) : Renderer.CanvasRenderer2<ElektronikaWatchCanvasRenderer.DigitalSharedAssets>(
    surfaceHolder = surfaceHolder,
    currentUserStyleRepository = currentUserStyleRepository,
    watchState = watchState,
    canvasType = canvasType,
    interactiveDrawModeUpdateDelayMillis = FRAME_PERIOD.inWholeMilliseconds,
    clearWithBackgroundTintBeforeRenderingHighlightLayer = false
) {
    class DigitalSharedAssets : SharedAssets {
        override fun onDestroy() {
        }
    }

    private var rendererDrawer: RendererDrawer = createRendererDrawer(dataRepository.state.value)

    init {
//        currentUserStyleRepository.userStyle
//            .onEach(::updateWatchFaceData)
//            .launchIn(scope)
        dataRepository.state
            .onEach { watchFaceData ->
                rendererDrawer = createRendererDrawer(watchFaceData)
                for ((_, complication) in complicationSlotsManager.complicationSlots) {
                    ComplicationDrawable.getDrawable(
                        context,
                        watchFaceData.getPalette().complicationStyleDrawableId
                    )?.let {
                        (complication.renderer as CanvasComplicationDrawable).drawable = it
                    }
                }
            }.launchIn(scope)
    }

    override suspend fun createSharedAssets(): DigitalSharedAssets = DigitalSharedAssets()

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
        when (renderParameters.drawMode) {
            DrawMode.AMBIENT -> WatchFaceData.Mode.AMBIENT
            DrawMode.INTERACTIVE,
            DrawMode.LOW_BATTERY_INTERACTIVE,
            DrawMode.MUTE -> WatchFaceData.Mode.ACTIVE
        }.also { mode ->
            dataRepository.update { watchFaceData ->
                watchFaceData.copy(mode = mode)
            }
        }

        rendererDrawer.draw(
            canvas = canvas,
            bounds = bounds,
            zonedDateTime = zonedDateTime,
        )

        drawComplications(canvas, zonedDateTime)
    }

    private fun drawComplications(canvas: Canvas, zonedDateTime: ZonedDateTime) {
        when (dataRepository.state.value.mode) {
            WatchFaceData.Mode.AMBIENT -> return
            WatchFaceData.Mode.ACTIVE -> complicationSlotsManager.complicationSlots
                .onEach { (_, complication) ->
                    if (complication.enabled) {
                        complication.render(canvas, zonedDateTime, renderParameters)
                    }
                }
        }
    }

    private fun createRendererDrawer(watchFaceData: WatchFaceData): RendererDrawer {
        return compositeRendererDrawer(
            watchFaceData.mode.rendererTypes.map { rendererDrawerType ->
                when (rendererDrawerType) {
                    RendererDrawerType.Background ->
                        BackgroundRendererDrawer(context, watchFaceData)

                    RendererDrawerType.Battery ->
                        BatteryRendererDrawer(context, watchFaceData)

                    RendererDrawerType.BottomLabel ->
                        BottomLabelRendererDrawer(context, watchFaceData)

                    RendererDrawerType.CenterRect ->
                        CenterRectRendererDrawer(context, watchFaceData)

                    RendererDrawerType.DigitalClock ->
                        DigitalClockFaceRendererDrawer(context, watchFaceData)

                    RendererDrawerType.Label ->
                        LabelRendererDrawer(context, watchFaceData)
                }
            }
        )
    }

    companion object {
        private const val TAG = "ElektronikaWatchCanvasRenderer"
        private val FRAME_PERIOD = 500.milliseconds
    }
}
