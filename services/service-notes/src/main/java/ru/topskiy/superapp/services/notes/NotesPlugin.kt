package ru.topskiy.superapp.services.notes

import ru.topskiy.superapp.platform.api.ServiceDescriptor
import ru.topskiy.superapp.platform.api.ServicePlugin
import ru.topskiy.superapp.platform.api.ServiceProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotesPlugin @Inject constructor(
    private val provider: NotesServiceProvider,
) : ServicePlugin {
    override val descriptor: ServiceDescriptor get() = provider.descriptor
    override val serviceProvider: ServiceProvider get() = provider
}
