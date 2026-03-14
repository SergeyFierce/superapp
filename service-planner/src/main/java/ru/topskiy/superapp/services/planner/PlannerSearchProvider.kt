package ru.topskiy.superapp.services.planner

import ru.topskiy.superapp.platform.api.SearchProvider
import ru.topskiy.superapp.platform.api.ServiceId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlannerSearchProvider @Inject constructor() : SearchProvider {

    override val serviceId: ServiceId = PLANNER_ID

    override suspend fun search(query: String): List<SearchResult> = emptyList()
}
