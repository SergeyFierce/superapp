package ru.topskiy.superapp.core.home

import androidx.compose.runtime.Composable
import ru.topskiy.superapp.core.services.ServiceId

/**
 * Контракт участия сервиса в экране "Главная".
 * Дополняет [ServiceCapability.HOME_BLOCK] в дескрипторе —
 * система фильтрует по capability, а блок предоставляет содержимое.
 */
interface HomeBlockProvider {

    val serviceId: ServiceId

    /**
     * Приоритет отображения (меньше = выше в списке).
     */
    val priority: Int get() = 50

    @Composable
    fun Content()
}
