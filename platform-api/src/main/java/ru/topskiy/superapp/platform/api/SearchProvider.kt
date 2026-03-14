package ru.topskiy.superapp.platform.api

interface SearchProvider {
    val serviceId: ServiceId
    suspend fun search(query: String): List<SearchResult>
}
