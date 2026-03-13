package ru.topskiy.superapp.shell.services

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.topskiy.superapp.core.navigation.AppRoutes
import ru.topskiy.superapp.core.services.ServiceRegistry
import ru.topskiy.superapp.core.services.ui.asImageVector
import ru.topskiy.superapp.core.ui.components.AppCard
import ru.topskiy.superapp.core.ui.tokens.Spacing

fun NavGraphBuilder.servicesScreen(
    navController: NavController,
    serviceRegistry: ServiceRegistry,
) {
    composable(AppRoutes.SERVICES) {
        val viewModel: ServicesViewModel = hiltViewModel()
        ServicesScreen(
            viewModel = viewModel,
            onServiceClick = { item -> if (item.isEnabled) navController.navigate(item.descriptor.rootRoute) },
        )
    }
}

@Composable
fun ServicesScreen(
    viewModel: ServicesViewModel = hiltViewModel(),
    onServiceClick: (ServiceUiItem) -> Unit = {},
) {
    val services by viewModel.services.collectAsStateWithLifecycle()

    LazyColumn(
        contentPadding = PaddingValues(horizontal = Spacing.md, vertical = Spacing.lg),
        verticalArrangement = Arrangement.spacedBy(Spacing.sm),
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
    ) {
        item {
            Text("Сервисы", style = MaterialTheme.typography.headlineMedium)
            Text(
                text = "Каталог подключённых модулей",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        items(services, key = { it.descriptor.id.value }) { item ->
            AppCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = { if (item.isEnabled) onServiceClick(item) },
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    Icon(
                        imageVector = item.descriptor.iconKey.asImageVector(),
                        contentDescription = item.descriptor.title,
                        tint = if (item.isEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(22.dp),
                    )
                    androidx.compose.foundation.layout.Column(modifier = Modifier.weight(1f)) {
                        Text(item.descriptor.title, style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = item.descriptor.description.ifBlank { "Открыть сервис" },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowForwardIos,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}
