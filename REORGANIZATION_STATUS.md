# Статус реорганизации — Этап B

## Выполнено

### 1. Platform-модули созданы
- **platform-api**: ServiceId, ServiceCategory, ServiceCapability, ServicePlugin, ServiceDescriptor, ServiceProvider, AppService, HomeBlockProvider, SearchProvider, SearchResult, NavigableServiceProvider
- **platform-commands**: CommandBus, AppCommand, CreateTaskCommand, OpenServiceCommand  
- **platform-events**: EventBus, AppEvent, TaskCreatedEvent, ServiceOpenedEvent
- **platform-navigation**: ServiceNavigator, ServiceAction, AppRoutesExtensions (service(ServiceId))
- **platform-runtime**: ServiceRegistry, ServiceManager

### 2. Core-navigation обновлён
- AppRoutes.serviceRoute(serviceId: String) — убрана зависимость от ServiceId
- AppRoutes.serviceSubRoute(serviceId: String, subPath: String)

### 3. settings.gradle.kts
- Добавлены: platform-api, platform-commands, platform-events, platform-navigation, platform-runtime

## Необходимо выполнить

### 4. Переключить потребителей на platform
Обновить импорты во всех модулях:
- `ru.topskiy.superapp.core.services` → `ru.topskiy.superapp.platform.api`
- `ru.topskiy.superapp.core.commands` → `ru.topskiy.superapp.platform.commands`  
- `ru.topskiy.superapp.core.events` → `ru.topskiy.superapp.platform.events`
- `ru.topskiy.superapp.core.navigation.ServiceNavigator` → `ru.topskiy.superapp.platform.navigation`
- AppRoutes.service(ServiceId) → использовать AppRoutes.service(serviceId) из platform-navigation

### 5. Модули для обновления
| Модуль | Действия |
|--------|----------|
| app | Заменить core-services-*, core-commands, core-events на platform-* |
| feature-planner | platform-*, обновить package на ru.topskiy.superapp.services.planner |
| feature-notes | то же |
| feature-finance | то же |
| service-diary | Уже использует core — переключить на platform |
| core-preferences | Добавить зависимость platform-api для ServiceId, обновить импорты |
| core-common | Удалить ServiceId, ServiceCategory, ServiceCapability |

### 6. Создать feature-модули
- feature-home, feature-services, feature-search, feature-settings
- Перенести HomeScreen, ServicesScreen, SearchScreen, SettingsScreen из app/shell

### 7. Переименовать feature-* → service-*
- feature-planner → service-planner (уже есть service-diary)
- feature-notes → service-notes
- feature-finance → service-finance

### 8. Удалить старые модули
- core-services-api, core-services-runtime, core-services-ui
- core-commands, core-events

### 9. Переименовать core-preferences → core-datastore
