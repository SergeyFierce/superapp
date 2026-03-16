package ru.topskiy.superapp.services.planner

import ru.topskiy.superapp.platform.api.ServiceDescriptor
import ru.topskiy.superapp.platform.api.ServicePlugin
import ru.topskiy.superapp.platform.api.ServiceProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlannerPlugin @Inject constructor(
    private val provider: PlannerServiceProvider,
) : ServicePlugin {
    override val descriptor: ServiceDescriptor get() = provider.descriptor
    override val serviceProvider: ServiceProvider get() = provider
}
