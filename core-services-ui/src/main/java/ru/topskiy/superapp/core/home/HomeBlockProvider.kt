package ru.topskiy.superapp.core.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.topskiy.superapp.core.services.ServiceId

interface HomeBlockProvider {

    val serviceId: ServiceId

    val priority: Int get() = 50

    @Composable
    fun Content(modifier: Modifier)
}
