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

import android.view.SurfaceHolder
import androidx.wear.watchface.CanvasType
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.WatchFace
import androidx.wear.watchface.WatchFaceService
import androidx.wear.watchface.WatchFaceType
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.style.CurrentUserStyleRepository
import androidx.wear.watchface.style.UserStyleSchema
import by.bulba.watch.elektronika.complication.DefaultComplicationSlotsManagerFactory
import by.bulba.watch.elektronika.complication.impl.DefaultCanvasComplicationFactory
import by.bulba.watch.elektronika.complication.impl.LeftComplicationSlotFactory
import by.bulba.watch.elektronika.complication.impl.RightComplicationSlotFactory
import by.bulba.watch.elektronika.utils.createUserStyleSchema

/**
 * Handles much of the boilerplate needed to implement a watch face (minus rendering code; see
 * [ElektronikaWatchCanvasRenderer]) including the complications and settings (styles user can change on
 * the watch face).
 */
internal class ElektronikaWatchFaceService : WatchFaceService() {

    // Used by Watch Face APIs to construct user setting options and repository.
    override fun createUserStyleSchema(): UserStyleSchema =
        createUserStyleSchema(context = applicationContext)

    // Creates all complication user settings and adds them to the existing user settings
    // repository.
    override fun createComplicationSlotsManager(
        currentUserStyleRepository: CurrentUserStyleRepository
    ): ComplicationSlotsManager {
        val canvasComplicationFactory = DefaultCanvasComplicationFactory(
            context = applicationContext,
        )
        return DefaultComplicationSlotsManagerFactory(
            LeftComplicationSlotFactory(
                canvasComplicationFactory = canvasComplicationFactory,
            ),
            RightComplicationSlotFactory(
                canvasComplicationFactory = canvasComplicationFactory,
            ),
        ).create(currentUserStyleRepository)
    }

    override suspend fun createWatchFace(
        surfaceHolder: SurfaceHolder,
        watchState: WatchState,
        complicationSlotsManager: ComplicationSlotsManager,
        currentUserStyleRepository: CurrentUserStyleRepository
    ): WatchFace {
        val renderer = ElektronikaWatchCanvasRenderer(
            context = applicationContext,
            surfaceHolder = surfaceHolder,
            watchState = watchState,
            complicationSlotsManager = complicationSlotsManager,
            currentUserStyleRepository = currentUserStyleRepository,
            canvasType = CanvasType.HARDWARE,
        )

        // Creates the watch face.
        return WatchFace(
            watchFaceType = WatchFaceType.DIGITAL,
            renderer = renderer
        )
    }
}
