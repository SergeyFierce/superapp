package ru.topskiy.superapp.shell.services

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.topskiy.superapp.core.navigation.AppRoutes
import ru.topskiy.superapp.core.services.ServiceRegistry

fun NavGraphBuilder.servicesScreen(
    navController: NavController,
    serviceRegistry: ServiceRegistry,
) {
    composable(AppRoutes.SERVICES) {
        val viewModel: ServicesViewModel = hiltViewModel()
        ServicesScreen(
            viewModel = viewModel,
            onServiceClick = { item ->
                if (item.isEnabled) navController.navigate(item.descriptor.rootRoute)
            },
        )
    }
}

/**
 * Экран "Сервисы" — каталог с возможностью включения/отключения.
 * Список строится из ServiceRegistry, состояние — из DataStore.
 */
@Composable
fun ServicesScreen(
    viewModel: ServicesViewModel = hiltViewModel(),
    onServiceClick: (ServiceUiItem) -> Unit = {},
) {
    val services by viewModel.services.collectAsStateWithLifecycle()

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            Text(
                text = "Сервисы",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }

        if (services.isEmpty()) {
            item {
                Text(
                    text = "Нет подключённых сервисов",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else {
            items(
                items = services,
                key = { it.descriptor.id.value },
            ) { item ->
                ServiceCard(
                    item = item,
                    onClick = { onServiceClick(item) },
                    onToggle = { enabled ->
                        viewModel.toggleService(item.descriptor.id, enabled)
                    },
                )
            }
        }
    }
}

@Composable
private fun ServiceCard(
    item: ServiceUiItem,
    onClick: () -> Unit,
    onToggle: (Boolean) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = item.isEnabled, onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (item.isEnabled) {
                MaterialTheme.colorScheme.surfaceVariant
            } else {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            },
        ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp),
        ) {
            Icon(
                imageVector = item.descriptor.icon as ImageVector,
                contentDescription = item.descriptor.title,
                modifier = Modifier.size(32.dp),
                tint = if (item.isEnabled) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                },
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.descriptor.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (item.isEnabled) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    },
                )
                if (item.descriptor.description.isNotBlank()) {
                    Text(
                        text = item.descriptor.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Text(
                    text = item.descriptor.category.title,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.tertiary,
                )
            }
            Switch(
                checked = item.isEnabled,
                onCheckedChange = onToggle,
            )
        }
    }
}
