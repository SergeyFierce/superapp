package ru.topskiy.superapp.services.diary.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import ru.topskiy.superapp.platform.api.HomeBlockProvider
import ru.topskiy.superapp.platform.api.SearchProvider
import ru.topskiy.superapp.services.diary.DiaryHomeBlock
import ru.topskiy.superapp.services.diary.DiarySearchProvider

@Module
@InstallIn(SingletonComponent::class)
abstract class DiaryModule {

    @Binds
    @IntoSet
    abstract fun bindHomeBlock(impl: DiaryHomeBlock): HomeBlockProvider

    @Binds
    @IntoSet
    abstract fun bindSearchProvider(impl: DiarySearchProvider): SearchProvider
}
