package ru.topskiy.superapp.core.services

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder

/**
 * Контракт подключения сервиса к системе.
 *
 * Каждый сервис реализует этот интерфейс и регистрируется
 * через Hilt multibindings (@IntoSet). Система никогда не
 * импортирует конкретные сервисы напрямую.
 */
interface ServiceProvider {

    /** Метаинформация о сервисе */
    val descriptor: ServiceDescriptor

    /**
     * Регистрация навигационного графа сервиса в системном NavHost.
     */
    fun NavGraphBuilder.registerGraph(navController: NavController)

    /**
     * Создать экземпляр background-части сервиса.
     *
     * Возвращает [AppService] если сервис имеет фоновую активность
     * (периодические задачи, подписки и т.п.).
     * Возвращает null если сервис работает только через UI.
     *
     * [ServiceManager] вызывает этот метод при старте приложения
     * и управляет lifecycle возвращённого объекта.
     */
    fun createService(): AppService? = null
}
