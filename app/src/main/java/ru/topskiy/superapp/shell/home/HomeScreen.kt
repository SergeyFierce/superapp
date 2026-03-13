package ru.topskiy.superapp.shell.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
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

fun NavGraphBuilder.homeScreen(navController: NavController) {
    composable(AppRoutes.HOME) {
        val viewModel: HomeViewModel = hiltViewModel()
        HomeScreen(viewModel = viewModel)
    }
}

/**
 * Экран "Главная" — системный агрегатор блоков.
 * Реагирует на изменения enabled-сервисов через StateFlow.
 */
@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val blocks by viewModel.blocks.collectAsStateWithLifecycle()

    if (blocks.isEmpty()) {
        HomeEmptyState()
    } else {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                    Text(
                        text = "Главная",
                        style = MaterialTheme.typography.headlineMedium,
                    )
                }
            }

            items(
                items = blocks,
                key = { it.serviceId.value },
            ) { block ->
                Box(modifier = Modifier.fillMaxWidth()) {
                    block.Content()
                }
            }
        }
    }
}

@Composable
private fun HomeEmptyState() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "Главная",
                style = MaterialTheme.typography.headlineMedium,
            )
            Text(
                text = "Все сервисы отключены или блоки не настроены",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
