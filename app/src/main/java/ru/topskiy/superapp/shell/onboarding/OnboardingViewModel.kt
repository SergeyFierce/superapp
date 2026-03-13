package ru.topskiy.superapp.shell.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.topskiy.superapp.core.preferences.PreferencesRepository
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val preferences: PreferencesRepository,
) : ViewModel() {

    fun completeOnboarding() {
        viewModelScope.launch { preferences.setOnboardingCompleted() }
    }
}
