package ru.topskiy.superapp.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import ru.topskiy.superapp.core.ui.tokens.Elevation
import ru.topskiy.superapp.core.ui.tokens.Spacing

data class FloatingBottomBarItem(
    val route: String,
    val label: String,
    val icon: ImageVector,
)

@Composable
fun FloatingBottomBar(
    items: List<FloatingBottomBarItem>,
    selectedRoute: String?,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    containerHeight: Dp = 64.dp,
) {
    Box(
        modifier = modifier
            .padding(horizontal = Spacing.lg, vertical = Spacing.lg),
        contentAlignment = Alignment.Center,
    ) {
        val containerShape: Shape = RoundedCornerShape(32.dp)

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    clip = true
                    shape = containerShape
                },
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
            shadowElevation = Elevation.lg,
            shape = containerShape,
        ) {
            Row(
                modifier = Modifier
                    .height(containerHeight)
                    .padding(horizontal = Spacing.md),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                items.forEach { item ->
                    val selected = item.route == selectedRoute
                    FloatingBottomBarTab(
                        item = item,
                        selected = selected,
                        onClick = { onItemSelected(item.route) },
                    )
                }
            }
        }
    }
}

@Composable
private fun FloatingBottomBarTab(
    item: FloatingBottomBarItem,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val capsuleShape = RoundedCornerShape(20.dp)
    val contentColor: Color
    val backgroundModifier: Modifier

    if (selected) {
        contentColor = MaterialTheme.colorScheme.primary
        backgroundModifier = Modifier
            .clip(capsuleShape)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    } else {
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        backgroundModifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
    }

    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .then(backgroundModifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.label,
            tint = contentColor,
            modifier = Modifier.size(26.dp),
        )
        Text(
            text = item.label,
            color = contentColor,
            style = MaterialTheme.typography.labelMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

