package ru.topskiy.superapp.core.ui.layout

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.topskiy.superapp.core.ui.tokens.Spacing

/**
 * Контейнер для элемента списка с разделителем снизу.
 */
@Composable
fun ListItemContainer(
    modifier: Modifier = Modifier,
    showDivider: Boolean = true,
    content: @Composable () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.sm, horizontal = Spacing.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        content()
    }
    if (showDivider) {
        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant,
            modifier = Modifier.padding(horizontal = Spacing.md),
        )
    }
}
