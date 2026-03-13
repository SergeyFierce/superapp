package ru.topskiy.superapp.shell.bootstrap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.topskiy.superapp.core.preferences.PreferencesRepository
import javax.inject.Inject

enum class BootstrapDestination { LOADING, ONBOARDING, SHELL }

@HiltViewModel
class BootstrapViewModel @Inject constructor(
    preferences: PreferencesRepository,
) : ViewModel() {

    val destination: StateFlow<BootstrapDestination> = preferences.onboardingCompleted
        .map { completed ->
            if (completed) BootstrapDestination.SHELL else BootstrapDestination.ONBOARDING
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = BootstrapDestination.LOADING,
        )
}
