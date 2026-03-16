package ru.topskiy.superapp.services.planner.api

/**
 * Публичный API Планировщика для использования другими сервисами.
 *
 * В multi-module: feature-модули не зависят друг от друга.
 * Взаимодействие только через CommandBus, EventBus, ServiceNavigator.
 */
interface PlannerApi {

    /**
     * Создать задачу.
     * Публикует [TaskCreatedEvent] через EventBus.
     */
    suspend fun createTask(title: String)

    /**
     * Запланировать напоминание на заданное время.
     */
    fun scheduleReminder(title: String, time: Long)
}
