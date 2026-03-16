package ru.topskiy.superapp.services.diary

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.topskiy.superapp.core.navigation.AppRoutes
import ru.topskiy.superapp.platform.navigation.service
import ru.topskiy.superapp.platform.api.NavigableServiceProvider
import ru.topskiy.superapp.platform.api.ServiceCapability
import ru.topskiy.superapp.platform.api.ServiceCategory
import ru.topskiy.superapp.platform.api.ServiceDescriptor
import ru.topskiy.superapp.core.ui.components.ServiceContainer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiaryServiceProvider @Inject constructor() : NavigableServiceProvider {

    override val descriptor = ServiceDescriptor(
        id = DIARY_ID,
        title = "Дневник",
        description = "Личные записи и рефлексия",
        iconKey = "diary",
        rootRoute = AppRoutes.service(DIARY_ID),
        category = ServiceCategory.PRODUCTIVITY,
        enabledByDefault = true,
        capabilities = setOf(
            ServiceCapability.HOME_BLOCK,
            ServiceCapability.SEARCH_PROVIDER,
        ),
    )

    override fun NavGraphBuilder.registerGraph(navController: NavController) {
        composable(AppRoutes.service(DIARY_ID)) {
            ServiceContainer(
                serviceName = descriptor.title,
                onBack = { navController.popBackStack() },
            ) {
                DiaryScreen(showTopBar = false)
            }
        }
    }
}
