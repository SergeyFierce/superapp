package ru.topskiy.superapp.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.topskiy.superapp.core.ui.tokens.Radius
import ru.topskiy.superapp.core.ui.tokens.Spacing

/**
 * Карточка приложения.
 *
 * Использует surface цвет, Radius.md, Spacing.md.
 */
@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    if (onClick != null) {
        Card(
            modifier = modifier,
            onClick = onClick,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
            shape = Radius.shapeMd,
            elevation = CardDefaults.cardElevation(
                defaultElevation = 0.dp,
                pressedElevation = 2.dp,
            ),
            content = {
                Column(modifier = Modifier.padding(Spacing.md)) {
                    content()
                }
            },
        )
    } else {
        Card(
            modifier = modifier,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
            shape = Radius.shapeMd,
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            content = {
                Column(modifier = Modifier.padding(Spacing.md)) {
                    content()
                }
            },
        )
    }
}
