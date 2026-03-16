package ru.topskiy.superapp.core.jobs

/**
 * Маркерный интерфейс для фоновых задач приложения.
 *
 * Каждая задача — data class с необходимыми параметрами.
 * [JobScheduler] принимает [AppJob] и делегирует выполнение WorkManager.
 *
 * Для добавления нового вида задачи:
 *   1. Создать data class, реализующий AppJob
 *   2. Создать CoroutineWorker для её выполнения
 *   3. Добавить ветку в JobScheduler.schedule()
 */
interface AppJob
