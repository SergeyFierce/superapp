package ru.topskiy.superapp.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import ru.topskiy.superapp.core.common.AppTheme as AppThemeSelection

/**
 * Тема приложения — Warm Premium.
 *
 * Принимает [themeSelection] для учёта пользовательских настроек.
 * По умолчанию использует системную тему.
 *
 * @param themeSelection выбор темы (LIGHT / DARK / SYSTEM)
 * @param content контент, обёрнутый в тему
 */
@Composable
fun AppTheme(
    themeSelection: AppThemeSelection = AppThemeSelection.SYSTEM,
    darkTheme: Boolean = when (themeSelection) {
        AppThemeSelection.LIGHT -> false
        AppThemeSelection.DARK -> true
        AppThemeSelection.SYSTEM -> isSystemInDarkTheme()
    },
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content,
    )
}
