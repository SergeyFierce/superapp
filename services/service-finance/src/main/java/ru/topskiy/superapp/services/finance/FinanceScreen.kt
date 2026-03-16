package ru.topskiy.superapp.services.finance

import androidx.compose.runtime.Composable
import ru.topskiy.superapp.core.ui.components.ServicePlaceholderScreen

@Composable
fun FinanceScreen(
    onBackClick: (() -> Unit)? = null,
    /** false при использовании внутри ServiceContainer */
    showTopBar: Boolean = true,
) {
    ServicePlaceholderScreen(
        serviceName = "Финансы",
        description = "Учёт расходов, доходов и платежей",
        onBackClick = onBackClick,
        showTopBar = showTopBar,
    )
}
