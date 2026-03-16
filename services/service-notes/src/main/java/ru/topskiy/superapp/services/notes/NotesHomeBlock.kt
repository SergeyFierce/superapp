package ru.topskiy.superapp.services.notes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.StickyNote2
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.topskiy.superapp.platform.api.HomeBlockProvider
import ru.topskiy.superapp.platform.api.ServiceId
import ru.topskiy.superapp.core.ui.components.AppCard
import ru.topskiy.superapp.core.ui.tokens.Spacing
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotesHomeBlock @Inject constructor() : HomeBlockProvider {

    override val serviceId: ServiceId = NOTES_ID
    override val priority = 20

    @Composable
    override fun Content(modifier: Modifier) {
        AppCard(modifier = modifier.fillMaxWidth()) {
            androidx.compose.foundation.layout.Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                Icon(Icons.Outlined.StickyNote2, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Text("Заметки", style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                Icon(Icons.AutoMirrored.Outlined.ArrowForward, contentDescription = null)
            }
            Text(
                text = "В разработке",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = Spacing.xs),
            )
        }
    }
}
