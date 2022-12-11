package by.bulba.watch.elektronika

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
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
import by.bulba.watch.elektronika.factory.DefaultWatchFaceDataFactory
import by.bulba.watch.elektronika.provider.DefaultPaletteStyleProvider
import by.bulba.watch.elektronika.provider.PaletteStyleStateProviderImpl
import by.bulba.watch.elektronika.receiver.BatteryBroadcastReceiver
import by.bulba.watch.elektronika.repository.DefaultDigitalClockTimeFormatProvider
import by.bulba.watch.elektronika.repository.DigitalClockTimeFormatProviderImpl
import by.bulba.watch.elektronika.repository.WatchFaceDataStateRepository
import by.bulba.watch.elektronika.repository.WatchFaceDataStateRepositoryImpl
import by.bulba.watch.elektronika.repository.platform.UserStyleSchemaBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

internal class ElektronikaWatchFaceService : WatchFaceService() {

    private val userStyleSchemaBuilder: UserStyleSchemaBuilder = UserStyleSchemaBuilder()
    private val defaultPaletteStyleProvider: DefaultPaletteStyleProvider =
        DefaultPaletteStyleProvider.create()
    private val defaultDigitalClockTimeFormatProvider: DefaultDigitalClockTimeFormatProvider =
        DefaultDigitalClockTimeFormatProvider.create()
    private var dataStateRepository: WatchFaceDataStateRepository? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private var batteryReceiver: BroadcastReceiver? = null

    override fun onDestroy() {
        scope.cancel()
        batteryReceiver?.also(::unregisterReceiver)
        batteryReceiver = null
        super.onDestroy()
    }

    override fun createUserStyleSchema(): UserStyleSchema =
        userStyleSchemaBuilder.createUserStyleSchema(context = applicationContext)

    override fun createComplicationSlotsManager(
        currentUserStyleRepository: CurrentUserStyleRepository
    ): ComplicationSlotsManager {
        if (dataStateRepository == null) {
            dataStateRepository = createWatchFaceDataRepository(
                currentUserStyleRepository = currentUserStyleRepository
            )
        }

        val canvasComplicationFactory = DefaultCanvasComplicationFactory(
            context = applicationContext,
            drawableId = requireNotNull(dataStateRepository).state.value
                .getPalette().complicationStyleDrawableId
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
            dataRepository = requireNotNull(dataStateRepository),
            scope = scope,
        )
        return WatchFace(
            watchFaceType = WatchFaceType.DIGITAL,
            renderer = renderer
        )
    }

    private fun createWatchFaceDataRepository(
        currentUserStyleRepository: CurrentUserStyleRepository
    ): WatchFaceDataStateRepository {
        val repository: WatchFaceDataStateRepository = WatchFaceDataStateRepositoryImpl(
            scope = scope,
            defaultWatchFaceDataFactory = DefaultWatchFaceDataFactory(
                defaultPaletteStyleProvider = defaultPaletteStyleProvider,
                defaultDigitalClockTimeFormatProvider = defaultDigitalClockTimeFormatProvider,
            ),
            paletteStyleStateProvider = PaletteStyleStateProviderImpl(
                userStyleRepository = currentUserStyleRepository,
                defaultPaletteStyleProvider = defaultPaletteStyleProvider,
            ),
            digitalClockTimeFormatStateProvider = DigitalClockTimeFormatProviderImpl(
                userStyleRepository = currentUserStyleRepository,
                defaultDigitalClockTimeFormatProvider = defaultDigitalClockTimeFormatProvider,
            )
        )
        batteryReceiver = BatteryBroadcastReceiver(repository).also { receiver ->
            if (batteryReceiver != null) unregisterReceiver(batteryReceiver)
            registerReceiver(receiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        }
        return repository
    }
}
