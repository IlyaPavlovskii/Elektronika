package by.bulba.watch.elektronika.editor.face

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.wear.watchface.DrawMode
import androidx.wear.watchface.RenderParameters
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.editor.EditorSession
import androidx.wear.watchface.style.UserStyle
import androidx.wear.watchface.style.UserStyleSchema
import androidx.wear.watchface.style.UserStyleSetting
import androidx.wear.watchface.style.WatchFaceLayer
import by.bulba.watch.elektronika.data.watchface.PaletteStyle
import by.bulba.watch.elektronika.repository.platform.COLOR_STYLE_SETTING
import by.bulba.watch.elektronika.utils.makeCircle
import by.bulba.watch.elektronika.utils.makeRoundedAngles
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.yield

internal class WatchFaceConfigStateHolder(
    private val scope: CoroutineScope,
    private val activity: ComponentActivity,
    private val rendererPaletteStyleIds: List<PaletteStyle.Identifier> = listOf(
        PaletteStyle.PRIMARY.id,
        PaletteStyle.SECONDARY.id,
    ),
) {
    private lateinit var editorSession: EditorSession
    private lateinit var colorStyleKey: UserStyleSetting.ListUserStyleSetting

    val uiState: StateFlow<EditWatchFaceUiState> = flow<EditWatchFaceUiState> {
        editorSession = EditorSession.createOnWatchEditorSession(activity = activity)
        extractsUserStyles(editorSession.userStyleSchema)

        val userStylesAndPreviewState: StateFlow<List<UserStylesAndPreview>> =
            editorSession.complicationsPreviewData.map { complicationsPreviewData ->
                val userStyleValue = editorSession.userStyle.value
                val selectedPaletteStyleId = userStyleValue.getSelectedPaletteStyleIdentifier()
                val userStylesAndPreviewList = rendererPaletteStyleIds.map { paletteStyleId ->
                    Log.d(TAG, "\tgenerate. setColor: $paletteStyleId")
                    setColorStyle(paletteStyleId)
                    val bitmap = createWatchFacePreview(complicationsPreviewData)
                    Log.d(TAG, "\tbitmap")
                    UserStylesAndPreview(
                        paletteStyleId = paletteStyleId,
                        previewImage = bitmap,
                    )
                }
                setColorStyle(selectedPaletteStyleId)
                Log.d(TAG, "Complete generation. setColor: $selectedPaletteStyleId")
                userStylesAndPreviewList
            }.stateIn(
                scope = scope + Dispatchers.Main.immediate,
                started = SharingStarted.Eagerly,
                initialValue = emptyList(),
            )

        emitAll(
            combine(
                editorSession.userStyle,
                userStylesAndPreviewState.drop(1),
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

    private fun extractsUserStyles(userStyleSchema: UserStyleSchema) {
        for (setting in userStyleSchema.userStyleSettings) {
            when (setting.id) {
                COLOR_STYLE_SETTING -> {
                    colorStyleKey = setting as UserStyleSetting.ListUserStyleSetting
                }
            }
        }
    }

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
            if (activity.resources.configuration.isScreenRound) {
                bitmap.makeCircle()
            } else {
                bitmap.makeRoundedAngles()
            }
        }

        return bitmap
    }

    fun setComplication(complicationLocation: Int) = scope.launch(Dispatchers.Main.immediate) {
        editorSession.openComplicationDataSourceChooser(complicationLocation)
    }

    fun setColorStyle(newColorStyleId: PaletteStyle.Identifier) {
        val userStyleSettingList = editorSession.userStyleSchema.userStyleSettings
        val colorUserStyleSetting = userStyleSettingList.firstOrNull { userStyleSetting ->
            userStyleSetting.id == COLOR_STYLE_SETTING
        }.let { it as? UserStyleSetting.ListUserStyleSetting } ?: return
        colorUserStyleSetting.options
            .firstOrNull { option ->
                option.id.toString() == newColorStyleId.value
            }
            ?.also { option -> setUserStyleOption(colorStyleKey, option) }
    }

    private fun setUserStyleOption(
        userStyleSetting: UserStyleSetting,
        userStyleOption: UserStyleSetting.Option
    ) {
        val mutableUserStyle = editorSession.userStyle.value.toMutableUserStyle()
        mutableUserStyle[userStyleSetting] = userStyleOption
        editorSession.userStyle.value = mutableUserStyle.toUserStyle()
    }

    private fun UserStyle.getSelectedPaletteStyleIdentifier(): PaletteStyle.Identifier =
        PaletteStyle.Identifier(requireNotNull(this[colorStyleKey]?.id?.toString()))

    sealed class EditWatchFaceUiState {
        data class Success(
                val selectedPaletteStyleId: PaletteStyle.Identifier,
                val previews: List<UserStylesAndPreview>,
        ) : EditWatchFaceUiState()

        object Loading : EditWatchFaceUiState()
    }

    data class UserStylesAndPreview(
        val paletteStyleId: PaletteStyle.Identifier,
        val previewImage: Bitmap,
    )

    companion object {
        private const val TAG = "WatchFaceConfigStateHolder"
    }
}