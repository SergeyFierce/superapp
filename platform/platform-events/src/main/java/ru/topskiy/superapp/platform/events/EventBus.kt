package ru.topskiy.superapp.platform.events

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Шина событий приложения.
 *
 * Позволяет сервисам взаимодействовать через события,
 * не имея прямых зависимостей друг от друга.
 */
class EventBus {

    private val _events = MutableSharedFlow<AppEvent>(
        extraBufferCapacity = 64,
    )

    fun publish(event: AppEvent) {
        _events.tryEmit(event)
    }

    fun events(): Flow<AppEvent> = _events.asSharedFlow()
}
