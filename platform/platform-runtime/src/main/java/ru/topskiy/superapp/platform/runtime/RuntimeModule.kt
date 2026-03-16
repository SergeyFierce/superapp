package ru.topskiy.superapp.platform.runtime

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * DI-модуль рантайма платформы.
 *
 * Пробрасывает реализации [DefaultServiceRegistry] и [DefaultServiceManager]
 * как интерфейсы [ServiceRegistry] и [ServiceManager] для всего приложения.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RuntimeModule {

    @Binds
    @Singleton
    abstract fun bindServiceRegistry(impl: DefaultServiceRegistry): ServiceRegistry

    @Binds
    @Singleton
    abstract fun bindServiceManager(impl: DefaultServiceManager): ServiceManager
}

