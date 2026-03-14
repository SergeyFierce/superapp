package ru.topskiy.superapp.platform.commands

/** Команда создать задачу. Обрабатывается Планировщиком. */
data class CreateTaskCommand(
    val title: String,
) : AppCommand

/** Команда открыть сервис. Обрабатывается системной оболочкой. */
data class OpenServiceCommand(
    val serviceId: String,
) : AppCommand
