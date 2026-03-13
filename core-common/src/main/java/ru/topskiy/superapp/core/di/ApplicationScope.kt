package ru.topskiy.superapp.core.di

import javax.inject.Qualifier

/**
 * Qualifier для CoroutineScope, привязанного к жизненному циклу приложения.
 * Используется Singleton-обработчиками, которые должны работать
 * независимо от того, открыт ли какой-либо экран.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApplicationScope
