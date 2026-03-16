package ru.topskiy.superapp.core.jobs

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * WorkManager Worker для напоминаний с Hilt-инжекцией.
 *
 * @HiltWorker + @AssistedInject позволяют инжектировать любые
 * Hilt-зависимости в поле класса, пока Context и WorkerParameters
 * передаются через @Assisted.
 *
 * Для добавления зависимости: просто добавить @Inject-поле в класс.
 */
@HiltWorker
class ReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
) : CoroutineWorker(context, params) {

    // Пример инжекции зависимости через Hilt (раскомментировать при необходимости):
    // @Inject lateinit var eventBus: EventBus

    override suspend fun doWork(): Result {
        val title = inputData.getString(KEY_TITLE)
            ?: return Result.failure()

        Log.i(TAG, "Напоминание: $title")

        // TODO: показать системное уведомление
        // NotificationHelper.show(applicationContext, title)

        return Result.success()
    }

    companion object {
        const val KEY_TITLE = "key_title"
        private const val TAG = "ReminderWorker"
    }
}
