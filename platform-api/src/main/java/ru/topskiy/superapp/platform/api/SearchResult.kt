package ru.topskiy.superapp.platform.api

data class SearchResult(
    val serviceId: ServiceId,
    val id: String,
    val title: String,
    val subtitle: String? = null,
    val route: String,
    val relevance: Float = 1.0f,
)
