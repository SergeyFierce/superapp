package ru.topskiy.superapp.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.weight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import ru.topskiy.superapp.core.ui.tokens.Spacing

/**
 * Универсальный экран-заглушка для сервисов в разработке.
 *
 * Используется во всех feature-модулях на этапе проектирования каркаса.
 * Сервис остаётся зарегистрированным в системе плагинов, но экран
 * отображает placeholder до реализации функциональности.
 *
 * @param serviceName Название сервиса (например, "Планировщик")
 * @param description Краткое описание назначения сервиса (опционально)
 * @param onBackClick Вызывается при нажатии кнопки «Назад». Если null, кнопка не отображается.
 * @param showTopBar Показывать ли TopBar. false при использовании внутри [ServiceContainer].
 */
@Composable
fun ServicePlaceholderScreen(
    serviceName: String,
    description: String = "",
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null,
    showTopBar: Boolean = true,
) {
    Column(modifier = modifier.fillMaxSize()) {
        if (showTopBar) {
            AppTopBar(
                title = "Сервис $serviceName",
                showBackButton = onBackClick != null,
                onBackClick = onBackClick ?: {},
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Spacing.md),
            ) {
                Text(
                    text = "Этот сервис находится в разработке",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "Функциональность появится в будущих версиях приложения",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
                if (description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(Spacing.xs))
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}
