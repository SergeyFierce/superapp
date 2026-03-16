package ru.topskiy.superapp.services.finance.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import ru.topskiy.superapp.platform.api.HomeBlockProvider
import ru.topskiy.superapp.platform.api.SearchProvider
import ru.topskiy.superapp.services.finance.FinanceHomeBlock
import ru.topskiy.superapp.services.finance.FinanceSearchProvider

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
