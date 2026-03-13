package ru.topskiy.superapp.features.finance

import ru.topskiy.superapp.core.navigation.AppRoutes
import ru.topskiy.superapp.core.search.SearchProvider
import ru.topskiy.superapp.core.search.SearchResult
import ru.topskiy.superapp.core.services.ServiceId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FinanceSearchProvider @Inject constructor() : SearchProvider {

    override val serviceId: ServiceId = FINANCE_ID

    private val stubPayments = listOf(
        "Интернет",
        "Телефон",
        "Коммунальные услуги",
        "Аренда",
        "Подписка на стриминг",
    )

    override suspend fun search(query: String): List<SearchResult> =
        stubPayments
            .filter { it.contains(query, ignoreCase = true) }
            .mapIndexed { index, payment ->
                SearchResult(
                    serviceId = FINANCE_ID,
                    id = "payment_$index",
                    title = payment,
                    subtitle = "Платёж · Финансы",
                    route = AppRoutes.service(FINANCE_ID),
                    relevance = 0.8f,
                )
            }
}
