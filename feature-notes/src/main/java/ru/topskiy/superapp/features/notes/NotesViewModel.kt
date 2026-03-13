package ru.topskiy.superapp.features.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.topskiy.superapp.core.events.EventBus
import ru.topskiy.superapp.core.events.TaskCreatedEvent
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val eventBus: EventBus,
) : ViewModel() {

    private val _incomingTasks = MutableStateFlow<List<String>>(emptyList())
    val incomingTasks: StateFlow<List<String>> = _incomingTasks.asStateFlow()

    init {
        viewModelScope.launch {
            eventBus.events()
                .filterIsInstance<TaskCreatedEvent>()
                .collect { event ->
                    _incomingTasks.update { current ->
                        (listOf("📋 ${event.taskTitle} (из: ${event.source})") + current).take(5)
                    }
                }
        }
    }
}
