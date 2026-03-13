package ru.topskiy.superapp.features.notes.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import ru.topskiy.superapp.core.home.HomeBlockProvider
import ru.topskiy.superapp.core.search.SearchProvider
import ru.topskiy.superapp.features.notes.NotesHomeBlock
import ru.topskiy.superapp.features.notes.NotesSearchProvider

@Module
@InstallIn(SingletonComponent::class)
abstract class NotesModule {

    @Binds
    @IntoSet
    abstract fun bindHomeBlock(impl: NotesHomeBlock): HomeBlockProvider

    @Binds
    @IntoSet
    abstract fun bindSearchProvider(impl: NotesSearchProvider): SearchProvider
}
