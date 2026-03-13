package ru.topskiy.superapp.core.services

/**
 * Платформенно-независимый контракт сервиса.
 *
 * Содержит только метаданные и фабрику background-части.
 * Платформенно-специфичные аспекты (навигация, UI) расширяются
 * в отдельных модулях, например core-services-ui.
 */
interface ServiceProvider {

    /** Метаинформация о сервисе */
    val descriptor: ServiceDescriptor

    /**
     * Создать экземпляр background-части сервиса.
     *
     * Возвращает [AppService] если сервис имеет фоновую активность
     * (периодические задачи, подписки и т.п.).
     * Возвращает null если сервис работает только через UI.
     */
    fun createService(): AppService? = null
}


