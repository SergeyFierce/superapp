package ru.topskiy.superapp.shell.bootstrap

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.topskiy.superapp.core.navigation.AppRoutes

fun NavGraphBuilder.bootstrapScreen(
    onNavigateToOnboarding: () -> Unit,
    onNavigateToShell: () -> Unit,
) {
    composable(AppRoutes.BOOTSTRAP) {
        val viewModel: BootstrapViewModel = hiltViewModel()
        BootstrapScreen(
            viewModel = viewModel,
            onNavigateToOnboarding = onNavigateToOnboarding,
            onNavigateToShell = onNavigateToShell,
        )
    }
}

/**
 * Стартовый экран.
 * Читает [PreferencesRepository.onboardingCompleted] и перенаправляет:
 * - false → Онбординг (первый запуск)
 * - true  → Shell (основная оболочка)
 *
 * Показывает индикатор загрузки пока DataStore не ответил.
 */
@Composable
fun BootstrapScreen(
    viewModel: BootstrapViewModel = hiltViewModel(),
    onNavigateToOnboarding: () -> Unit,
    onNavigateToShell: () -> Unit,
) {
    val destination by viewModel.destination.collectAsStateWithLifecycle()

    LaunchedEffect(destination) {
        when (destination) {
            BootstrapDestination.ONBOARDING -> onNavigateToOnboarding()
            BootstrapDestination.SHELL -> onNavigateToShell()
            BootstrapDestination.LOADING -> Unit
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        CircularProgressIndicator()
    }
}
