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
package by.bulba.watch.elektronika.data.watchface

import androidx.annotation.IntRange
import by.bulba.watch.elektronika.R
import by.bulba.watch.elektronika.utils.wrapper.ColorWrapper
import by.bulba.watch.elektronika.utils.wrapper.DimensWrapper
import by.bulba.watch.elektronika.utils.wrapper.DrawableWrapper
import by.bulba.watch.elektronika.utils.wrapper.TextWrapper
import java.time.format.DateTimeFormatter

// Defaults for the watch face. All private values aren't editable by the user, so they don't need
// to be exposed as settings defaults.
const val DRAW_HOUR_PIPS_DEFAULT = true

private const val HOUR_HAND_LENGTH_FRACTION = 0.21028f
private const val HOUR_HAND_WIDTH_FRACTION = 0.02336f

// Because the minute length is something the user can edit, we make it publicly
// accessible as a default. We also specify the minimum and maximum values for the user
// settings as well.
const val MINUTE_HAND_LENGTH_FRACTION_DEFAULT = 0.3783f
const val MINUTE_HAND_LENGTH_FRACTION_MINIMUM = 0.10000f
const val MINUTE_HAND_LENGTH_FRACTION_MAXIMUM = 0.40000f
private const val MINUTE_HAND_WIDTH_FRACTION = 0.0163f

private const val SECOND_HAND_LENGTH_FRACTION = 0.37383f
private const val SECOND_HAND_WIDTH_FRACTION = 0.00934f

// Used for corner roundness of the arms.
private const val ROUNDED_RECTANGLE_CORNERS_RADIUS = 1.5f
private const val SQUARE_RECTANGLE_CORNERS_RADIUS = 0.0f

private const val CENTER_CIRCLE_DIAMETER_FRACTION = 0.03738f
private const val OUTER_CIRCLE_STROKE_WIDTH_FRACTION = 0.00467f
private const val NUMBER_STYLE_OUTER_CIRCLE_RADIUS_FRACTION = 0.00584f

private const val GAP_BETWEEN_OUTER_CIRCLE_AND_BORDER_FRACTION = 0.03738f
private const val GAP_BETWEEN_HAND_AND_CENTER_FRACTION =
    0.01869f + CENTER_CIRCLE_DIAMETER_FRACTION / 2.0f

private const val NUMBER_RADIUS_FRACTION = 0.45f

/**
 * Represents all data needed to render an analog watch face.
 */
data class WatchFaceData(
    val activeColorStyle: ColorStyleIdAndResourceIds = ColorStyleIdAndResourceIds.RED,
    val ambientColorStyle: ColorStyleIdAndResourceIds = ColorStyleIdAndResourceIds.AMBIENT,
    val drawHourPips: Boolean = DRAW_HOUR_PIPS_DEFAULT,
    val hourHandDimensions: ArmDimensions = ArmDimensions(
        lengthFraction = HOUR_HAND_LENGTH_FRACTION,
        widthFraction = HOUR_HAND_WIDTH_FRACTION,
        xRadiusRoundedCorners = ROUNDED_RECTANGLE_CORNERS_RADIUS,
        yRadiusRoundedCorners = ROUNDED_RECTANGLE_CORNERS_RADIUS
    ),
    val minuteHandDimensions: ArmDimensions = ArmDimensions(
        lengthFraction = MINUTE_HAND_LENGTH_FRACTION_DEFAULT,
        widthFraction = MINUTE_HAND_WIDTH_FRACTION,
        xRadiusRoundedCorners = ROUNDED_RECTANGLE_CORNERS_RADIUS,
        yRadiusRoundedCorners = ROUNDED_RECTANGLE_CORNERS_RADIUS
    ),
    val secondHandDimensions: ArmDimensions = ArmDimensions(
        lengthFraction = SECOND_HAND_LENGTH_FRACTION,
        widthFraction = SECOND_HAND_WIDTH_FRACTION,
        xRadiusRoundedCorners = ROUNDED_RECTANGLE_CORNERS_RADIUS,
        yRadiusRoundedCorners = ROUNDED_RECTANGLE_CORNERS_RADIUS
    ),
    val centerCircleDiameterFraction: Float = CENTER_CIRCLE_DIAMETER_FRACTION,
    val numberRadiusFraction: Float = NUMBER_RADIUS_FRACTION,
    val outerCircleStokeWidthFraction: Float = OUTER_CIRCLE_STROKE_WIDTH_FRACTION,
    val numberStyleOuterCircleRadiusFraction: Float = NUMBER_STYLE_OUTER_CIRCLE_RADIUS_FRACTION,
    val gapBetweenOuterCircleAndBorderFraction: Float =
        GAP_BETWEEN_OUTER_CIRCLE_AND_BORDER_FRACTION,
    val gapBetweenHandAndCenterFraction: Float = GAP_BETWEEN_HAND_AND_CENTER_FRACTION,
    // ------------------------------------------------------------------------------------------
    val backgroundColor: ColorWrapper = ColorWrapper.Id(R.color.elektronika__background_color),
    val label: Label = Label(
        label = TextWrapper.Id(R.string.elektronika__top_label),
        labelColor = ColorWrapper.Id(R.color.elektronika__text_color),
    ),
    val centerRect: CenterRect = CenterRect(
        fillColor = ColorWrapper.Id(R.color.elektronika__center_rect_fill_color),
        strokeColor = ColorWrapper.Id(R.color.elektronika__center_rect_stroke_color),
        strokeWidth = DimensWrapper.Id(R.dimen.elektronika__stroke_width)
    ),
    val digitalClock: DigitalClock = DigitalClock(
        digitalClockColor = ColorWrapper.Id(R.color.elektronika__clock_text_color),
        timeFormatType = DigitalClock.TimeFormatType.HOURS_24,
    ),
    val bottomLabel: Label = Label(
        label = TextWrapper.Id(R.string.elektronika__bottom_label),
        labelColor = ColorWrapper.Id(R.color.elektronika__text_color),
    ),
    val battery: Battery = Battery(
        level = Battery.Level(0u),
        state = Battery.State.NOT_CHARGING,
    )
) {
    data class Label(
        val label: TextWrapper,
        val labelColor: ColorWrapper,
    )

    data class CenterRect(
        val fillColor: ColorWrapper,
        val strokeColor: ColorWrapper,
        val strokeWidth: DimensWrapper,
    )

    data class DigitalClock(
        val digitalClockColor: ColorWrapper,
        val timeFormatType: TimeFormatType,
    ) {
        enum class TimeFormatType(val dateTimeFormatter: DateTimeFormatter) {
            HOURS_12(dateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss")),
            HOURS_24(dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")),
        }
    }

    data class Battery(
        val level: Level,
        val state: State,
        val layerColor: ColorWrapper = ColorWrapper.Id(R.color.elektronika__battery_layer_color),
        val levelColor: ColorWrapper = ColorWrapper.Id(R.color.elektronika__battery_level_color),
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
