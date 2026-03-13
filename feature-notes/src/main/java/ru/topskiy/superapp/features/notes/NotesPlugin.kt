package ru.topskiy.superapp.features.notes

import ru.topskiy.superapp.core.services.ServiceDescriptor
import ru.topskiy.superapp.core.services.ServicePlugin
import ru.topskiy.superapp.core.services.ServiceProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotesPlugin @Inject constructor(
    private val provider: NotesServiceProvider,
) : ServicePlugin {
    override val descriptor: ServiceDescriptor get() = provider.descriptor
    override val serviceProvider: ServiceProvider get() = provider
}
