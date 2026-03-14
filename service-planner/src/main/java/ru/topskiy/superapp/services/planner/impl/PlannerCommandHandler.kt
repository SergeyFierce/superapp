package ru.topskiy.superapp.services.planner.impl

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch
import ru.topskiy.superapp.platform.commands.CommandBus
import ru.topskiy.superapp.platform.commands.CreateTaskCommand
import ru.topskiy.superapp.core.di.ApplicationScope
import ru.topskiy.superapp.services.planner.api.PlannerApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlannerCommandHandler @Inject constructor(
    commandBus: CommandBus,
    private val plannerApi: PlannerApi,
    @ApplicationScope private val scope: CoroutineScope,
) {
    init {
        scope.launch {
            commandBus.commands()
                .filterIsInstance<CreateTaskCommand>()
                .collect { command ->
                    plannerApi.createTask(command.title)
                }
        }
    }
}
