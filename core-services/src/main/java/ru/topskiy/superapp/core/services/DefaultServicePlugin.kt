package ru.topskiy.superapp.core.services

/**
 * Стандартная реализация [ServicePlugin].
 * Используется когда сервис не требует кастомной логики на уровне Plugin.
 */
class DefaultServicePlugin(
    override val descriptor: ServiceDescriptor,
    override val serviceProvider: ServiceProvider,
) : ServicePlugin
