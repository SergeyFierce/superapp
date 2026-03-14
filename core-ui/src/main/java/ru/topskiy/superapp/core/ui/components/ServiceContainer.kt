package ru.topskiy.superapp.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.weight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Единая оболочка для всех экранов сервисов.
 *
 * Обеспечивает:
 * - Единый UI-фрейм (toolbar с кнопкой «Назад»)
 * - Централизованную обработку выхода из сервиса
 * - Подготовку к nested NavHost внутри сервиса
 *
 * Сейчас content — один экран (placeholder или основной).
 * Позже content может быть NavHost с внутренними маршрутами сервиса.
 *
 * @param serviceName Заголовок в TopBar (например, "Планировщик")
 * @param onBack Вызывается при нажатии «Назад» — выход из сервиса
 * @param content Контент сервиса. Сейчас — один экран, позже — nested NavHost
 */
@Composable
fun ServiceContainer(
    serviceName: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier.fillMaxSize()) {
        AppTopBar(
            title = "Сервис $serviceName",
            showBackButton = true,
            onBackClick = onBack,
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            content()
        }
    }
}
