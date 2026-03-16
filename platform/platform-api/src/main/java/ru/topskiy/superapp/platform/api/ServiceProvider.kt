package ru.topskiy.superapp.platform.api

interface ServiceProvider {
    val descriptor: ServiceDescriptor
    fun createService(): AppService? = null
}
