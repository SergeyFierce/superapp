package ru.topskiy.superapp.shell.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.topskiy.superapp.core.navigation.AppRoutes
import ru.topskiy.superapp.core.search.SearchResult

fun NavGraphBuilder.searchScreen(navController: NavController) {
    composable(AppRoutes.SEARCH) {
        val viewModel: SearchViewModel = hiltViewModel()
        SearchScreen(
            viewModel = viewModel,
            onResultClick = { result -> navController.navigate(result.route) },
        )
    }
}

/**
 * Экран глобального поиска — агрегирует результаты от всех
 * зарегистрированных SearchProvider'ов.
 */
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onResultClick: (SearchResult) -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = state.query,
            onValueChange = viewModel::onQueryChange,
            placeholder = { Text("Поиск по всем сервисам…") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "Поиск",
                )
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
        )

        when {
            state.isLoading -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    CircularProgressIndicator()
                }
            }

            state.query.isBlank() -> {
                SearchHint()
            }

            state.results.isEmpty() -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Text(
                        text = "Ничего не найдено",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            else -> {
                SearchResultList(
                    results = state.results,
                    onResultClick = onResultClick,
                )
            }
        }
    }
}

@Composable
private fun SearchHint() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = "Введите запрос",
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = "Поиск выполняется по всем подключённым сервисам",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun SearchResultList(
    results: List<SearchResult>,
    onResultClick: (SearchResult) -> Unit,
) {
    // Группировка результатов по сервису для удобного отображения
    val grouped = results.groupBy { it.serviceId }

    LazyColumn(
        contentPadding = PaddingValues(bottom = 16.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        grouped.forEach { (serviceId, serviceResults) ->
            item(key = "header_$serviceId") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                ) {
                    Text(
                        text = serviceId.value,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }

            items(
                items = serviceResults,
                key = { "${it.serviceId.value}_${it.id}" },
            ) { result ->
                ListItem(
                    headlineContent = { Text(result.title) },
                    supportingContent = result.subtitle?.let { sub ->
                        { Text(sub, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                    },
                    modifier = Modifier.clickable { onResultClick(result) },
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            }

            item(key = "spacer_$serviceId") { Spacer(modifier = Modifier.height(4.dp)) }
        }
    }
}
