package ru.topskiy.superapp.features.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.topskiy.superapp.core.common.AppTheme
import ru.topskiy.superapp.core.navigation.AppRoutes
import ru.topskiy.superapp.core.ui.components.AppCard
import ru.topskiy.superapp.core.ui.tokens.Spacing

fun NavGraphBuilder.settingsScreen(
    theme: AppTheme,
    onThemeChange: (AppTheme) -> Unit,
) {
    composable(AppRoutes.SETTINGS) {
        SettingsScreen(theme = theme, onThemeChange = onThemeChange)
    }
}

@Composable
fun SettingsScreen(
    theme: AppTheme,
    onThemeChange: (AppTheme) -> Unit,
) {
    val sections = listOf("Appearance", "Services", "Notifications", "About")

    LazyColumn(
        contentPadding = PaddingValues(horizontal = Spacing.md, vertical = Spacing.lg),
        verticalArrangement = Arrangement.spacedBy(Spacing.md),
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
    ) {
        item { Text("Настройки", style = MaterialTheme.typography.headlineMedium) }

        item {
            AppCard(modifier = Modifier.fillMaxWidth()) {
                Text("Appearance", style = MaterialTheme.typography.titleMedium)
                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    AppTheme.entries.forEachIndexed { index, t ->
                        SegmentedButton(
                            selected = t == theme,
                            onClick = { onThemeChange(t) },
                            shape = SegmentedButtonDefaults.itemShape(index = index, count = AppTheme.entries.size),
                            label = { Text(t.title) },
                        )
                    }
                }
            }
        }

        items(sections.drop(1)) { section ->
            AppCard(modifier = Modifier.fillMaxWidth()) {
                Text(section, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "Раздел в процессе обновления UX",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
