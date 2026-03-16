package ru.topskiy.superapp.services.notes.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import ru.topskiy.superapp.platform.api.ServicePlugin
import ru.topskiy.superapp.services.notes.NotesPlugin

@Module
@InstallIn(SingletonComponent::class)
abstract class NotesPluginModule {

    @Binds
    @IntoSet
    abstract fun bindNotesPlugin(impl: NotesPlugin): ServicePlugin
}
