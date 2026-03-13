package ru.topskiy.superapp.core.events

import ru.topskiy.superapp.core.services.ServiceId

/**
 * Системные cross-service события.
 *
 * Здесь живут события, которые один сервис может отправить,
 * а другой — получить, без прямой зависимости между ними.
 */

/** Создан новый элемент задачи/события в сервисе-планировщике */
data class TaskCreatedEvent(
    val source: ServiceId,
    val taskTitle: String,
) : AppEvent

/** Пользователь открыл конкретный сервис */
data class ServiceOpenedEvent(
    val serviceId: ServiceId,
) : AppEvent
