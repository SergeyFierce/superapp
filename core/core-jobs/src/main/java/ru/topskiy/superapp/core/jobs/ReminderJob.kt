package ru.topskiy.superapp.core.jobs

/**
 * Задача — показать напоминание в заданное время.
 *
 * @param title   текст напоминания
 * @param time    время срабатывания в миллисекундах (System.currentTimeMillis())
 * @param tag     уникальный тег для отмены задачи через WorkManager
 */
data class ReminderJob(
    val title: String,
    val time: Long,
    val tag: String = "reminder_$title",
) : AppJob
