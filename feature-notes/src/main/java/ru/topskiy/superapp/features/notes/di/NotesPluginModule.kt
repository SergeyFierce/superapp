package ru.topskiy.superapp.features.notes.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import ru.topskiy.superapp.core.services.ServicePlugin
import ru.topskiy.superapp.features.notes.NotesPlugin

@Module
@InstallIn(SingletonComponent::class)
abstract class NotesPluginModule {

    @Binds
    @IntoSet
    abstract fun bindNotesPlugin(impl: NotesPlugin): ServicePlugin
}
