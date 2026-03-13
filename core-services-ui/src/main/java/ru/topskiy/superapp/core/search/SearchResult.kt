package ru.topskiy.superapp.core.search

import ru.topskiy.superapp.core.services.ServiceId

/**
 * Единица результата поиска от любого сервиса.
 */
data class SearchResult(
    val serviceId: ServiceId,
    val id: String,
    val title: String,
    val subtitle: String? = null,
    /** Маршрут для перехода при нажатии */
    val route: String,
    val relevance: Float = 1.0f,
)

