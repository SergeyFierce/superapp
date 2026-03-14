package ru.topskiy.superapp.platform.events

import ru.topskiy.superapp.platform.api.ServiceId

/** Создан новый элемент задачи/события в сервисе-планировщике */
data class TaskCreatedEvent(
    val source: ServiceId,
    val taskTitle: String,
) : AppEvent

/** Пользователь открыл конкретный сервис */
data class ServiceOpenedEvent(
    val serviceId: ServiceId,
) : AppEvent
