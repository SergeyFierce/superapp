package ru.topskiy.superapp.services.planner

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.topskiy.superapp.core.navigation.AppRoutes
import ru.topskiy.superapp.platform.navigation.service
import ru.topskiy.superapp.core.ui.components.ServiceContainer
import ru.topskiy.superapp.platform.api.NavigableServiceProvider
import ru.topskiy.superapp.platform.api.ServiceCapability
import ru.topskiy.superapp.platform.api.ServiceCategory
import ru.topskiy.superapp.platform.api.ServiceDescriptor
import ru.topskiy.superapp.platform.api.ServiceId
import ru.topskiy.superapp.platform.navigation.service
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlannerServiceProvider @Inject constructor() : NavigableServiceProvider {

    override val descriptor = ServiceDescriptor(
        id = PLANNER_ID,
        title = "Планировщик",
        description = "Задачи, события и напоминания",
        iconKey = "planner",
        rootRoute = AppRoutes.service(PLANNER_ID),
        category = ServiceCategory.PRODUCTIVITY,
        enabledByDefault = true,
        capabilities = setOf(
            ServiceCapability.HOME_BLOCK,
            ServiceCapability.SEARCH_PROVIDER,
            ServiceCapability.BACKGROUND_SERVICE,
            ServiceCapability.NOTIFICATIONS,
        ),
    )

    override fun NavGraphBuilder.registerGraph(navController: NavController) {
        composable(AppRoutes.service(PLANNER_ID)) {
            ServiceContainer(
                serviceName = descriptor.title,
                onBack = { navController.popBackStack() },
            ) {
                PlannerScreen(showTopBar = false)
            }
        }
    }
}
