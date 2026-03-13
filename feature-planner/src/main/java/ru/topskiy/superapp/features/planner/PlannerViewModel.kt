package ru.topskiy.superapp.features.planner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.topskiy.superapp.features.planner.api.PlannerApi
import javax.inject.Inject

@HiltViewModel
class PlannerViewModel @Inject constructor(
    private val plannerApi: PlannerApi,
) : ViewModel() {

    fun createTask(title: String) {
        viewModelScope.launch { plannerApi.createTask(title) }
    }
}
