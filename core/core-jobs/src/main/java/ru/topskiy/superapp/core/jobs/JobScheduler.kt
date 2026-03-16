package ru.topskiy.superapp.core.jobs

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Планировщик фоновых задач.
 *
 * Единая точка входа для запуска любой [AppJob] через WorkManager.
 * Сервисы не работают с WorkManager напрямую — только через этот класс.
 *
 * Для добавления нового вида задачи: добавить ветку в [schedule].
 */
@Singleton
class JobScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val workManager: WorkManager get() = WorkManager.getInstance(context)

    /**
     * Поставить задачу в очередь.
     * Выполнение откладывается до наступления [AppJob]-специфичного времени.
     */
    fun schedule(job: AppJob) {
        when (job) {
            is ReminderJob -> scheduleReminder(job)
            else -> Unit // новые типы добавлять здесь
        }
    }

    /** Отменить все задачи с данным тегом */
    fun cancel(tag: String) {
        workManager.cancelAllWorkByTag(tag)
    }

    // ── Приватные планировщики ──────────────────────────────────────────

    private fun scheduleReminder(job: ReminderJob) {
        val delayMs = (job.time - System.currentTimeMillis()).coerceAtLeast(0L)

        val inputData = workDataOf(
            ReminderWorker.KEY_TITLE to job.title,
        )

        val request = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delayMs, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .addTag(job.tag)
            .build()

        workManager.enqueue(request)
    }
}
