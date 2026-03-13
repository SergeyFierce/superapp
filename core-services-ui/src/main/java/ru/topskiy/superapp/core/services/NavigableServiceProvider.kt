package ru.topskiy.superapp.core.services

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder

/**
 * Расширение [ServiceProvider] для сервисов, которые регистрируют
 * свой навигационный граф в системном NavHost.
 *
 * Платформенно-зависимый контракт, вынесенный в core-services-ui,
 * чтобы core-services-api оставался независимым от Android.
 */
interface NavigableServiceProvider : ServiceProvider {

    /**
     * Регистрация навигационного графа сервиса в системном NavHost.
     */
    fun NavGraphBuilder.registerGraph(navController: NavController)
}

