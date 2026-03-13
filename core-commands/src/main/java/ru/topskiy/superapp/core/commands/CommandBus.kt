package ru.topskiy.superapp.core.commands

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Глобальная шина команд приложения.
 *
 * Сервис-отправитель: вызывает [dispatch] — не знает, кто обработает.
 * Сервис-получатель: подписывается на [commands] с filterIsInstance<T>().
 *
 * Сравнение с [EventBus]:
 *   EventBus  — уведомление о свершившемся факте, подписчиков может быть 0..N
 *   CommandBus — запрос на выполнение действия, ожидается ровно 1 обработчик
 *
 * Важно: подписчик должен быть жив в момент dispatch.
 * Для фоновой обработки используй Singleton-обработчик (не ViewModel).
 */
class CommandBus {

    private val _commands = MutableSharedFlow<AppCommand>(
        extraBufferCapacity = 64,
    )

    /** Отправить команду. Suspend — гарантирует доставку при наличии подписчика. */
    suspend fun dispatch(command: AppCommand) {
        _commands.emit(command)
    }

    /** Поток команд. Фильтруй нужный тип: filterIsInstance<CreateTaskCommand>(). */
    fun commands(): Flow<AppCommand> = _commands.asSharedFlow()
}
