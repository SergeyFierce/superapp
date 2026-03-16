package ru.topskiy.superapp.services.diary

import androidx.compose.runtime.Composable
import ru.topskiy.superapp.core.ui.components.ServicePlaceholderScreen

@Composable
fun DiaryScreen(
    onBackClick: (() -> Unit)? = null,
    showTopBar: Boolean = true,
) {
    ServicePlaceholderScreen(
        serviceName = "Дневник",
        description = "Личные записи и рефлексия",
        onBackClick = onBackClick,
        showTopBar = showTopBar,
    )
}
