package ru.topskiy.superapp.platform.navigation

import androidx.navigation.NavController
import ru.topskiy.superapp.core.navigation.AppRoutes
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Системный навигатор для межсервисных переходов.
 *
 * Сервисы инжектируют ServiceNavigator и вызывают [execute],
 * не зная об устройстве NavController и конкретных маршрутах.
 *
 * Жизненный цикл:
 * - AppShell вызывает [attach] при создании NavController
 * - AppShell вызывает [detach] в onDispose (при уничтожении)
 *
 * Singleton безопасен: ссылка на NavController очищается
 * через [detach], поэтому утечки не происходит.
 */
@Singleton
class ServiceNavigator @Inject constructor() {

    private var navController: NavController? = null

    /** Вызывается из AppShell при инициализации NavController */
    fun attach(controller: NavController) {
        navController = controller
    }

    /** Вызывается из AppShell в DisposableEffect.onDispose */
    fun detach() {
        navController = null
    }

    /** Выполнить навигационное действие */
    fun execute(action: ServiceAction) {
        val nav = navController ?: return
        when (action) {
            is ServiceAction.OpenService ->
                nav.navigate(AppRoutes.serviceRoute(action.serviceId.value))

            is ServiceAction.Navigate ->
                nav.navigate(action.route)

            is ServiceAction.GoBack ->
                nav.popBackStack()
        }
    }
}
