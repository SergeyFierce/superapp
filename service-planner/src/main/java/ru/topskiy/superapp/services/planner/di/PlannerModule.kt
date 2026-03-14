package ru.topskiy.superapp.services.planner.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import ru.topskiy.superapp.platform.api.HomeBlockProvider
import ru.topskiy.superapp.platform.api.SearchProvider
import ru.topskiy.superapp.services.planner.PlannerHomeBlock
import ru.topskiy.superapp.services.planner.PlannerSearchProvider

@Module
@InstallIn(SingletonComponent::class)
abstract class PlannerModule {

    @Binds
    @IntoSet
    abstract fun bindHomeBlock(impl: PlannerHomeBlock): HomeBlockProvider

    @Binds
    @IntoSet
    abstract fun bindSearchProvider(impl: PlannerSearchProvider): SearchProvider
}
