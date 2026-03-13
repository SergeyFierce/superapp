package ru.topskiy.superapp.core.services

import androidx.compose.ui.graphics.vector.ImageVector

sealed interface ServiceIcon {
    data class VectorIcon(val icon: ImageVector) : ServiceIcon
}
