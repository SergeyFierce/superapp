package ru.topskiy.superapp.core.services

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Реестр сервисов — единый источник истины о доступных сервисах.
 *
 * Заполняется через Hilt multibindings: каждый feature-модуль регистрирует
 * свой [ServicePlugin] через @Binds @IntoSet.
 *
 * Хранит [ServicePlugin], предоставляет [ServiceProvider] и [ServiceDescriptor]
 * через типизированные методы — система всегда работает только через реестр.
 */
@Singleton
class ServiceRegistry @Inject constructor(
    private val plugins: Set<@JvmSuppressWildcards ServicePlugin>,
) {
    // ── Plugin API ─────────────────────────────────────────────────────

    /** Все зарегистрированные плагины, отсортированные по названию */
    fun getAllPlugins(): List<ServicePlugin> =
        plugins.sortedBy { it.descriptor.title }

    // ── Provider API (основная точка доступа для системных компонентов) ─

    /** Все ServiceProvider через слой Plugin */
    fun getServiceProviders(): List<ServiceProvider> =
        plugins.map { it.serviceProvider }.sortedBy { it.descriptor.title }

    /** Обратная совместимость — алиас для [getServiceProviders] */
    fun getAllProviders(): List<ServiceProvider> = getServiceProviders()

    /** Найти ServiceProvider по идентификатору */
    fun getService(id: ServiceId): ServiceProvider? =
        plugins.find { it.descriptor.id == id }?.serviceProvider

    // ── Descriptor API ─────────────────────────────────────────────────

    /** Дескрипторы всех зарегистрированных сервисов */
    fun getDescriptors(): List<ServiceDescriptor> =
        plugins.map { it.descriptor }.sortedBy { it.title }

    // ── Filtered queries ───────────────────────────────────────────────

    /**
     * Активные сервисы с учётом пользовательских настроек.
     * Если [enabledIds] пуст — возвращаются все с [ServiceDescriptor.enabledByDefault] = true.
     */
    fun getEnabledProviders(enabledIds: Set<ServiceId> = emptySet()): List<ServiceProvider> =
        plugins
            .filter { plugin ->
                if (enabledIds.isEmpty()) plugin.descriptor.enabledByDefault
                else plugin.descriptor.id in enabledIds
            }
            .map { it.serviceProvider }
            .sortedBy { it.descriptor.title }

    /** Дескрипторы активных сервисов */
    fun enabledServices(enabledIds: Set<ServiceId> = emptySet()): List<ServiceDescriptor> =
        getEnabledProviders(enabledIds).map { it.descriptor }

    /** Провайдеры с заданной возможностью */
    fun getByCapability(capability: ServiceCapability): List<ServiceProvider> =
        plugins
            .filter { it.descriptor.supports(capability) }
            .map { it.serviceProvider }
            .sortedBy { it.descriptor.title }

    /** Провайдеры по категории */
    fun getByCategory(category: ServiceCategory): List<ServiceProvider> =
        plugins
            .filter { it.descriptor.category == category }
            .map { it.serviceProvider }
            .sortedBy { it.descriptor.title }
}
