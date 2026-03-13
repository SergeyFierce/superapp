package ru.topskiy.superapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.ElementsIntoSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import ru.topskiy.superapp.core.bootstrap.AppBootstrap
import ru.topskiy.superapp.core.commands.CommandBus
import ru.topskiy.superapp.core.di.ApplicationScope
import ru.topskiy.superapp.core.events.EventBus
import ru.topskiy.superapp.core.home.HomeBlockProvider
import ru.topskiy.superapp.core.search.SearchProvider
import ru.topskiy.superapp.core.services.ServicePlugin
import javax.inject.Singleton

/**
 * Базовый модуль приложения.
 *
 * Инициализирует пустые множества для multibindings — нужно,
 * чтобы Hilt мог инжектировать Set<T> даже без @IntoSet-зависимостей.
 *
 * ServicePlugin — главный регистрационный контракт сервиса.
 * HomeBlockProvider и SearchProvider — специализированные контракты,
 * регистрируются независимо в каждом feature-модуле.
 *
 * EventBus и CommandBus — pure Kotlin классы из core-events/core-commands,
 * предоставляются через Hilt как Singleton.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideEventBus(): EventBus = EventBus()

    @Provides
    @Singleton
    fun provideCommandBus(): CommandBus = CommandBus()

    @Provides
    @Singleton
    @ApplicationScope
    fun provideApplicationScope(): CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Default)


    @Provides
    @ElementsIntoSet
    fun provideInitialAppBootstraps(): Set<@JvmSuppressWildcards AppBootstrap> =
        emptySet()

    @Provides
    @ElementsIntoSet
    fun provideInitialServicePlugins(): Set<@JvmSuppressWildcards ServicePlugin> =
        emptySet()

    @Provides
    @ElementsIntoSet
    fun provideInitialHomeBlockProviders(): Set<@JvmSuppressWildcards HomeBlockProvider> =
        emptySet()

    @Provides
    @ElementsIntoSet
    fun provideInitialSearchProviders(): Set<@JvmSuppressWildcards SearchProvider> =
        emptySet()
}
