package ru.topskiy.superapp.core.home

import androidx.compose.runtime.Composable
import ru.topskiy.superapp.core.services.ServiceId

interface HomeBlockProvider {

    val serviceId: ServiceId

    val priority: Int get() = 50

    @Composable
    fun Content()
}
