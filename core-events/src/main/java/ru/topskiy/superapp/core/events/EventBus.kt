package ru.topskiy.superapp.core.events

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Шина событий приложения.
 *
 * Позволяет сервисам взаимодействовать через события,
 * не имея прямых зависимостей друг от друга.
 *
 * Публикация: [publish] — fire-and-forget, не блокирует поток.
 * Подписка: [events] — cold Flow, фильтруй через filterIsInstance<T>().
 */
class EventBus {

    private val _events = MutableSharedFlow<AppEvent>(
        extraBufferCapacity = 64,
    )

    /** Опубликовать событие. Не требует suspend-контекста. */
    fun publish(event: AppEvent) {
        _events.tryEmit(event)
    }

    /** Поток всех событий. Подписчик получает только события, опубликованные после подписки. */
    fun events(): Flow<AppEvent> = _events.asSharedFlow()
}
