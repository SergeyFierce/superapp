# Архитектурная реорганизация SuperApp — Этап A: Анализ

**Дата:** 14 марта 2025  
**Цель:** Привести проект к правильной, масштабируемой архитектуре super app / personal assistant

---

## 1. ТЕКУЩАЯ СТРУКТУРА ПРОЕКТА

```
superapp/
├── app/                          # Точка входа + shell screens встроены
│   ├── shell/
│   │   ├── AppRoot.kt, AppShell.kt, ShellViewModel.kt, ThemeViewModel.kt
│   │   ├── home/ (HomeScreen, HomeViewModel)
│   │   ├── services/ (ServicesScreen, ServicesViewModel)
│   │   ├── search/ (SearchScreen, SearchViewModel)
│   │   ├── settings/ (SettingsScreen)
│   │   ├── onboarding/ (OnboardingScreen, OnboardingViewModel)
│   │   └── bootstrap/ (BootstrapScreen, BootstrapViewModel)
│   ├── di/AppModule.kt
│   ├── MainActivity.kt, SuperApp.kt
│
├── core-common/                  # AppTheme, ServiceId, ServiceCategory, ServiceCapability, AppBootstrap
├── core-commands/                # CommandBus, CreateTaskCommand, OpenServiceCommand
├── core-events/                  # EventBus, TaskCreatedEvent, ServiceOpenedEvent
├── core-services-api/            # ServicePlugin, ServiceDescriptor, ServiceProvider, AppService
├── core-services-runtime/       # ServiceRegistry, ServiceManager
├── core-services-ui/            # HomeBlockProvider, SearchProvider, NavigableServiceProvider, ServiceIconMapping
├── core-database/               # AppDatabase (placeholder)
├── core-preferences/            # PreferencesRepository (DataStore)
├── core-jobs/                   # ReminderWorker, JobScheduler
├── core-navigation/             # AppRoutes, ServiceAction, ServiceNavigator
├── core-ui/                     # AppButton, AppCard, AppTopBar, ServicePlaceholderScreen, theme
│
├── feature-planner/             # Planner как сервис (НЕПРАВИЛЬНО размещён)
├── feature-notes/              # Notes как сервис
├── feature-finance/            # Finance как сервис
```

---

## 2. ЧТО СЕЙЧАС НЕ ТАК

### 2.1 Смешение слоёв в app

| Проблема | Детали |
|----------|--------|
| **Shell screens в app** | Home, Services, Search, Settings лежат в `app/shell/*` — это экраны оболочки (features), а не точка входа |
| **Нарушение SRP** | app отвечает за всё: bootstrap, onboarding, NavHost, composition root и 4 полноценных экрана |
| **Масштабируемость** | Добавление нового экрана оболочки = изменения в app |

### 2.2 Неправильная номенклатура: feature vs service

| Текущее | Правильное |
|---------|------------|
| `feature-planner` | Сервис, не feature оболочки |
| `feature-notes` | Сервис |
| `feature-finance` | Сервис |
| `app/shell/home` | Это feature Home |
| `app/shell/services` | Это feature Services (каталог) |

**Feature** = экран оболочки приложения (Home, Services, Search, Settings)  
**Service** = отдельный функциональный модуль (Planner, Notes, Diary, Finance)

### 2.3 Сервисная платформа в core

| Компонент | Текущее место | Должно быть |
|-----------|---------------|-------------|
| ServicePlugin, ServiceDescriptor | core-services-api | platform |
| ServiceRegistry, ServiceManager | core-services-runtime | platform |
| HomeBlockProvider, SearchProvider | core-services-ui | platform |
| ServiceNavigator, ServiceAction | core-navigation | platform (или core + platform) |
| CommandBus, EventBus | core-commands, core-events | platform |

**core** должен содержать только обще техническую инфраструктуру, не связанную с супер-аппом.

### 2.4 Отсутствие слоёв domain и data

- Нет явного слоя **domain** (модели, интерфейсы репозиториев, use cases)
- Нет явного слоя **data** (реализации репозиториев, источники данных)
- core-preferences, core-database — это data-слой, но без абстракций domain

### 2.5 Отсутствие сервиса Diary

Целевая архитектура предполагает 4 сервиса: Planner, Notes, Diary, Finance.  
Diary отсутствует.

### 2.6 Нет build-logic и testing модуля

- build-logic — общие конвенции сборки (если нужны)
- testing — общие инструменты тестирования

### 2.7 Package structure

- `ru.topskiy.superapp.shell.*` — смешивает все features
- `ru.topskiy.superapp.features.planner` — на самом деле service
- `ru.topskiy.superapp.core.services` — это platform, не core

---

## 3. ЧТО НУЖНО ПЕРЕНЕСТИ

### 3.1 Из app в features (новые модули)

| Содержимое | Источник | Назначение |
|------------|----------|------------|
| HomeScreen, HomeViewModel | app/shell/home | feature-home |
| ServicesScreen, ServicesViewModel | app/shell/services | feature-services |
| SearchScreen, SearchViewModel | app/shell/search | feature-search |
| SettingsScreen, (ThemeViewModel?) | app/shell/settings | feature-settings |
| BootstrapScreen, BootstrapViewModel | app/shell/bootstrap | Оставить в app или feature-bootstrap |
| OnboardingScreen, OnboardingViewModel | app/shell/onboarding | Оставить в app или feature-onboarding |

**Рекомендация:** Bootstrap и Onboarding оставить в app — это часть entry flow, не отдельные features.

### 3.2 Из core в platform

| Текущий модуль | Содержимое | Новый модуль |
|----------------|------------|--------------|
| core-services-api | ServicePlugin, ServiceDescriptor, ServiceProvider, AppService | platform-api |
| core-services-runtime | ServiceRegistry, ServiceManager | platform-runtime |
| core-services-ui | HomeBlockProvider, SearchProvider, SearchResult, NavigableServiceProvider, ServiceIconMapping | platform-api (контракты) + platform-ui (иконки, если нужно) |
| core-commands | CommandBus, AppCommand, CreateTaskCommand, OpenServiceCommand | platform-commands |
| core-events | EventBus, AppEvent, TaskCreatedEvent, ServiceOpenedEvent | platform-events |
| core-navigation | ServiceAction, ServiceNavigator | platform-navigation |

**Примечание:** AppRoutes содержит и системные (home, services), и сервисные маршруты. Оставить в core-navigation как единый источник истины; platform-navigation зависит от core-navigation.

### 3.3 Переименование feature → service

| Текущее | Новое |
|---------|-------|
| feature-planner | service-planner |
| feature-notes | service-notes |
| feature-finance | service-finance |
| — | service-diary (новый) |

### 3.4 Core — что остаётся

| Модуль | Содержимое | Комментарий |
|--------|------------|-------------|
| core-common | AppTheme, AppBootstrap, ApplicationScope | Общие типы, bootstrap marker |
| core-ui | AppButton, AppCard, AppTopBar, ServicePlaceholderScreen, theme | Design system |
| core-navigation | AppRoutes | Единый источник маршрутов |
| core-database | AppDatabase | Инфраструктура данных |
| core-preferences | PreferencesRepository | Инфраструктура данных |
| core-jobs | WorkManager jobs | Инфраструктура |

**Удалить из core:** ServiceId, ServiceCategory, ServiceCapability → перенести в platform-api.

---

## 4. ЦЕЛЕВАЯ СТРУКТУРА МОДУЛЕЙ

```
superapp/
├── build-logic/                 # [Опционально] Конвенции сборки
│
├── app/                         # Точка входа, composition root, AppShell, NavHost
│   ├── Application, MainActivity
│   ├── AppRoot, AppShell
│   ├── ShellViewModel, ThemeViewModel
│   ├── bootstrap/, onboarding/  # Entry flow
│   ├── di/                      # Composition root, собирает всё
│   └── НЕ содержит: Home, Services, Search, Settings экраны
│
├── core/                        # Техническая инфраструктура
│   ├── core-common/             # AppTheme, ApplicationScope, общие типы
│   ├── core-ui/                 # Design system, компоненты
│   ├── core-navigation/         # AppRoutes (все маршруты)
│   ├── core-database/           # Room
│   ├── core-datastore/          # DataStore (переименовать core-preferences)
│   ├── core-jobs/               # WorkManager
│   └── core-testing/             # [Опционально] Общие тест-утилиты
│
├── platform/                    # Платформа супер-аппа
│   ├── platform-api/            # Контракты: ServicePlugin, ServiceDescriptor, ServiceProvider,
│   │                            # HomeBlockProvider, SearchProvider, ServiceId, ServiceCapability
│   ├── platform-runtime/        # ServiceRegistry, ServiceManager
│   ├── platform-navigation/     # ServiceNavigator, ServiceAction
│   ├── platform-commands/       # CommandBus, команды
│   └── platform-events/          # EventBus, события
│
├── features/                   # Экраны оболочки приложения
│   ├── feature-home/
│   ├── feature-services/
│   ├── feature-search/
│   └── feature-settings/
│
├── services/                   # Сервисы приложения
│   ├── service-planner/
│   ├── service-notes/
│   ├── service-diary/           # Новый
│   └── service-finance/
│
├── domain/                     # [Фаза 2] Бизнес-модели, интерфейсы
│   └── domain-common/          # Пока пустой или минимальный
│
├── data/                       # [Фаза 2] Реализации репозиториев
│   └── data-preferences/       # PreferencesRepository
│
└── testing/                    # [Опционально] Общие тесты
```

---

## 5. ЦЕЛЕВОЙ DEPENDENCY GRAPH

```
                                    ┌─────────┐
                                    │   app   │
                                    └────┬────┘
                                         │
         ┌───────────────────────────────┼───────────────────────────────┐
         │                               │                               │
         ▼                               ▼                               ▼
┌─────────────────┐             ┌─────────────────┐             ┌─────────────────┐
│    features     │             │    platform      │             │  bootstrap/     │
│  home, services,│◄────────────│  api, runtime,  │             │  onboarding     │
│  search,        │             │  navigation,    │             │  (в app)         │
│  settings       │             │  commands,      │             └─────────────────┘
└────────┬────────┘             │  events         │
         │                      └────────┬─────────┘
         │                               │
         │                      ┌────────┼─────────────────────────────┐
         │                      │        │                             │
         │                      ▼        ▼                             ▼
         │               ┌───────────┐ ┌───────────┐             ┌───────────┐
         │               │  core-    │ │  core-    │             │  core-    │
         └──────────────►│  common   │ │  ui       │             │  database │
                         │  navigation│  datastore │             │  jobs      │
                         └───────────┘ └───────────┘             └───────────┘
                                         │
                                         │
                                ┌────────┴────────┐
                                │    services    │
                                │  planner,      │
                                │  notes, diary, │
                                │  finance       │
                                └────────────────┘
```

**Правила зависимостей:**
- `app` → features, platform, core, services
- `features` → platform, core
- `platform` → core
- `services` → platform, core
- **Нет:** services → services, features → services (кроме косвенно через platform)

---

## 6. ДЕТАЛИЗАЦИЯ ПЕРЕНОСА ПО МОДУЛЯМ

### 6.1 platform-api

**Содержимое (из core-services-api + core-services-ui контракты):**
- ServicePlugin, ServiceDescriptor, ServiceProvider, AppService
- ServiceId, ServiceCategory, ServiceCapability (из core-common)
- HomeBlockProvider, SearchProvider, SearchResult
- NavigableServiceProvider

**Зависимости:** core-common (только базовые типы, если нужны), иначе — минимум.

### 6.2 platform-runtime

**Содержимое:**
- ServiceRegistry, ServiceManager

**Зависимости:** platform-api, core-datastore (preferences), core-common

### 6.3 platform-navigation

**Содержимое:**
- ServiceNavigator, ServiceAction

**Зависимости:** platform-api, core-navigation (AppRoutes)

### 6.4 platform-commands

**Содержимое:**
- CommandBus, AppCommand, CreateTaskCommand, OpenServiceCommand

**Зависимости:** core-common (или нет — чистый Kotlin)

### 6.5 platform-events

**Содержимое:**
- EventBus, AppEvent, TaskCreatedEvent, ServiceOpenedEvent

**Зависимости:** platform-api (ServiceId в событиях)

### 6.6 core-common после реорганизации

**Оставить:**
- AppTheme
- AppBootstrap
- ApplicationScope (корутины)

**Удалить (→ platform-api):**
- ServiceId, ServiceCategory, ServiceCapability

### 6.7 feature-home

**Содержимое:** HomeScreen, HomeViewModel  
**Зависимости:** platform-api, platform-runtime, core-ui, core-datastore

### 6.8 feature-services

**Содержимое:** ServicesScreen, ServicesViewModel  
**Зависимости:** platform-api, platform-runtime, core-ui, core-datastore

**Уже корректно:** получает сервисы из ServiceRegistry, отображает карточки, по клику — `navController.navigate(descriptor.rootRoute)`.

### 6.9 feature-search

**Содержимое:** SearchScreen, SearchViewModel  
**Зависимости:** platform-api, platform-runtime, core-ui

### 6.10 feature-settings

**Содержимое:** SettingsScreen  
**Зависимости:** core-ui, core-datastore

### 6.11 service-planner (бывший feature-planner)

**Содержимое:** PlannerScreen, PlannerPlugin, PlannerHomeBlock, PlannerSearchProvider, PlannerApi, PlannerCommandHandler, PlannerBootstrap  
**Зависимости:** platform-*, core-ui, core-navigation

### 6.12 service-diary (новый)

**Содержимое:** Placeholder Screen, DiaryPlugin, DiaryHomeBlock, DiarySearchProvider  
**Зависимости:** platform-*, core-ui

---

## 7. НАВИГАЦИЯ — ТЕКУЩЕЕ СОСТОЯНИЕ

| Аспект | Статус |
|--------|--------|
| AppShell с Home, Services, Search, Settings | ✅ Реализовано |
| ServicesScreen берёт сервисы из registry | ✅ Реализовано |
| По клику — navigate(rootRoute) | ✅ Реализовано |
| ServiceNavigator для межсервисной навигации | ✅ Реализовано |
| Nested navigation внутри сервисов | ⏳ Готовность архитектуры есть (маршруты service/{id}/{subPath}) |

**Вывод:** Навигация уже близка к целевой. ServicesScreen корректно работает через registry. Требуется только перенос в правильные модули.

---

## 7.1 ServiceContainer и Nested Service Navigation

### Зачем нужен ServiceContainer

**ServiceContainer** — единая оболочка для всех сервисов, которая:

- Даёт единый UI-фрейм (toolbar, back-кнопка)
- Централизует обработку "назад" (выйти из сервиса vs назад внутри сервиса)
- Готовит место для вложенной навигации сервиса

### Архитектура

```
Main NavHost (AppShell)
├── home
├── services
├── search
├── settings
└── service/{serviceId}          ← одна точка входа на сервис
        └── ServiceContainer
                └── [Сейчас] один экран (placeholder)
                └── [Потом] nested NavHost сервиса
```

### Реализация ServiceContainer

| Компонент | Описание |
|-----------|----------|
| **ServiceContainer** | Composable в platform-ui или core-ui |
| **Параметры** | `serviceId: ServiceId`, `onBack: () -> Unit`, `content: @Composable () -> Unit` |
| **Сейчас** | content = экран-заглушка от сервиса |
| **Позже** | content = NavHost с внутренними маршрутами сервиса |

### Контракт NavigableServiceProvider

Оставить гибкость для nested navigation:

```kotlin
interface NavigableServiceProvider : ServiceProvider {
    /** Регистрация графа сервиса. Сейчас — один composable, позже — nested NavHost */
    fun NavGraphBuilder.registerGraph(navController: NavController)
}
```

Сейчас `registerGraph` добавляет один `composable(route) { ServiceContainer { ServiceScreen() } }`.  
Позже можно заменить на `composable(route) { ServiceContainer { ServiceNavHost(...) } }`.

### Схема маршрутов для nested navigation

| Маршрут | Описание |
|---------|----------|
| `service/{id}` | Корень сервиса (обязательно) |
| `service/{id}/{subPath}` | Вложенные экраны (опционально, для будущего) |

Примеры для Planner:
- `service/planner` — список задач
- `service/planner/task/123` — детали задачи
- `service/planner/edit` — создание/редактирование

### Точка регистрации

При регистрации в `AppNavHost`:

```kotlin
// Вместо прямого вызова provider.registerGraph()
composable(AppRoutes.service(serviceId)) {
    ServiceContainer(
        serviceId = serviceId,
        onBack = { navController.popBackStack() },
    ) {
        // Контент от сервиса через callback или получаемый из registry
    }
}
```

Сервис должен предоставлять свой content. Варианты:
- **Вариант A:** `registerGraph` принимает `NavController` и сам рендерит `ServiceContainer` + content
- **Вариант B:** Platform рендерит `ServiceContainer`, content предоставляет сервис через новый метод `@Composable fun Content()`

**Рекомендация:** Вариант A — сервис по-прежнему полностью управляет своим экраном, но внутри `registerGraph` сначала идёт `ServiceContainer`, затем экран. В текущей реализации `registerGraph` добавляет `composable { PlannerScreen() }`. Меняем на `composable { ServiceContainer(..., onBack) { PlannerScreen() } }` — и платформа, и сервис остаются в своих границах.

### Расположение ServiceContainer

| Вариант | Плюсы | Минусы |
|---------|-------|--------|
| **core-ui** | Общий UI-компонент | core не должен знать о ServiceId |
| **platform-ui** | Логично для платформы | Новый модуль |
| **app** | Всё в одном месте | Смешивание ответственности |

**Решение:** `platform-ui` — модуль платформы с Compose-компонентами, зависящими от platform-api. Либо включить в `platform-navigation`, если он уже Android/Compose.

---

## 8. PACKAGE STRUCTURE — ЦЕЛЕВАЯ

```
ru.topskiy.superapp/
├── app/                          # app модуль
│   ├── SuperApp.kt
│   ├── MainActivity.kt
│   ├── shell/
│   │   ├── AppRoot.kt, AppShell.kt
│   │   ├── ShellViewModel.kt, ThemeViewModel.kt
│   │   ├── bootstrap/
│   │   └── onboarding/
│   └── di/
│
├── core/
│   ├── common/                   # core-common
│   ├── ui/                        # core-ui
│   ├── navigation/               # core-navigation
│   ├── database/                 # core-database
│   ├── datastore/                 # core-datastore (бывший preferences)
│   └── jobs/                     # core-jobs
│
├── platform/
│   ├── api/                      # platform-api
│   ├── runtime/                  # platform-runtime
│   ├── navigation/               # platform-navigation
│   ├── commands/                 # platform-commands
│   └── events/                   # platform-events
│
├── features/
│   ├── home/
│   ├── services/
│   ├── search/
│   └── settings/
│
└── services/
    ├── planner/
    ├── notes/
    ├── diary/
    └── finance/
```

---

## 9. ПОРЯДОК ВЫПОЛНЕНИЯ (Этап B)

1. **Создать platform-модули** (api, runtime, navigation, commands, events, **platform-ui**)
2. **Перенести содержимое** из core-services-*, core-commands, core-events
3. **Создать platform-ui** с **ServiceContainer** (оболочка для экранов сервисов)
4. **Обновить core-common** (убрать ServiceId, ServiceCategory, ServiceCapability)
5. **Создать feature-модули** (home, services, search, settings)
6. **Перенести shell screens** из app в features
7. **Переименовать feature-* в service-*** (planner, notes, finance)
8. **Создать service-diary**
9. **Интегрировать ServiceContainer** в навигацию: каждый сервис рендерится внутри ServiceContainer
10. **Подготовить nested navigation**: схема `service/{id}/{subPath}`, контракт готов к nested NavHost
11. **Обновить app** — оставить только shell, NavHost, DI
12. **Обновить settings.gradle.kts**
13. **Обновить все build.gradle.kts** (зависимости)
14. **Обновить DI** (AppModule, модули в features/services)
15. **Обновить импорты и package** во всех файлах

---

## 10. РИСКИ И СПОРНЫЕ МОМЕНТЫ

| Вопрос | Рекомендация |
|--------|--------------|
| **domain/data сейчас** | Для этапа каркаса оставить data в core (database, datastore). domain добавить при появлении бизнес-логики в сервисах |
| **core-preferences → core-datastore** | Переименовать для консистентности с data-слоем |
| **ServiceIconMapping** | Остаётся в platform-api или выносится в platform-ui (иконки по iconKey) |
| **build-logic** | Опционально на текущем этапе |
| **testing модуль** | Опционально, можно добавить позже |

---

## 11. СТАТУС РЕАЛИЗАЦИИ (Обновлено)

### Реализовано (Этап B — частично)

| Компонент | Статус | Описание |
|-----------|--------|----------|
| **ServiceContainer** | ✅ | Добавлен в `core-ui`. Единый фрейм для экранов сервисов (TopBar + content). |
| **Интеграция ServiceContainer** | ✅ | Planner, Notes, Finance — `registerGraph` оборачивает экран в ServiceContainer. |
| **ServicePlaceholderScreen.showTopBar** | ✅ | Параметр для отключения TopBar при использовании внутри ServiceContainer. |
| **service-diary** | ✅ | Новый модуль: DiaryScreen, DiaryPlugin, DiaryHomeBlock, DiarySearchProvider. |
| **Схема nested navigation** | ✅ | AppRoutes.serviceSubRoute(id, subPath) готова к использованию. |

### Ожидает реализации

- Создание platform-модулей (api, runtime, navigation, commands, events)
- Создание feature-модулей (home, services, search, settings)
- Переименование feature-* → service-* (planner, notes, finance)
- Полная реорганизация app и core

---

*Документ подготовлен для Этапа A. Частичная реализация выполнена в Этапе B.*
