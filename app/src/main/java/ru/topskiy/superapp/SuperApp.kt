package ru.topskiy.superapp

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import ru.topskiy.superapp.core.services.ServiceManager
import ru.topskiy.superapp.features.planner.impl.PlannerCommandHandler
import javax.inject.Inject

/**
 * Точка входа приложения.
 *
 * Реализует [Configuration.Provider] для передачи [HiltWorkerFactory]
 * в WorkManager — Workers получают зависимости через Hilt.
 *
 * @Inject lateinit var — принудительная инициализация Singleton-объектов,
 * которые должны стартовать до открытия первого экрана.
 */
@HiltAndroidApp
class SuperApp : Application(), Configuration.Provider {

    @Inject lateinit var workerFactory: HiltWorkerFactory

    /** Подписывается на CommandBus и обрабатывает команды Планировщика */
    @Inject lateinit var plannerCommandHandler: PlannerCommandHandler

    /** Управляет lifecycle background-частей сервисов */
    @Inject lateinit var serviceManager: ServiceManager

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
