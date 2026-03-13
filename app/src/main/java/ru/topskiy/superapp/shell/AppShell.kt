package ru.topskiy.superapp.shell

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.topskiy.superapp.core.common.AppTheme
import ru.topskiy.superapp.core.navigation.AppRoutes
import ru.topskiy.superapp.core.navigation.ServiceNavigator
import ru.topskiy.superapp.core.services.ServiceRegistry
import ru.topskiy.superapp.shell.home.homeScreen
import ru.topskiy.superapp.shell.search.searchScreen
import ru.topskiy.superapp.shell.services.servicesScreen
import ru.topskiy.superapp.shell.settings.settingsScreen

private data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
)

private val bottomNavItems = listOf(
    BottomNavItem(AppRoutes.HOME, "Главная", Icons.Outlined.Home, Icons.Filled.Home),
    BottomNavItem(AppRoutes.SERVICES, "Сервисы", Icons.Outlined.Apps, Icons.Filled.Apps),
    BottomNavItem(AppRoutes.SEARCH, "Поиск", Icons.Outlined.Search, Icons.Filled.Search),
    BottomNavItem(AppRoutes.SETTINGS, "Настройки", Icons.Outlined.Settings, Icons.Filled.Settings),
)

/**
 * Корневая оболочка приложения.
 *
 * Отвечает за:
 * - Root Scaffold с нижней навигацией
 * - Системный NavHost
 * - Привязку ServiceNavigator к NavController (attach/detach)
 */
@Composable
fun AppShell(
    serviceRegistry: ServiceRegistry,
    serviceNavigator: ServiceNavigator,
    viewModel: ShellViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()
    val shellState by viewModel.state.collectAsStateWithLifecycle()

    // Привязываем ServiceNavigator к NavController на время жизни AppShell
    DisposableEffect(navController) {
        serviceNavigator.attach(navController)
        onDispose { serviceNavigator.detach() }
    }

    Scaffold(
        bottomBar = {
            AppBottomBar(navController = navController)
        }
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            serviceRegistry = serviceRegistry,
            shellState = shellState,
            onThemeChange = viewModel::setTheme,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
private fun AppBottomBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        bottomNavItems.forEach { item ->
            val selected = currentDestination?.hierarchy
                ?.any { it.route == item.route } == true

            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (selected) item.selectedIcon else item.icon,
                        contentDescription = item.label,
                    )
                },
                label = { Text(item.label) },
            )
        }
    }
}

@Composable
private fun AppNavHost(
    navController: NavHostController,
    serviceRegistry: ServiceRegistry,
    shellState: AppShellState,
    onThemeChange: (AppTheme) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = AppRoutes.HOME,
        modifier = modifier,
    ) {
        homeScreen(navController = navController)
        servicesScreen(navController = navController, serviceRegistry = serviceRegistry)
        searchScreen(navController = navController)
        settingsScreen(shellState = shellState, onThemeChange = onThemeChange)

        // Регистрируем графы всех сервисов через контракт ServiceProvider
        serviceRegistry.getAllProviders().forEach { provider ->
            with(provider) { registerGraph(navController) }
        }
    }
}
