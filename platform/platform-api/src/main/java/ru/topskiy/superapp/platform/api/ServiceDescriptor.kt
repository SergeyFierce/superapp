package ru.topskiy.superapp.platform.api

data class ServiceDescriptor(
    val id: ServiceId,
    val title: String,
    val description: String = "",
    val iconKey: String,
    val rootRoute: String,
    val category: ServiceCategory,
    val enabledByDefault: Boolean = true,
    val capabilities: Set<ServiceCapability> = emptySet(),
) {
    fun supports(capability: ServiceCapability): Boolean = capability in capabilities

    /**
     * Удобный алиас: сервис считается поддерживающим виджеты
     * домой, если у него указана capability HOME_WIDGET или
     * (для обратной совместимости) старое значение HOME_BLOCK.
     */
    fun supportsHomeWidget(): Boolean =
        ServiceCapability.HOME_WIDGET in capabilities ||
            ServiceCapability.HOME_BLOCK in capabilities
}
