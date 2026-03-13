package ru.topskiy.superapp.features.planner

import ru.topskiy.superapp.core.navigation.AppRoutes
import ru.topskiy.superapp.core.search.SearchProvider
import ru.topskiy.superapp.core.search.SearchResult
import ru.topskiy.superapp.core.services.ServiceId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlannerSearchProvider @Inject constructor() : SearchProvider {

    override val serviceId: ServiceId = PLANNER_ID

    private val stubTasks = listOf(
        "Встреча с командой",
        "Подготовить отчёт",
        "Позвонить в банк",
        "Купить продукты",
        "Записаться к врачу",
    )

    override suspend fun search(query: String): List<SearchResult> =
        stubTasks
            .filter { it.contains(query, ignoreCase = true) }
            .mapIndexed { index, task ->
                SearchResult(
                    serviceId = PLANNER_ID,
                    id = "task_$index",
                    title = task,
                    subtitle = "Задача · Планировщик",
                    route = AppRoutes.service(PLANNER_ID),
                    relevance = 1.0f,
                )
            }
}
