package ru.topskiy.superapp.core.commands

/**
 * Системные cross-service команды.
 *
 * Сервис-отправитель знает только об этом файле — не о реализации получателя.
 */

/** Команда создать задачу. Обрабатывается Планировщиком. */
data class CreateTaskCommand(
    val title: String,
) : AppCommand

/** Команда открыть сервис. Обрабатывается системной оболочкой. */
data class OpenServiceCommand(
    val serviceId: String,
) : AppCommand
