package ru.topskiy.superapp

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import ru.topskiy.superapp.core.bootstrap.AppBootstrap
import ru.topskiy.superapp.core.services.ServiceManager
import javax.inject.Inject

@HiltAndroidApp
class SuperApp : Application(), Configuration.Provider {

    @Inject lateinit var workerFactory: HiltWorkerFactory

    /** Eager init для bootstrap-компонентов, зарегистрированных во feature-модулях. */
    @Inject lateinit var appBootstraps: Set<@JvmSuppressWildcards AppBootstrap>

    /** Управляет lifecycle background-частей сервисов */
    @Inject lateinit var serviceManager: ServiceManager

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }
}
