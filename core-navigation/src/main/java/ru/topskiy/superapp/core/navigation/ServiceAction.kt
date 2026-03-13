package ru.topskiy.superapp.core.navigation

import ru.topskiy.superapp.core.services.ServiceId

/**
 * Действия межсервисной навигации.
 *
 * Сервис создаёт ServiceAction и передаёт в ServiceNavigator —
 * он не знает о NavController и не зависит от конкретных маршрутов.
 */
sealed interface ServiceAction {

    /** Открыть корневой экран сервиса по его идентификатору */
    data class OpenService(val serviceId: ServiceId) : ServiceAction

    /** Перейти по произвольному маршруту (для вложенной навигации внутри сервиса) */
    data class Navigate(val route: String) : ServiceAction

    /** Вернуться назад */
    data object GoBack : ServiceAction
}
