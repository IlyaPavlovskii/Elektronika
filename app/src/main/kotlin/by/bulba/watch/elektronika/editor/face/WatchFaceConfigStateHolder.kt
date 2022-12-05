package by.bulba.watch.elektronika.editor.face

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.wear.watchface.DrawMode
import androidx.wear.watchface.RenderParameters
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.style.UserStyle
import androidx.wear.watchface.style.WatchFaceLayer
import by.bulba.watch.elektronika.data.watchface.PaletteStyle
import by.bulba.watch.elektronika.editor.root.WatchSettingsRootHolder
import by.bulba.watch.elektronika.provider.DefaultPaletteStyleProvider
import by.bulba.watch.elektronika.repository.platform.COLOR_STYLE_SETTING
import by.bulba.watch.elektronika.utils.findSelectedOption
import by.bulba.watch.elektronika.utils.makeCircle
import by.bulba.watch.elektronika.utils.makeRoundedAngles
import by.bulba.watch.elektronika.utils.setNewOptionId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.yield
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

internal class WatchFaceConfigStateHolder(
    private val scope: CoroutineScope,
    private val isScreenRound: Boolean,
    private val defaultPaletteStyleProvider: DefaultPaletteStyleProvider =
        DefaultPaletteStyleProvider.create(),
    private val applyPaletteStyleDelay: Duration = DEFAULT_APPLY_PALETTE_STYLE_DELAY
) {
    private val editorSession = WatchSettingsRootHolder.getEditorSession()

    val uiState: StateFlow<EditWatchFaceUiState> = flow<EditWatchFaceUiState> {
        val userStylesAndPreviewState: StateFlow<List<UserStylesAndPreview>> =
            editorSession.complicationsPreviewData.map { complicationsPreviewData ->
                val userStyleValue = editorSession.userStyle.value
                val selectedPaletteStyleId = userStyleValue.getSelectedPaletteStyleIdentifier()
                val userStylesAndPreviewList = defaultPaletteStyleProvider.collection()
                    .map(PaletteStyle::id).map { paletteStyleId ->
                        setPalette(paletteStyleId)
                        delay(applyPaletteStyleDelay)
                        val bitmap = createWatchFacePreview(complicationsPreviewData)
                        UserStylesAndPreview(
                            paletteStyleId = paletteStyleId,
                            previewImage = bitmap,
                        )
                    }
                setPalette(selectedPaletteStyleId)
                userStylesAndPreviewList
            }.stateIn(
                scope = scope + Dispatchers.Main.immediate,
                started = SharingStarted.Eagerly,
                initialValue = emptyList(),
            )

        emitAll(
            combine(
                editorSession.userStyle,
                userStylesAndPreviewState,
            ) { userStyle, previews ->
                yield()

                val selectedPaletteStyleId = userStyle.getSelectedPaletteStyleIdentifier()

                EditWatchFaceUiState.Success(
                    selectedPaletteStyleId = selectedPaletteStyleId,
                    previews = previews
                )
            }
        )
    }.stateIn(
        scope = scope + Dispatchers.Main.immediate,
        started = SharingStarted.Eagerly,
        initialValue = EditWatchFaceUiState.Loading
    )

    private fun createWatchFacePreview(
        complicationsPreviewData: Map<Int, ComplicationData>
    ): Bitmap {
        val bitmap = editorSession.renderWatchFaceToBitmap(
            renderParameters = RenderParameters(
                drawMode = DrawMode.INTERACTIVE,
                watchFaceLayers = WatchFaceLayer.ALL_WATCH_FACE_LAYERS,
                highlightLayer = RenderParameters.HighlightLayer(
                    RenderParameters.HighlightedElement.AllComplicationSlots,
                    Color.RED,
                    Color.argb(128, 0, 0, 0)
                ),
            ),
            instant = editorSession.previewReferenceInstant,
            slotIdToComplicationData = complicationsPreviewData
        ).let { bitmap ->
            if (isScreenRound) {
                bitmap.makeCircle()
            } else {
                bitmap.makeRoundedAngles()
            }
        }

        return bitmap
    }

    fun chooseComplication(complicationLocation: Int) = scope.launch(Dispatchers.Main.immediate) {
        editorSession.openComplicationDataSourceChooser(complicationLocation)
    }

    fun setPalette(newColorStyleId: PaletteStyle.Identifier) =
        editorSession.userStyle.setNewOptionId(COLOR_STYLE_SETTING) { option ->
            option.id.toString() == newColorStyleId.value
        }

    private fun UserStyle.getSelectedPaletteStyleIdentifier(): PaletteStyle.Identifier =
        PaletteStyle.Identifier(
            requireNotNull(
                this.findSelectedOption(COLOR_STYLE_SETTING).toString()
            )
        )

    sealed class EditWatchFaceUiState {
        data class Success(
            val selectedPaletteStyleId: PaletteStyle.Identifier,
            val previews: List<UserStylesAndPreview>,
        ) : EditWatchFaceUiState() {
            fun getSelectedPosition(): Int = previews.indexOfFirst { userStylesAndPreview ->
                userStylesAndPreview.paletteStyleId == selectedPaletteStyleId
            }
        }

        object Loading : EditWatchFaceUiState()
    }

    data class UserStylesAndPreview(
        val paletteStyleId: PaletteStyle.Identifier,
        val previewImage: Bitmap,
    )

    companion object {
        private const val TAG = "WatchFaceConfigStateHolder"
        private val DEFAULT_APPLY_PALETTE_STYLE_DELAY = 100.milliseconds
    }
}