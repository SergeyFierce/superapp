package ru.topskiy.superapp.services.planner.impl

import ru.topskiy.superapp.services.planner.api.PlannerApi
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Заглушка реализации PlannerApi.
 * Функциональность будет реализована на этапе разработки сервиса.
 */
@Singleton
class PlannerApiImpl @Inject constructor() : PlannerApi {

    override suspend fun createTask(title: String) {
        // Заглушка: функциональность в разработке
    }

    override fun scheduleReminder(title: String, time: Long) {
        // Заглушка: функциональность в разработке
    }
}
