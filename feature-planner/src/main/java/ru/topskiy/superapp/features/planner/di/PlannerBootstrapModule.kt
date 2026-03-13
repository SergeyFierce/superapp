package ru.topskiy.superapp.features.planner.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import ru.topskiy.superapp.core.bootstrap.AppBootstrap
import ru.topskiy.superapp.features.planner.PlannerBootstrap

@Module
@InstallIn(SingletonComponent::class)
abstract class PlannerBootstrapModule {

    @Binds
    @IntoSet
    abstract fun bindPlannerBootstrap(impl: PlannerBootstrap): AppBootstrap
}
