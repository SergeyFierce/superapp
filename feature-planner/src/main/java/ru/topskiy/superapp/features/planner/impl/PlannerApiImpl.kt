package ru.topskiy.superapp.features.planner.impl

import ru.topskiy.superapp.core.events.EventBus
import ru.topskiy.superapp.core.events.TaskCreatedEvent
import ru.topskiy.superapp.core.jobs.JobScheduler
import ru.topskiy.superapp.core.jobs.ReminderJob
import ru.topskiy.superapp.features.planner.PLANNER_ID
import ru.topskiy.superapp.features.planner.api.PlannerApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlannerApiImpl @Inject constructor(
    private val eventBus: EventBus,
    private val jobScheduler: JobScheduler,
) : PlannerApi {

    override suspend fun createTask(title: String) {
        eventBus.publish(
            TaskCreatedEvent(
                source = PLANNER_ID,
                taskTitle = title,
            )
        )
    }

    override fun scheduleReminder(title: String, time: Long) {
        jobScheduler.schedule(
            ReminderJob(
                title = title,
                time = time,
                tag = "reminder_planner_${title.hashCode()}",
            )
        )
    }
}
