package ru.topskiy.superapp.shell.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.topskiy.superapp.core.preferences.PreferencesRepository
import ru.topskiy.superapp.core.search.SearchProvider
import ru.topskiy.superapp.core.search.SearchResult
import ru.topskiy.superapp.core.services.ServiceCapability
import ru.topskiy.superapp.core.services.ServiceRegistry
import javax.inject.Inject

data class SearchUiState(
    val query: String = "",
    val results: List<SearchResult> = emptyList(),
    val isLoading: Boolean = false,
    val recentSearches: List<String> = listOf("Платежи", "Заметки", "Сегодня"),
    val suggestions: List<String> = listOf("Создать задачу", "Последние транзакции", "Идеи"),
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchProviders: Set<@JvmSuppressWildcards SearchProvider>,
    private val serviceRegistry: ServiceRegistry,
    private val preferences: PreferencesRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(SearchUiState())
    val state: StateFlow<SearchUiState> = _state.asStateFlow()

    private val queryFlow = MutableStateFlow("")

    init {
        observeQuery()
    }

    fun onQueryChange(query: String) {
        _state.update { it.copy(query = query) }
        queryFlow.value = query
    }

    fun onSuggestionClick(value: String) = onQueryChange(value)

    fun serviceTitle(serviceId: String): String =
        serviceRegistry.getAllProviders().firstOrNull { it.descriptor.id.value == serviceId }?.descriptor?.title
            ?: serviceId

    @OptIn(FlowPreview::class)
    private fun observeQuery() {
        viewModelScope.launch {
            queryFlow
                .debounce(300)
                .distinctUntilChanged()
                .collect { query -> performSearch(query) }
        }
    }

    private suspend fun performSearch(query: String) {
        if (query.isBlank()) {
            _state.update { it.copy(results = emptyList(), isLoading = false) }
            return
        }

        _state.update { it.copy(isLoading = true) }
        val enabledIds = preferences.enabledServiceIds.first()

        val activeProviders = searchProviders.filter { provider ->
            val descriptor = serviceRegistry.getService(provider.serviceId)?.descriptor ?: return@filter false
            val isEnabled = if (enabledIds.isEmpty()) descriptor.enabledByDefault else descriptor.id in enabledIds
            isEnabled && descriptor.supports(ServiceCapability.SEARCH_PROVIDER)
        }

        val allResults = activeProviders.flatMap { provider ->
            runCatching { provider.search(query) }.getOrDefault(emptyList())
        }.sortedByDescending { it.relevance }

        _state.update {
            it.copy(
                results = allResults,
                isLoading = false,
                recentSearches = (listOf(query) + it.recentSearches).distinct().take(5),
            )
        }
    }
}
