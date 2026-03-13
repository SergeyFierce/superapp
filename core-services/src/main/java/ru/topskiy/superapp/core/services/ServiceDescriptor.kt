package ru.topskiy.superapp.core.services

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Метаинформация о сервисе — всё, что нужно системе,
 * чтобы отобразить сервис в каталоге и организовать навигацию.
 *
 * Флаги supportsHome/supportsSearch заменены на [capabilities],
 * что позволяет добавлять новые возможности без изменения существующих сервисов.
 */
data class ServiceDescriptor(
    val id: ServiceId,
    val title: String,
    val description: String = "",
    val icon: ImageVector,
    /** Корневой маршрут навигации */
    val rootRoute: String,
    val category: ServiceCategory,
    val enabledByDefault: Boolean = true,
    /** Набор возможностей сервиса */
    val capabilities: Set<ServiceCapability> = emptySet(),
) {
    fun supports(capability: ServiceCapability): Boolean = capability in capabilities
}
