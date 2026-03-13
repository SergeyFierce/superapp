package ru.topskiy.superapp.features.finance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.topskiy.superapp.core.commands.CommandBus
import ru.topskiy.superapp.core.commands.CreateTaskCommand
import ru.topskiy.superapp.core.navigation.ServiceAction
import ru.topskiy.superapp.core.navigation.ServiceNavigator
import ru.topskiy.superapp.core.services.ServiceId
import javax.inject.Inject

/**
 * В multi-module: feature-модули не зависят друг от друга.
 *
 * Каналы межсервисного взаимодействия:
 *   1. CommandBus — dispatch(CreateTaskCommand) → обработчик в Планировщике
 *   2. ServiceNavigator — OpenService(ServiceId) → навигация по ID
 */
@HiltViewModel
class FinanceViewModel @Inject constructor(
    private val serviceNavigator: ServiceNavigator,
    private val commandBus: CommandBus,
) : ViewModel() {

    fun dispatchCreateTask(title: String) {
        viewModelScope.launch {
            commandBus.dispatch(CreateTaskCommand(title))
        }
    }

    fun openPlanner() {
        serviceNavigator.execute(ServiceAction.OpenService(ServiceId("planner")))
    }

    fun goBack() {
        serviceNavigator.execute(ServiceAction.GoBack)
    }
}
