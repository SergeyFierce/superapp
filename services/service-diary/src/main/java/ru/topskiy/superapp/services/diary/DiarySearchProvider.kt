package ru.topskiy.superapp.services.diary

import ru.topskiy.superapp.platform.api.SearchProvider
import ru.topskiy.superapp.platform.api.SearchResult
import ru.topskiy.superapp.platform.api.ServiceId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiarySearchProvider @Inject constructor() : SearchProvider {

    override val serviceId: ServiceId = DIARY_ID

    override suspend fun search(query: String): List<SearchResult> = emptyList()
}
