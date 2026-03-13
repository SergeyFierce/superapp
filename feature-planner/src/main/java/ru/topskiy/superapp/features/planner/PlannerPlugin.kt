package ru.topskiy.superapp.features.planner

import ru.topskiy.superapp.core.services.ServiceDescriptor
import ru.topskiy.superapp.core.services.ServicePlugin
import ru.topskiy.superapp.core.services.ServiceProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlannerPlugin @Inject constructor(
    private val provider: PlannerServiceProvider,
) : ServicePlugin {
    override val descriptor: ServiceDescriptor get() = provider.descriptor
    override val serviceProvider: ServiceProvider get() = provider
}
