package ru.topskiy.superapp.platform.runtime

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ru.topskiy.superapp.core.datastore.PreferencesRepository
import ru.topskiy.superapp.platform.api.HomeBlockProvider
import ru.topskiy.superapp.platform.api.SearchProvider
import ru.topskiy.superapp.platform.api.ServiceDescriptor
import ru.topskiy.superapp.platform.api.ServiceId
import ru.topskiy.superapp.platform.api.ServicePlugin
import ru.topskiy.superapp.platform.api.ServiceProvider
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Публичные контракты рантайма платформы.
 *
 * Специально остаются в пакете platform.runtime, чтобы не ломать
 * существующие импорты (ServiceRegistry, ServiceManager).
 */
interface ServiceRegistry {
    /** Все зарегистрированные плагины. */
    val plugins: Set<ServicePlugin>

    /** Все дескрипторы сервисов (по одному на плагин). */
    val descriptors: List<ServiceDescriptor>

    /** Все провайдеры сервисов. */
    val providers: List<ServiceProvider>

    /** Найти дескриптор по идентификатору. */
    fun findDescriptor(id: ServiceId): ServiceDescriptor?

    /** Найти провайдера по идентификатору. */
    fun findProvider(id: ServiceId): ServiceProvider?

    /** Удобный алиас для старого API: получить все провайдеры. */
    fun getAllProviders(): List<ServiceProvider> = providers

    /** Удобный алиас для старого API: получить провайдера по id. */
    fun getService(id: ServiceId): ServiceProvider? = findProvider(id)
}

/**
 * Менеджер сервисов учитывает пользовательские настройки (enabled/disabled)
 * и предоставляет удобные потоки и методы для оболочки.
 */
interface ServiceManager {
    /** Реестр всех известных сервисов. */
    val registry: ServiceRegistry

    /** Поток включённых сервисов с учётом настроек пользователя. */
    val enabledServiceDescriptors: Flow<List<ServiceDescriptor>>

    /** Поток включённых идентификаторов сервисов. */
    val enabledServiceIds: Flow<Set<ServiceId>>

    /**
     * Изменить состояние сервиса (включён/выключен).
     *
     * Логика "не выключать последний сервис" инкапсулирована
     * внутри [PreferencesRepository.toggleService].
     */
    suspend fun setServiceEnabled(id: ServiceId, enabled: Boolean)

    /**
     * Список SearchProvider только для тех сервисов, которые включены
     * и реально поддерживают поиск.
     */
    suspend fun getEnabledSearchProviders(): List<SearchProvider>

    /**
     * Список HomeBlockProvider только для включённых сервисов.
     */
    suspend fun getEnabledHomeBlocks(): List<HomeBlockProvider>
}

/**
 * Базовая реализация [ServiceRegistry], собирающая данные из множества [ServicePlugin].
 */
@Singleton
class DefaultServiceRegistry @Inject constructor(
    override val plugins: Set<@JvmSuppressWildcards ServicePlugin>,
) : ServiceRegistry {

    private val descriptorById: Map<ServiceId, ServiceDescriptor> =
        plugins.associate { it.descriptor.id to it.descriptor }

    private val providerById: Map<ServiceId, ServiceProvider> =
        plugins.associate { it.descriptor.id to it.serviceProvider }

    override val descriptors: List<ServiceDescriptor>
        get() = descriptorById.values.toList()

    override val providers: List<ServiceProvider>
        get() = providerById.values.toList()

    override fun findDescriptor(id: ServiceId): ServiceDescriptor? = descriptorById[id]

    override fun findProvider(id: ServiceId): ServiceProvider? = providerById[id]
}

/**
 * Реализация [ServiceManager], оборачивающая [ServiceRegistry] и [PreferencesRepository].
 */
@Singleton
class DefaultServiceManager @Inject constructor(
    override val registry: ServiceRegistry,
    private val preferencesRepository: PreferencesRepository,
    private val homeBlockProviders: Set<@JvmSuppressWildcards HomeBlockProvider>,
    private val searchProviders: Set<@JvmSuppressWildcards SearchProvider>,
) : ServiceManager {

    override val enabledServiceIds: Flow<Set<ServiceId>> =
        preferencesRepository.enabledServiceIds.map { stored ->
            // Если настроек ещё нет, считаем включёнными все сервисы,
            // которые по умолчанию enabledByDefault = true.
            if (stored.isEmpty()) {
                registry.descriptors
                    .filter { it.enabledByDefault }
                    .map { it.id }
                    .toSet()
            } else {
                stored
            }
        }

    override val enabledServiceDescriptors: Flow<List<ServiceDescriptor>> =
        enabledServiceIds.map { enabledIds ->
            registry.descriptors.filter { it.id in enabledIds }
        }

    override suspend fun setServiceEnabled(id: ServiceId, enabled: Boolean) {
        val allIds = registry.descriptors.map { it.id }.toSet()
        preferencesRepository.toggleService(
            serviceId = id,
            enabled = enabled,
            allServiceIds = allIds,
        )
    }

    override suspend fun getEnabledSearchProviders(): List<SearchProvider> {
        val enabled = enabledServiceIds.first()
        return searchProviders.filter { it.serviceId in enabled }
    }

    override suspend fun getEnabledHomeBlocks(): List<HomeBlockProvider> {
        val enabled = enabledServiceIds.first()
        return homeBlockProviders
            .filter { it.serviceId in enabled }
            .sortedBy { it.priority }
    }
}

