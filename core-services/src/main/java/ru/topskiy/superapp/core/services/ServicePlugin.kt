package ru.topskiy.superapp.core.services

/**
 * Верхнеуровневый объект сервиса — единая точка регистрации в системе.
 *
 * Объединяет метаданные ([ServiceDescriptor]) и реализацию ([ServiceProvider]).
 * Регистрируется через Hilt multibindings (@Binds @IntoSet).
 *
 * [ServiceProvider] остаётся полноправным объектом и продолжает отвечать за:
 *   - навигацию
 *   - home-блоки (через HomeBlockProvider)
 *   - поиск (через SearchProvider)
 *   - background lifecycle (через AppService)
 */
interface ServicePlugin {
    val descriptor: ServiceDescriptor
    val serviceProvider: ServiceProvider
}
