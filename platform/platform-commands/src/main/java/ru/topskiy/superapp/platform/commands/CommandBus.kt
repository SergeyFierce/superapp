package ru.topskiy.superapp.platform.commands

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Глобальная шина команд приложения.
 *
 * Сервис-отправитель: вызывает [dispatch] — не знает, кто обработает.
 * Сервис-получатель: подписывается на [commands] с filterIsInstance<T>().
 */
class CommandBus {

    private val _commands = MutableSharedFlow<AppCommand>(
        extraBufferCapacity = 64,
    )

    suspend fun dispatch(command: AppCommand) {
        _commands.emit(command)
    }

    fun commands(): Flow<AppCommand> = _commands.asSharedFlow()
}
