package ru.topskiy.superapp.core.services

/**
 * Типизированный идентификатор сервиса.
 * Value class — нулевые накладные расходы в рантайме,
 * но компилятор различает ServiceId и обычный String.
 */
@JvmInline
value class ServiceId(val value: String) {
    override fun toString(): String = value
}
