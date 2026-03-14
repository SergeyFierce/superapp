package ru.topskiy.superapp.services.planner.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.topskiy.superapp.services.planner.api.PlannerApi
import ru.topskiy.superapp.services.planner.impl.PlannerApiImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class PlannerApiModule {

    @Binds
    abstract fun bindPlannerApi(impl: PlannerApiImpl): PlannerApi
}
