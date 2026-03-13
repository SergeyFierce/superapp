package ru.topskiy.superapp.features.planner.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.topskiy.superapp.features.planner.api.PlannerApi
import ru.topskiy.superapp.features.planner.impl.PlannerApiImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class PlannerApiModule {

    @Binds
    abstract fun bindPlannerApi(impl: PlannerApiImpl): PlannerApi
}
