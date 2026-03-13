package ru.topskiy.superapp

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import ru.topskiy.superapp.core.services.ServiceManager
import ru.topskiy.superapp.features.planner.impl.PlannerCommandHandler
import javax.inject.Inject

@HiltAndroidApp
class SuperApp : Application(), Configuration.Provider {

    @Inject lateinit var workerFactory: HiltWorkerFactory

    /** Подписывается на CommandBus и обрабатывает команды Планировщика */
    @Inject lateinit var plannerCommandHandler: PlannerCommandHandler

    /** Управляет lifecycle background-частей сервисов */
    @Inject lateinit var serviceManager: ServiceManager

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }
}