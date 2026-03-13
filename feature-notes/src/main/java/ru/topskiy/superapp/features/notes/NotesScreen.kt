package ru.topskiy.superapp.features.notes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.topskiy.superapp.core.ui.components.AppCard
import ru.topskiy.superapp.core.ui.components.AppTextField
import ru.topskiy.superapp.core.ui.components.AppTopBar
import ru.topskiy.superapp.core.ui.tokens.Spacing

@Composable
fun NotesScreen(viewModel: NotesViewModel = hiltViewModel()) {
    val incomingTasks by viewModel.incomingTasks.collectAsStateWithLifecycle()
    var query by remember { mutableStateOf("") }

    LazyColumn(
        contentPadding = PaddingValues(Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.sm),
        modifier = Modifier.fillMaxSize(),
    ) {
        item { AppTopBar(title = "Notes") }
        item {
            AppTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = "Поиск заметок",
                modifier = Modifier.fillMaxWidth(),
            )
        }

        if (incomingTasks.isEmpty()) {
            item {
                AppCard(modifier = Modifier.fillMaxWidth()) {
                    Text("Пока пусто", style = MaterialTheme.typography.bodyLarge)
                }
            }
        } else {
            items(incomingTasks) { note ->
                AppCard(modifier = Modifier.fillMaxWidth()) {
                    Text(note, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
