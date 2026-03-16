package ru.topskiy.superapp.features.services

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.topskiy.superapp.core.datastore.PreferencesRepository
import ru.topskiy.superapp.platform.api.ServiceDescriptor
import ru.topskiy.superapp.platform.api.ServiceId
import ru.topskiy.superapp.platform.runtime.ServiceRegistry
import javax.inject.Inject

data class ServiceUiItem(
    val descriptor: ServiceDescriptor,
    val isEnabled: Boolean,
)

@HiltViewModel
class ServicesViewModel @Inject constructor(
    private val serviceRegistry: ServiceRegistry,
    private val preferences: PreferencesRepository,
) : ViewModel() {

    private val allProviders = serviceRegistry.getAllProviders()

    /**
     * Реактивный список сервисов с текущим статусом включения.
     * Обновляется при каждом изменении DataStore.
     */
    val services: StateFlow<List<ServiceUiItem>> = preferences.enabledServiceIds
        .map { enabledIds ->
            allProviders.map { provider ->
                val isEnabled = if (enabledIds.isEmpty()) {
                    provider.descriptor.enabledByDefault
                } else {
                    provider.descriptor.id in enabledIds
                }
                ServiceUiItem(provider.descriptor, isEnabled)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = allProviders.map { ServiceUiItem(it.descriptor, it.descriptor.enabledByDefault) },
        )

    fun toggleService(serviceId: ServiceId, enabled: Boolean) {
        viewModelScope.launch {
            preferences.toggleService(
                serviceId = serviceId,
                enabled = enabled,
                allServiceIds = allProviders.map { it.descriptor.id }.toSet(),
            )
        }
    }
}
