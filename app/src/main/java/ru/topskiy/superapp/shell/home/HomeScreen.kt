package ru.topskiy.superapp.shell.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
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
import ru.topskiy.superapp.core.ui.layout.Section
import ru.topskiy.superapp.core.ui.tokens.Spacing

fun NavGraphBuilder.homeScreen(navController: NavController) {
    composable(AppRoutes.HOME) {
        val viewModel: HomeViewModel = hiltViewModel()
        HomeScreen(viewModel = viewModel, onRouteOpen = navController::navigate)
    }
}

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onRouteOpen: (String) -> Unit = {},
) {
    val blocks by viewModel.blocks.collectAsStateWithLifecycle()
    val quickActions = blocks.take(1)
    val services = blocks.drop(1)

    LazyColumn(
        contentPadding = PaddingValues(horizontal = Spacing.md, vertical = Spacing.lg),
        verticalArrangement = Arrangement.spacedBy(Spacing.lg),
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
    ) {
        item {
            Text("Dashboard", style = MaterialTheme.typography.headlineMedium)
            Text(
                text = "Управляйте сервисами и быстрыми сценариями",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        item {
            Section(title = "Быстрые действия", subtitle = "Частые задачи") {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    quickActions.forEach { block ->
                        val route = viewModel.routeFor(block.serviceId)
                        block.Content(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(enabled = route != null) { route?.let(onRouteOpen) },
                        )
                    }
                }
            }
        }

        item {
            Section(title = "Ваши сервисы", subtitle = "Открывайте модули одним касанием") {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    services.forEach { block ->
                        val route = viewModel.routeFor(block.serviceId)
                        block.Content(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(enabled = route != null) { route?.let(onRouteOpen) },
                        )
                    }
                }
            }
        }

        item {
            Section(title = "Актуальное", subtitle = "Что важно сейчас") {
                Text(
                    text = "Проверьте финансы, завершите задачи в Planner и зафиксируйте идеи в Notes.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
