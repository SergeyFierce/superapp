package ru.topskiy.superapp.core.navigation

import ru.topskiy.superapp.core.services.ServiceId

/**
 * Единый источник истины для всех маршрутов приложения.
 *
 * Системные маршруты принадлежат оболочке.
 * Сервисные маршруты строятся по схеме "service/{serviceId}".
 */
object AppRoutes {

    // ── Системные маршруты ──────────────────────────────────────────────
    /** Стартовый маршрут: проверяет онбординг и перенаправляет */
    const val BOOTSTRAP = "bootstrap"
    const val ONBOARDING = "onboarding"
    /** Основная оболочка с нижней навигацией */
    const val SHELL = "shell"
    const val HOME = "home"
    const val SERVICES = "services"
    const val SEARCH = "search"
    const val SETTINGS = "settings"

    // ── Сервисные маршруты ──────────────────────────────────────────────
    private const val SERVICE_PREFIX = "service"

    /** Корневой маршрут сервиса: "service/{serviceId}" */
    fun service(serviceId: ServiceId) = "$SERVICE_PREFIX/${serviceId.value}"

    /** Маршрут с вложенным путём: "service/{serviceId}/{subPath}" */
    fun serviceSubRoute(serviceId: ServiceId, subPath: String) =
        "$SERVICE_PREFIX/${serviceId.value}/$subPath"
}
