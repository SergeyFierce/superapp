package ru.topskiy.superapp.shell

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import ru.topskiy.superapp.core.common.AppTheme
import ru.topskiy.superapp.core.datastore.PreferencesRepository
import javax.inject.Inject

/**
 * ViewModel для получения текущей темы.
 * Используется в MainActivity для передачи в AppTheme.
 */
@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val preferences: PreferencesRepository,
) : ViewModel() {

    val theme: StateFlow<AppTheme> = preferences.theme.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AppTheme.SYSTEM,
    )
}
