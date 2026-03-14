package ru.topskiy.superapp.shell

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.topskiy.superapp.core.common.AppTheme
import ru.topskiy.superapp.core.navigation.AppRoutes
import ru.topskiy.superapp.platform.api.NavigableServiceProvider
import ru.topskiy.superapp.platform.navigation.ServiceNavigator
import ru.topskiy.superapp.platform.runtime.ServiceRegistry
import ru.topskiy.superapp.core.ui.components.FloatingBottomBar
import ru.topskiy.superapp.core.ui.components.FloatingBottomBarItem
import ru.topskiy.superapp.features.home.homeScreen
import ru.topskiy.superapp.features.search.searchScreen
import ru.topskiy.superapp.features.services.servicesScreen
import ru.topskiy.superapp.features.settings.settingsScreen

private val bottomNavItems = listOf(
    FloatingBottomBarItem(AppRoutes.HOME, "Главная", Icons.Outlined.Home),
    FloatingBottomBarItem(AppRoutes.SERVICES, "Сервисы", Icons.Outlined.GridView),
    FloatingBottomBarItem(AppRoutes.SEARCH, "Поиск", Icons.Outlined.Search),
    FloatingBottomBarItem(AppRoutes.SETTINGS, "Настройки", Icons.Outlined.Settings),
)

@Composable
fun AppShell(
    serviceRegistry: ServiceRegistry,
    serviceNavigator: ServiceNavigator,
    viewModel: ShellViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()
    val shellState by viewModel.state.collectAsStateWithLifecycle()

    DisposableEffect(navController) {
        serviceNavigator.attach(navController)
        onDispose { serviceNavigator.detach() }
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(modifier = Modifier.fillMaxSize()) {
        AppNavHost(
            navController = navController,
            serviceRegistry = serviceRegistry,
            shellState = shellState,
            onThemeChange = viewModel::setTheme,
            modifier = Modifier.fillMaxSize(),
        )

        FloatingBottomBar(
            items = bottomNavItems,
            selectedRoute = currentRoute,
            onItemSelected = { route ->
                navController.navigate(route) {
                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .windowInsetsPadding(WindowInsets.navigationBars),
        )
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
        modifier = modifier.windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)),
    ) {
        homeScreen(navController = navController)
        servicesScreen(navController = navController, serviceRegistry = serviceRegistry)
        searchScreen(navController = navController)
        settingsScreen(theme = shellState.theme, onThemeChange = onThemeChange)

        serviceRegistry.getAllProviders()
            .filterIsInstance<NavigableServiceProvider>()
            .forEach { provider -> with(provider) { registerGraph(navController) } }
    }
}
