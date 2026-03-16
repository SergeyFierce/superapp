package ru.topskiy.superapp.platform.api

/**
 * Типизированный идентификатор сервиса.
 */
@JvmInline
value class ServiceId(val value: String) {
    override fun toString(): String = value
}
