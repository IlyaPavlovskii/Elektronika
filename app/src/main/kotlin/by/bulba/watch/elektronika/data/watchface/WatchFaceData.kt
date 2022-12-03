package by.bulba.watch.elektronika.data.watchface

import androidx.annotation.IntRange
import by.bulba.watch.elektronika.R
import by.bulba.watch.elektronika.renderer.impl.RendererDrawerType
import by.bulba.watch.elektronika.utils.wrapper.DimensWrapper
import by.bulba.watch.elektronika.utils.wrapper.DrawableWrapper
import by.bulba.watch.elektronika.utils.wrapper.TextWrapper

/**
 * Represents all data needed to render an analog watch face.
 */
internal data class WatchFaceData(
    private val activePalette: WatchFaceColorPalette,
    private val ambientPalette: WatchFaceColorPalette,
    val mode: Mode,
    val label: Label = Label(
        label = TextWrapper.Id(R.string.elektronika__top_label),
    ),
    val centerRect: CenterRect = CenterRect(
        strokeWidth = DimensWrapper.Id(R.dimen.elektronika__stroke_width)
    ),
    val digitalClock: DigitalClock,
    val bottomLabel: Label = Label(
        label = TextWrapper.Id(R.string.elektronika__bottom_label),
    ),
    val battery: Battery = Battery(
        level = Battery.Level(0u),
        state = Battery.State.NOT_CHARGING,
    )
) {

    fun getPalette(): WatchFaceColorPalette = when(mode) {
        Mode.AMBIENT -> ambientPalette
        Mode.ACTIVE -> activePalette
    }

    enum class Mode(
        val rendererTypes: Set<RendererDrawerType>,
        val snapshotTime: String,
    ) {
        AMBIENT(
            rendererTypes = setOf(
                RendererDrawerType.Background,
                RendererDrawerType.Label,
                RendererDrawerType.DigitalClock,
                RendererDrawerType.BottomLabel,
            ),
            snapshotTime = "00:00",
        ),
        ACTIVE(
            rendererTypes = setOf(
                RendererDrawerType.Background,
                RendererDrawerType.Label,
                RendererDrawerType.CenterRect,
                RendererDrawerType.DigitalClock,
                RendererDrawerType.Battery,
                RendererDrawerType.BottomLabel
            ),
            snapshotTime = "00:00:00",
        ),
        ;
    }

    data class Label(
        val label: TextWrapper,
    )

    data class CenterRect(
        val strokeWidth: DimensWrapper,
    )

    data class DigitalClock(
        val digitalClockTimeFormat: DigitalClockTimeFormat,
    )

    data class Battery(
        val level: Level,
        val state: State,
    ) {

        fun getIcon(): DrawableWrapper = when(state) {
            State.CHARGING -> DrawableWrapper.Id(R.drawable.baseline_battery_charging_full_24)
            State.NOT_CHARGING -> {
                val state = BatteryIconState.values().firstOrNull { batteryIconState ->
                    level.value in batteryIconState.range
                } ?: BatteryIconState.STATE_FULL
                state.icon
            }
        }

        @JvmInline
        value class Level(
            @IntRange(from = 0, to = 100) val value: UInt
            ) {
            init {
                check(value in LEVEL_RANGE) {
                    "Incorrect value $value. Value must be in range [0;100]"
                }
            }

            fun getFloatValue(): Float = value.toFloat() / PERCENT_FACTOR

            companion object {
                private val LEVEL_RANGE = 0u..100u
                private const val PERCENT_FACTOR = 100f
            }
        }

        enum class State {
            CHARGING,
            NOT_CHARGING,
        }

        private enum class BatteryIconState(
            val range: UIntRange,
            val icon: DrawableWrapper,
        ) {
            STATE_0(
                icon = DrawableWrapper.Id(R.drawable.baseline_battery_0_bar_24),
                range = 0u..14u
            ),
            STATE_1(
                icon = DrawableWrapper.Id(R.drawable.baseline_battery_1_bar_24),
                range = 14u..28u
            ),
            STATE_2(
                icon = DrawableWrapper.Id(R.drawable.baseline_battery_2_bar_24),
                range = 28u..42u
            ),
            STATE_3(
                icon = DrawableWrapper.Id(R.drawable.baseline_battery_3_bar_24),
                range = 42u..56u
            ),
            STATE_4(
                icon = DrawableWrapper.Id(R.drawable.baseline_battery_4_bar_24),
                range = 56u..70u
            ),
            STATE_5(
                icon = DrawableWrapper.Id(R.drawable.baseline_battery_5_bar_24),
                range = 70u..84u
            ),
            STATE_6(
                icon = DrawableWrapper.Id(R.drawable.baseline_battery_6_bar_24),
                range = 84u..95u
            ),
            STATE_FULL(
                icon = DrawableWrapper.Id(R.drawable.baseline_battery_full_24),
                range = 95u..100u
            ),
        }
    }
}
