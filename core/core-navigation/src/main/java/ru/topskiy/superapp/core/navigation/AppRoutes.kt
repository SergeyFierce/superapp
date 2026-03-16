package ru.topskiy.superapp.core.navigation

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
    fun serviceRoute(serviceId: String) = "$SERVICE_PREFIX/$serviceId"

    /** Маршрут с вложенным путём: "service/{serviceId}/{subPath}" */
    fun serviceSubRoute(serviceId: String, subPath: String) =
        "$SERVICE_PREFIX/$serviceId/$subPath"
}
