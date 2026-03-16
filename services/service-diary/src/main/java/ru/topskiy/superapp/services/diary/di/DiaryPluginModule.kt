package ru.topskiy.superapp.services.diary.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import ru.topskiy.superapp.platform.api.ServicePlugin
import ru.topskiy.superapp.services.diary.DiaryPlugin

@Module
@InstallIn(SingletonComponent::class)
abstract class DiaryPluginModule {

    @Binds
    @IntoSet
    abstract fun bindDiaryPlugin(impl: DiaryPlugin): ServicePlugin
}
