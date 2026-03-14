package ru.topskiy.superapp.features.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.topskiy.superapp.core.navigation.AppRoutes
import ru.topskiy.superapp.platform.api.SearchResult
import ru.topskiy.superapp.core.ui.components.AppCard
import ru.topskiy.superapp.core.ui.components.AppTextField
import ru.topskiy.superapp.core.ui.layout.Section
import ru.topskiy.superapp.core.ui.tokens.Spacing

fun NavGraphBuilder.searchScreen(navController: NavController) {
    composable(AppRoutes.SEARCH) {
        val viewModel: SearchViewModel = hiltViewModel()
        SearchScreen(
            viewModel = viewModel,
            onResultClick = { result -> navController.navigate(result.route) },
        )
    }
}

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onResultClick: (SearchResult) -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LazyColumn(
        contentPadding = PaddingValues(horizontal = Spacing.md, vertical = Spacing.lg),
        verticalArrangement = Arrangement.spacedBy(Spacing.md),
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
    ) {
        item {
            Text("Поиск", style = MaterialTheme.typography.headlineMedium)
            AppTextField(
                value = state.query,
                onValueChange = viewModel::onQueryChange,
                placeholder = "Поиск по сервисам",
                modifier = Modifier
                    .fillMaxWidth(),
            )
        }

        if (state.query.isBlank()) {
            item {
                Section(title = "Recent", subtitle = "Недавние запросы") {
                    Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                        state.recentSearches.forEach { recent ->
                            AppCard(onClick = { viewModel.onSuggestionClick(recent) }) {
                                Text(recent, style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                    }
                }
            }

            item {
                Section(title = "Suggestions", subtitle = "Попробуйте эти запросы") {
                    Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                        state.suggestions.forEach { suggestion ->
                            AppCard(onClick = { viewModel.onSuggestionClick(suggestion) }) {
                                Text(suggestion, style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                    }
                }
            }
        }

        when {
            state.isLoading -> item { CircularProgressIndicator() }
            state.results.isNotEmpty() -> {
                val grouped = state.results.groupBy { it.serviceId.value }
                grouped.forEach { (serviceId, serviceResults) ->
                    item("header_$serviceId") {
                        Text(
                            text = viewModel.serviceTitle(serviceId),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }

                    items(serviceResults, key = { "${it.serviceId.value}_${it.id}" }) { result ->
                        AppCard(onClick = { onResultClick(result) }) {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(result.title, style = MaterialTheme.typography.titleMedium)
                                    result.subtitle?.let {
                                        Text(
                                            text = it,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            state.query.isNotBlank() -> item {
                Text("Ничего не найдено", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
