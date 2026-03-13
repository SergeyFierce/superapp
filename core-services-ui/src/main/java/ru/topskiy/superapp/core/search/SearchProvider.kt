package ru.topskiy.superapp.core.search

import ru.topskiy.superapp.core.services.ServiceId

/**
 * Контракт участия сервиса в глобальном поиске.
 * Дополняет [ServiceCapability.SEARCH_PROVIDER] в дескрипторе сервиса.
 */
interface SearchProvider {

    val serviceId: ServiceId

    /**
     * Выполняет поиск по [query] в локальных данных сервиса.
     * Вызывается в фоновом потоке.
     */
    suspend fun search(query: String): List<SearchResult>
}

