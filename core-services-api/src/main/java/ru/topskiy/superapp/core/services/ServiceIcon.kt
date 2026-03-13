package ru.topskiy.superapp.core.services

sealed interface ServiceIcon {
    data class Key(
        val name: String,
    ) : ServiceIcon
}
