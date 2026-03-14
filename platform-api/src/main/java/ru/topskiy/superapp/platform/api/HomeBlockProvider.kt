package ru.topskiy.superapp.platform.api

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface HomeBlockProvider {
    val serviceId: ServiceId
    val priority: Int get() = 50

    @Composable
    fun Content(modifier: Modifier)
}
