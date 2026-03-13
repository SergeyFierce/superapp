package ru.topskiy.superapp.core.ui.theme

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import ru.topskiy.superapp.core.common.AppTheme as AppThemeSelection

/**
 * Тема приложения — Warm Premium.
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
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val activity = view.context.findActivity() ?: return@SideEffect
            val appWindow = activity.window
            appWindow.statusBarColor = colorScheme.background.toArgb()
            appWindow.navigationBarColor = colorScheme.background.toArgb()

            val insetsController = WindowCompat.getInsetsController(appWindow, view)
            insetsController.isAppearanceLightStatusBars = !darkTheme
            insetsController.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content,
    )
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
