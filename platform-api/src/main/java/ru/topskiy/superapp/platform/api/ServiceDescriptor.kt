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
}
