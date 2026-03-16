package ru.topskiy.superapp.services.finance

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
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FinanceServiceProvider @Inject constructor() : NavigableServiceProvider {

    override val descriptor = ServiceDescriptor(
        id = FINANCE_ID,
        title = "Финансы",
        description = "Учёт расходов, доходов и платежей",
        iconKey = "finance",
        rootRoute = AppRoutes.service(FINANCE_ID),
        category = ServiceCategory.FINANCE,
        enabledByDefault = true,
        capabilities = setOf(
            ServiceCapability.HOME_BLOCK,
            ServiceCapability.SEARCH_PROVIDER,
            ServiceCapability.DEEP_LINK,
        ),
    )

    override fun NavGraphBuilder.registerGraph(navController: NavController) {
        composable(AppRoutes.service(FINANCE_ID)) {
            ServiceContainer(
                serviceName = descriptor.title,
                onBack = { navController.popBackStack() },
            ) {
                FinanceScreen(showTopBar = false)
            }
        }
    }
}
