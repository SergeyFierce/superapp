package ru.topskiy.superapp.services.planner.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import ru.topskiy.superapp.platform.api.ServicePlugin
import ru.topskiy.superapp.services.planner.PlannerPlugin

@Module
@InstallIn(SingletonComponent::class)
abstract class PlannerPluginModule {

    @Binds
    @IntoSet
    abstract fun bindPlannerPlugin(impl: PlannerPlugin): ServicePlugin
}
