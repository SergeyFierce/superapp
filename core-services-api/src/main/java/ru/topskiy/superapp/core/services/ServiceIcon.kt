package ru.topskiy.superapp.core.services

sealed interface ServiceIcon {
    data class Key(
        val name: String,
    ) : ServiceIcon
import androidx.compose.ui.graphics.vector.ImageVector

sealed interface ServiceIcon {
    data class VectorIcon(val icon: ImageVector) : ServiceIcon
}
