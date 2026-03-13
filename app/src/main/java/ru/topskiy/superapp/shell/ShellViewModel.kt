package ru.topskiy.superapp.shell

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.topskiy.superapp.core.common.AppTheme
import ru.topskiy.superapp.core.preferences.PreferencesRepository
import ru.topskiy.superapp.core.services.ServiceId
import javax.inject.Inject

/**
 * Системное состояние оболочки приложения.
 * Является проекцией [PreferencesRepository] — нет своего изменяемого MutableStateFlow.
 */
data class AppShellState(
    val onboardingCompleted: Boolean = true,
    val theme: AppTheme = AppTheme.SYSTEM,
    /** Пустое множество = используются enabledByDefault из ServiceDescriptor */
    val enabledServiceIds: Set<ServiceId> = emptySet(),
    val pinnedServiceIds: List<ServiceId> = emptyList(),
)

@HiltViewModel
class ShellViewModel @Inject constructor(
    private val preferences: PreferencesRepository,
) : ViewModel() {

    /**
     * Реактивное состояние оболочки — автоматически обновляется
     * при изменении DataStore из любой точки приложения.
     */
    val state: StateFlow<AppShellState> = combine(
        preferences.theme,
        preferences.enabledServiceIds,
        preferences.onboardingCompleted,
    ) { theme, enabledIds, onboarding ->
        AppShellState(
            onboardingCompleted = onboarding,
            theme = theme,
            enabledServiceIds = enabledIds,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AppShellState(),
    )

    fun setTheme(theme: AppTheme) {
        viewModelScope.launch { preferences.setTheme(theme) }
    }

    fun completeOnboarding() {
        viewModelScope.launch { preferences.setOnboardingCompleted() }
    }
}
