package ru.topskiy.superapp.features.finance.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import ru.topskiy.superapp.core.home.HomeBlockProvider
import ru.topskiy.superapp.core.search.SearchProvider
import ru.topskiy.superapp.features.finance.FinanceHomeBlock
import ru.topskiy.superapp.features.finance.FinanceSearchProvider

@Module
@InstallIn(SingletonComponent::class)
abstract class FinanceModule {

    @Binds
    @IntoSet
    abstract fun bindHomeBlock(impl: FinanceHomeBlock): HomeBlockProvider

    @Binds
    @IntoSet
    abstract fun bindSearchProvider(impl: FinanceSearchProvider): SearchProvider
}
