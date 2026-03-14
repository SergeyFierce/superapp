package ru.topskiy.superapp.platform.navigation

import ru.topskiy.superapp.core.navigation.AppRoutes
import ru.topskiy.superapp.platform.api.ServiceId

/**
 * Расширения AppRoutes для работы с [ServiceId].
 */
fun AppRoutes.service(serviceId: ServiceId): String = AppRoutes.serviceRoute(serviceId.value)
fun AppRoutes.serviceSubRoute(serviceId: ServiceId, subPath: String): String =
    AppRoutes.serviceSubRoute(serviceId.value, subPath)
