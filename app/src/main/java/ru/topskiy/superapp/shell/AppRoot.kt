package ru.topskiy.superapp.shell

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.topskiy.superapp.core.navigation.AppRoutes
import ru.topskiy.superapp.core.navigation.ServiceNavigator
import ru.topskiy.superapp.core.services.ServiceRegistry
import ru.topskiy.superapp.shell.bootstrap.bootstrapScreen
import ru.topskiy.superapp.shell.onboarding.onboardingScreen

/**
 * Корневой композабл приложения.
 *
 * Управляет верхнеуровневым флоу:
 *   bootstrap → onboarding (первый запуск) → shell
 *   bootstrap →                               shell (повторный запуск)
 *
 * [AppShell] внутри "shell" содержит свой NavHost с нижней навигацией
 * и сервисными маршрутами. [ServiceNavigator] привязывается к внутреннему
 * NavController AppShell, а не к корневому.
 */
@Composable
fun AppRoot(
    serviceRegistry: ServiceRegistry,
    serviceNavigator: ServiceNavigator,
) {
    val rootNavController = rememberNavController()

    NavHost(
        navController = rootNavController,
        startDestination = AppRoutes.BOOTSTRAP,
    ) {
        bootstrapScreen(
            onNavigateToOnboarding = {
                rootNavController.navigate(AppRoutes.ONBOARDING) {
                    popUpTo(AppRoutes.BOOTSTRAP) { inclusive = true }
                }
            },
            onNavigateToShell = {
                rootNavController.navigate(AppRoutes.SHELL) {
                    popUpTo(AppRoutes.BOOTSTRAP) { inclusive = true }
                }
            },
        )

        onboardingScreen(
            onComplete = {
                rootNavController.navigate(AppRoutes.SHELL) {
                    popUpTo(AppRoutes.ONBOARDING) { inclusive = true }
                }
            },
        )

        composable(AppRoutes.SHELL) {
            AppShell(
                serviceRegistry = serviceRegistry,
                serviceNavigator = serviceNavigator,
            )
        }
    }
}
