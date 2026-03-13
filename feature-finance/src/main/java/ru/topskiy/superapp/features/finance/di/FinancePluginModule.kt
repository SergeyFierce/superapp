package ru.topskiy.superapp.features.finance.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import ru.topskiy.superapp.core.services.ServicePlugin
import ru.topskiy.superapp.features.finance.FinancePlugin

@Module
@InstallIn(SingletonComponent::class)
abstract class FinancePluginModule {

    @Binds
    @IntoSet
    abstract fun bindFinancePlugin(impl: FinancePlugin): ServicePlugin
}
