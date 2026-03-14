package ru.topskiy.superapp.services.diary

import ru.topskiy.superapp.platform.api.ServiceDescriptor
import ru.topskiy.superapp.platform.api.ServicePlugin
import ru.topskiy.superapp.platform.api.ServiceProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiaryPlugin @Inject constructor(
    private val provider: DiaryServiceProvider,
) : ServicePlugin {
    override val descriptor: ServiceDescriptor get() = provider.descriptor
    override val serviceProvider: ServiceProvider get() = provider
}
