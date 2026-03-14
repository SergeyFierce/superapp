package ru.topskiy.superapp.platform.api

interface ServicePlugin {
    val descriptor: ServiceDescriptor
    val serviceProvider: ServiceProvider
}
