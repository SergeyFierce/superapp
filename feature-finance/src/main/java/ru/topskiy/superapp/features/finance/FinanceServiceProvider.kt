package ru.topskiy.superapp.features.finance

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
class FinanceServiceProvider @Inject constructor() : NavigableServiceProvider {

    override val descriptor = ServiceDescriptor(
        id = FINANCE_ID,
        title = "Финансы",
        description = "Учёт расходов, доходов и платежей",
        icon = ServiceIcon.Key("finance"),
        icon = ServiceIcon.VectorIcon(Icons.Outlined.AccountBalance),
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
            FinanceScreen()
        }
    }
}
