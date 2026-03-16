package ru.topskiy.superapp.platform.api

enum class ServiceCapability {
    /**
     * Виджет на домашнем экране (новое название).
     *
     * Для обратной совместимости с существующими сервисами
     * значение [HOME_BLOCK] продолжает поддерживаться и
     * трактуется как синоним.
     */
    HOME_WIDGET,

    /**
     * Старое имя для HOME_WIDGET, оставлено ради сервисов,
     * которые уже используют это значение.
     */
    HOME_BLOCK,

    SEARCH_PROVIDER,

    /**
     * Быстрые действия сервиса (например, создание сущностей
     * в один тап из домашнего экрана).
     */
    QUICK_ACTION,

    BACKGROUND_SERVICE,
    SYNC,
    NOTIFICATIONS,
    DEEP_LINK,
}
