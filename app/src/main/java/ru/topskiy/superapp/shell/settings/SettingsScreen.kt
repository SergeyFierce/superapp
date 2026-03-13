package ru.topskiy.superapp.shell.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.topskiy.superapp.core.navigation.AppRoutes
import ru.topskiy.superapp.shell.AppShellState
import ru.topskiy.superapp.core.common.AppTheme

fun NavGraphBuilder.settingsScreen(
    shellState: AppShellState,
    onThemeChange: (AppTheme) -> Unit,
) {
    composable(AppRoutes.SETTINGS) {
        SettingsScreen(
            shellState = shellState,
            onThemeChange = onThemeChange,
        )
    }
}

@Composable
fun SettingsScreen(
    shellState: AppShellState,
    onThemeChange: (AppTheme) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Text(
            text = "Настройки",
            style = MaterialTheme.typography.headlineMedium,
        )

        AppearanceSection(
            currentTheme = shellState.theme,
            onThemeChange = onThemeChange,
        )
    }
}

@Composable
private fun AppearanceSection(
    currentTheme: AppTheme,
    onThemeChange: (AppTheme) -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Внешний вид",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 12.dp),
            )
            HorizontalDivider()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
            ) {
                Text(
                    text = "Тема",
                    style = MaterialTheme.typography.bodyMedium,
                )
                SingleChoiceSegmentedButtonRow {
                    AppTheme.entries.forEachIndexed { index, theme ->
                        SegmentedButton(
                            selected = theme == currentTheme,
                            onClick = { onThemeChange(theme) },
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = AppTheme.entries.size,
                            ),
                            label = { Text(theme.title) },
                        )
                    }
                }
            }
        }
    }
}
