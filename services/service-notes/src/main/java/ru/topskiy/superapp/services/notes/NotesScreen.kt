package ru.topskiy.superapp.services.notes

import androidx.compose.runtime.Composable
import ru.topskiy.superapp.core.ui.components.ServicePlaceholderScreen

@Composable
fun NotesScreen(
    onBackClick: (() -> Unit)? = null,
    /** false при использовании внутри ServiceContainer */
    showTopBar: Boolean = true,
) {
    ServicePlaceholderScreen(
        serviceName = "Заметки",
        description = "Быстрые заметки и записи",
        onBackClick = onBackClick,
        showTopBar = showTopBar,
    )
}
