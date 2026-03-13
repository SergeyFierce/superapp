package ru.topskiy.superapp.features.planner

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.topskiy.superapp.core.navigation.AppRoutes
import ru.topskiy.superapp.core.services.ServiceCapability
import ru.topskiy.superapp.core.services.ServiceCategory
import ru.topskiy.superapp.core.services.ServiceDescriptor
import ru.topskiy.superapp.core.services.ServiceId
import ru.topskiy.superapp.core.services.ServiceIcon
import ru.topskiy.superapp.core.services.NavigableServiceProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlannerServiceProvider @Inject constructor() : NavigableServiceProvider {

    override val descriptor = ServiceDescriptor(
        id = PLANNER_ID,
        title = "Планировщик",
        description = "Задачи, события и напоминания",
        icon = ServiceIcon.Key("planner"),
        icon = ServiceIcon.VectorIcon(Icons.Outlined.CalendarToday),
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
            PlannerScreen()
        }
    }
}
