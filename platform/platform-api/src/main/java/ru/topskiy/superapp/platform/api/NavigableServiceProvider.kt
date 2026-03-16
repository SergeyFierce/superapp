package ru.topskiy.superapp.platform.api

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder

/**
 * Расширение [ServiceProvider] для сервисов, которые регистрируют
 * свой навигационный граф в системном NavHost.
 *
 * Платформенно-зависимый контракт для Android.
 */
interface NavigableServiceProvider : ServiceProvider {

    /**
     * Регистрация навигационного графа сервиса в системном NavHost.
     */
    fun NavGraphBuilder.registerGraph(navController: NavController)
}
