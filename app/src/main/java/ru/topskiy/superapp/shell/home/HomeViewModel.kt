package ru.topskiy.superapp.shell.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.topskiy.superapp.core.home.HomeBlockProvider
import ru.topskiy.superapp.core.preferences.PreferencesRepository
import ru.topskiy.superapp.core.services.ServiceCapability
import ru.topskiy.superapp.core.services.ServiceRegistry
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeBlockProviders: Set<@JvmSuppressWildcards HomeBlockProvider>,
    private val serviceRegistry: ServiceRegistry,
    private val preferences: PreferencesRepository,
) : ViewModel() {

    /**
     * Реактивный список блоков для Главной.
     * Обновляется автоматически при изменении enabled-сервисов в DataStore.
     *
     * Двойной фильтр:
     * 1. Сервис должен быть включён (DataStore или enabledByDefault)
     * 2. Сервис должен иметь capability [ServiceCapability.HOME_BLOCK]
     */
    val blocks: StateFlow<List<HomeBlockProvider>> = preferences.enabledServiceIds
        .map { enabledIds ->
            homeBlockProviders
                .filter { provider ->
                    val descriptor = serviceRegistry
                        .getService(provider.serviceId)
                        ?.descriptor
                        ?: return@filter false

                    val isEnabled = if (enabledIds.isEmpty()) descriptor.enabledByDefault
                                    else descriptor.id in enabledIds

                    isEnabled && descriptor.supports(ServiceCapability.HOME_BLOCK)
                }
                .sortedBy { it.priority }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList(),
        )
}
