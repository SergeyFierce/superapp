package ru.topskiy.superapp.services.planner

import androidx.compose.runtime.Composable
import ru.topskiy.superapp.core.ui.components.ServicePlaceholderScreen

@Composable
fun PlannerScreen(
    onBackClick: (() -> Unit)? = null,
    /** false при использовании внутри ServiceContainer */
    showTopBar: Boolean = true,
) {
    ServicePlaceholderScreen(
        serviceName = "Планировщик",
        description = "Планирование задач и напоминаний",
        onBackClick = onBackClick,
        showTopBar = showTopBar,
    )
}
