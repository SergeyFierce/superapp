package ru.topskiy.superapp.core.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// ── DARK THEME — Warm Premium ────────────────────────────────────────────

private val DarkBackground = Color(0xFF1C1917)
private val DarkSurface = Color(0xFF2E2A27)
private val DarkSurfaceElevated = Color(0xFF3A3531)
private val DarkSurfaceVariant = Color(0xFF44403C)

private val DarkOutline = Color(0xFF3A3531)
private val DarkDivider = Color(0xFF44403C)

private val DarkOnSurface = Color(0xFFFAFAF9)
private val DarkOnSurfaceVariant = Color(0xFFA8A29E)
private val DarkDisabled = Color(0xFF78716C)

private val DarkPrimary = Color(0xFFF59E0B)
private val DarkOnPrimary = Color(0xFF1C1917)
private val DarkPrimaryPressed = Color(0xFFD97706)
private val DarkPrimaryContainer = Color(0xFF3A2A10)

private val DarkSecondary = Color(0xFFEAB308)
private val DarkOnSecondary = Color(0xFF1C1917)

private val DarkError = Color(0xFFEF4444)
private val DarkOnError = Color(0xFF1C1917)

val DarkColorScheme: ColorScheme = darkColorScheme(
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkPrimaryContainer,
    onPrimaryContainer = DarkOnSurface,
    secondary = DarkSecondary,
    onSecondary = DarkOnSecondary,
    error = DarkError,
    onError = DarkOnError,
    onSurface = DarkOnSurface,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = DarkOutline,
    outlineVariant = DarkDivider,
)

// ── LIGHT THEME — Warm Premium ───────────────────────────────────────────

private val LightBackground = Color(0xFFFFFBF5)
private val LightSurface = Color(0xFFFFFFFF)
private val LightSurfaceElevated = Color(0xFFFFF4E6)
private val LightSurfaceVariant = Color(0xFFF3E8D8)

private val LightOutline = Color(0xFFE7D9C6)
private val LightDivider = Color(0xFFEFE2D2)

private val LightOnSurface = Color(0xFF1C1917)
private val LightOnSurfaceVariant = Color(0xFF57534E)
private val LightDisabled = Color(0xFFA8A29E)

private val LightPrimary = Color(0xFFF59E0B)
private val LightOnPrimary = Color(0xFF1C1917)
private val LightPrimaryPressed = Color(0xFFD97706)
private val LightPrimaryContainer = Color(0xFFFFF1D6)

private val LightSecondary = Color(0xFFEAB308)
private val LightOnSecondary = Color(0xFF1C1917)

private val LightError = Color(0xFFDC2626)
private val LightOnError = Color(0xFFFFFFFF)

val LightColorScheme: ColorScheme = lightColorScheme(
    background = LightBackground,
    surface = LightSurface,
    surfaceVariant = LightSurfaceVariant,
    primary = LightPrimary,
    onPrimary = LightOnPrimary,
    primaryContainer = LightPrimaryContainer,
    onPrimaryContainer = LightOnSurface,
    secondary = LightSecondary,
    onSecondary = LightOnSecondary,
    error = LightError,
    onError = LightOnError,
    onSurface = LightOnSurface,
    onSurfaceVariant = LightOnSurfaceVariant,
    outline = LightOutline,
    outlineVariant = LightDivider,
)
