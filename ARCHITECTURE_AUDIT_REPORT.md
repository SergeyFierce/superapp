# Архитектурный аудит проекта SuperApp

**Дата:** 14 марта 2025  
**Методология:** Reverse engineering на основе анализа кода без предварительных предположений

---

## ЭТАП 1 — Структура проекта

### Дерево структуры

```
superapp/
├── app/                              # Основной модуль приложения
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── src/main/
│       ├── AndroidManifest.xml
│       └── java/ru/topskiy/superapp/
│           ├── MainActivity.kt
│           ├── SuperApp.kt
│           ├── di/AppModule.kt
│           └── shell/
│               ├── AppRoot.kt, AppShell.kt, ShellViewModel.kt, ThemeViewModel.kt
│               ├── home/ (HomeScreen.kt, HomeViewModel.kt)
│               ├── services/ (ServicesScreen.kt, ServicesViewModel.kt)
│               ├── settings/ (SettingsScreen.kt)
│               ├── search/ (SearchScreen.kt, SearchViewModel.kt)
│               ├── onboarding/ (OnboardingScreen.kt, OnboardingViewModel.kt)
│               └── bootstrap/ (BootstrapScreen.kt, BootstrapViewModel.kt)
│
├── core-common/                      # Общие типы, bootstrap, DI
├── core-commands/                    # Command bus (шина команд)
├── core-events/                      # Event bus (шина событий)
├── core-services-api/                # API плагинов сервисов
├── core-services/                    # DEPRECATED (только README)
├── core-services-runtime/            # ServiceManager, ServiceRegistry
├── core-services-ui/                 # Home blocks, Search, NavigableServiceProvider
├── core-database/                    # Room (placeholder)
├── core-preferences/                 # DataStore
├── core-jobs/                        # WorkManager
├── core-navigation/                  # AppRoutes, ServiceNavigator
├── core-ui/                          # Compose компоненты, тема
│
├── feature-planner/                  # Сервис «Планировщик»
├── feature-notes/                    # Сервис «Заметки»
├── feature-finance/                  # Сервис «Финансы»
│
├── gradle/libs.versions.toml
├── build.gradle.kts
└── settings.gradle.kts
```

### Модули и их build-файлы

| Модуль | Тип | Назначение |
|--------|-----|------------|
| `:app` | application | Точка входа, Shell, экраны оболочки |
| `:core-common` | Kotlin JVM | AppTheme, ServiceId, ServiceCategory, ServiceCapability, AppBootstrap |
| `:core-commands` | Kotlin JVM | CommandBus, AppCommand, CreateTaskCommand, OpenServiceCommand |
| `:core-events` | Kotlin JVM | EventBus, AppEvent, TaskCreatedEvent, ServiceOpenedEvent |
| `:core-services-api` | Kotlin JVM | ServiceDescriptor, ServiceProvider, AppService, ServicePlugin |
| `:core-services` | — | Deprecated |
| `:core-services-runtime` | Android library | ServiceManager, ServiceRegistry |
| `:core-services-ui` | Android library | HomeBlockProvider, SearchProvider, ServiceIconMapping |
| `:core-database` | Android library | AppDatabase (placeholder) |
| `:core-preferences` | Android library | PreferencesRepository |
| `:core-jobs` | Android library | ReminderWorker, JobScheduler |
| `:core-navigation` | Android library | AppRoutes, ServiceAction, ServiceNavigator |
| `:core-ui` | Android library | AppButton, AppCard, AppTopBar, AppTheme (Compose) |
| `:feature-planner` | Android library | Planner экран, API, провайдеры |
| `:feature-notes` | Android library | Notes экран |
| `:feature-finance` | Android library | Finance экран |

### Стек технологий

| Категория | Технология |
|-----------|------------|
| **Язык** | Kotlin 2.0.21 |
| **Сборка** | Gradle Kotlin DSL, AGP 9.0.1, KSP 2.0.21-1.0.28 |
| **DI** | Hilt 2.59 |
| **UI** | Jetpack Compose (BOM 2024.09.00), Material3 |
| **Навигация** | Navigation Compose 2.8.0 |
| **База данных** | Room 2.6.1 |
| **Preferences** | DataStore Preferences 1.1.1 |
| **Background** | WorkManager 2.9.1 |
| **Concurrency** | Kotlin Coroutines 1.8.1 |
| **Testing** | JUnit 4.13.2, Espresso, Compose UI Test |
| **Networking** | Не используется |
| **Сериализация** | Стандартная (нет JSON/Protobuf) |

---

## ЭТАП 2 — Reverse Engineering приложения

### Назначение приложения

**SuperApp** — модульное Android‑приложение с плагинной архитектурой сервисов. Пользователь получает единую оболочку (Shell) с главным экраном, каталогом сервисов, поиском и настройками, а сервисы (Planner, Notes, Finance) подключаются как плагины.

### Решаемые проблемы

1. Объединение нескольких функций (планирование, заметки, финансы) в одном приложении
2. Возможность включать/отключать сервисы в настройках
3. Межсервисное взаимодействие без прямых зависимостей (через CommandBus, EventBus, ServiceNavigator)
4. Единый UI и навигация для всех сервисов

### Основные пользовательские сценарии

1. **Первый запуск**: Bootstrap → Onboarding → Shell  
2. **Повторный запуск**: Bootstrap → Shell  
3. **Главная (Dashboard)**: блоки «Быстрые действия», «Ваши сервисы», «Актуальное»  
4. **Каталог сервисов**: список с переключателями вкл/выкл  
5. **Поиск**: поиск по сервисам с поддержкой SearchProvider  
6. **Настройки**: тема (светлая/тёмная/системная), управление сервисами  
7. **Planner**: создание задач, планирование напоминаний  
8. **Notes**: приём событий от Planner (TaskCreatedEvent), отображение «входящих» задач  
9. **Finance**: список транзакций (мок), кнопка «Создать задачу» → CommandBus, «Открыть Planner» → ServiceNavigator  

### Ключевые сущности

| Сущность | Модуль | Описание |
|----------|--------|----------|
| `ServicePlugin` | core-services-api | Плагин сервиса (descriptor + provider) |
| `ServiceDescriptor` | core-services-api | Метаданные: id, title, iconKey, rootRoute, category, capabilities |
| `ServiceId` | core-common | Идентификатор сервиса |
| `HomeBlockProvider` | core-services-ui | Блок на главной |
| `SearchProvider` | core-services-ui | Поставщик результатов поиска |
| `AppEvent`, `AppCommand` | core-events, core-commands | Контракты для межсервисной связи |

### Главная бизнес‑идея

**SuperApp** — это «супер‑приложение» с плагинной системой: несколько независимых сервисов живут в общей оболочке, взаимодействуют через шины (команды/события) и навигатор, не зная друг о друге напрямую.

---

## ЭТАП 3 — Анализ архитектуры

### Архитектурный стиль

- **Плагинная модульная архитектура**
- **MVVM** в экранах (ViewModel + Compose UI)
- Признаки **Clean Architecture**: разделение на слои, dependency inversion через интерфейсы
- **Feature-based** организация фич (feature-planner, feature-notes, feature-finance)

### Слои

```
┌─────────────────────────────────────────────────────────────────────┐
│  UI Layer (app shell + feature screens)                             │
│  Compose Screens, ViewModels                                        │
└─────────────────────────────────────────────────────────────────────┘
                                    │
└─────────────────────────────────────────────────────────────────────┘
│  Domain / Application Layer                                        │
│  CommandBus, EventBus, PlannerApi, UseCases (частично в API)        │
└─────────────────────────────────────────────────────────────────────┘
                                    │
┌─────────────────────────────────────────────────────────────────────┐
│  Data Layer                                                        │
│  PreferencesRepository, AppDatabase (placeholder), JobScheduler     │
└─────────────────────────────────────────────────────────────────────┘
                                    │
┌─────────────────────────────────────────────────────────────────────┐
│  Core Infrastructure                                               │
│  core-common, core-services-api, core-services-runtime             │
└─────────────────────────────────────────────────────────────────────┘
```

### Разделение обязанностей

- **app**: точка входа, Shell, bootstrap/onboarding, композиция фич  
- **core-***: инфраструктура и контракты  
- **feature-***: отдельные сервисы с UI, ViewModel, провайдерами, плагинами  

### Карта модулей по назначению

| Инфраструктура | Фичи |
|----------------|------|
| core-common, core-commands, core-events | feature-planner |
| core-services-api, core-services-runtime, core-services-ui | feature-notes |
| core-database, core-preferences, core-jobs | feature-finance |
| core-navigation, core-ui | |

---

## ЭТАП 4 — Dependency Graph

```
                                    ┌──────────────┐
                                    │     app      │
                                    └──────┬───────┘
                                           │
     ┌────────────────────────────────────┼────────────────────────────────────┐
     │                                    │                                    │
     ▼                                    ▼                                    ▼
┌─────────┐  ┌─────────┐  ┌─────────┐  ┌─────────┐  ┌─────────┐  ┌─────────┐  ┌─────────┐
│core-    │  │core-    │  │core-    │  │core-    │  │core-    │  │core-    │  │core-    │
│common   │  │commands │  │events   │  │services │  │services │  │database │  │prefs    │
└────┬────┘  └────┬────┘  └────┬────┘  │api      │  │runtime  │  └─────────┘  └────┬────┘
     │            │            │       └────┬────┘  └────┬────┘                     │
     │            │            │            │            │                          │
     │            │            └────────────┴────────────┘                          │
     │            │                     │                                           │
     │            │                     ▼                                           │
     │            │            ┌─────────────────┐                                  │
     │            │            │ core-services-ui│                                  │
     │            │            └────────┬────────┘                                  │
     │            │                     │                                            │
     │            │            ┌───────┴───────┐                                    │
     │            │            ▼               ▼                                    │
     │            │      ┌──────────┐   ┌──────────┐                                │
     │            │      │core-nav  │   │ core-ui  │                                │
     │            │      └──────────┘   └──────────┘                                │
     │            │            │               │                                      │
     └────────────┴────────────┴───────────────┴────────────────────────────────────┘
                                           │
     ┌────────────────────────────────────┼────────────────────────────────────┐
     ▼                                    ▼                                    ▼
┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐
│ feature-planner │  │ feature-notes    │  │ feature-finance  │
│ (core-cmds,     │  │ (core-events,   │  │ (core-cmds,      │
│  core-events,   │  │  core-services-*│  │  core-services-* │
│  core-jobs,     │  │  core-nav,      │  │  core-nav)       │
│  core-nav)      │  │  core-ui)       │  │                  │
└─────────────────┘  └─────────────────┘  └─────────────────┘
```

### Направление зависимостей

- **app** → все core-модули и все feature-модули  
- **feature-*** → core-модули (никогда не друг на друга)  
- **core-services-runtime** → core-services-api, core-preferences, core-common  
- **core-services-ui** → core-services-api, core-common  
- **core-navigation** → core-common  
- **core-preferences** → core-common  

### Циклические зависимости

Не обнаружены. Зависимости направлены «сверху вниз».

### Потенциальные нарушения

- `app` объединяет слишком много ответственности (Shell + DI + маршрутизация).  
- `core-services` до сих пор в `settings.gradle.kts`, хотя deprecated.

---

## ЭТАП 5 — Анализ модулей

### core-common

- **Назначение**: общие типы и контракты  
- **Содержит**: `AppTheme`, `ServiceId`, `ServiceCategory`, `ServiceCapability`, `AppBootstrap`, `ApplicationScope`  
- **Зависимости**: нет проектных  
- **Замечания**: минимальный, чистый модуль  

### core-commands

- **Назначение**: шина команд  
- **Содержит**: `CommandBus`, `AppCommand`, `CreateTaskCommand`, `OpenServiceCommand`  
- **Зависимости**: только `javax.inject` (compileOnly)  
- **Замечания**: изолирован, подходит для расширения новых команд  

### core-events

- **Назначение**: шина событий  
- **Содержит**: `EventBus`, `AppEvent`, `TaskCreatedEvent`, `ServiceOpenedEvent`  
- **Зависимости**: core-common (для `ServiceId`), coroutines  
- **Замечания**: правильное размещение событий, минимальная связанность  

### core-services-api

- **Назначение**: API для плагинов  
- **Содержит**: `ServiceDescriptor`, `ServiceProvider`, `AppService`, `ServicePlugin`  
- **Зависимости**: core-common  
- **Замечания**: чистый контракт, без реализации  

### core-services-runtime

- **Назначение**: регистрация и управление сервисами  
- **Содержит**: `ServiceManager`, `ServiceRegistry`  
- **Зависимости**: core-services-api, core-preferences, core-common  
- **Замечания**: единственная точка владения списком сервисов  

### core-services-ui

- **Назначение**: UI-контракты для сервисов  
- **Содержит**: `HomeBlockProvider`, `SearchProvider`, `SearchResult`, `NavigableServiceProvider`, `ServiceIconMapping`  
- **Зависимости**: core-services-api, core-common  
- **Замечания**: разделение UI-контрактов и runtime — корректно  

### core-database

- **Назначение**: Room-база  
- **Содержит**: `AppDatabase`, `SystemEntity` (placeholder)  
- **Зависимости**: Room, Hilt  
- **Замечания**: схема пустая; `SystemEntity` — только для Room  

### core-preferences

- **Назначение**: DataStore  
- **Содержит**: `PreferencesRepository` (тема, enabled_services, onboarding_completed)  
- **Зависимости**: core-common, DataStore, Hilt  
- **Замечания**: логика переключения сервисов реализована корректно  

### core-jobs

- **Назначение**: фоновые задачи  
- **Содержит**: `ReminderWorker`, `JobScheduler`, `ReminderJob`  
- **Зависимости**: WorkManager, Hilt  
- **Замечания**: есть TODO в `ReminderWorker` (уведомление)  

### core-navigation

- **Назначение**: маршруты и ServiceNavigator  
- **Содержит**: `AppRoutes`, `ServiceAction`, `ServiceNavigator`  
- **Зависимости**: core-common, Navigation Compose  
- **Замечания**: навигация по ServiceId абстрагирована корректно  

### core-ui

- **Назначение**: общие UI-компоненты  
- **Содержит**: `AppButton`, `AppCard`, `AppTopBar`, `AppTextField`, `FloatingBottomBar`, тема  
- **Зависимости**: core-common (api), Compose, Material3  
- **Замечания**: ui-компоненты переиспользуются  

### feature-planner

- **Назначение**: планировщик  
- **Содержит**: `PlannerScreen`, `PlannerViewModel`, `PlannerApi`, `PlannerApiImpl`, `PlannerCommandHandler`, `PlannerHomeBlock`, `PlannerSearchProvider`, `PlannerPlugin`, `PlannerBootstrap`  
- **Слои**: UI, API, impl, DI  
- **Замечания**: самый развитый feature; `PlannerHomeBlock` использует захардкоженный текст  

### feature-notes

- **Назначение**: заметки  
- **Содержит**: `NotesScreen`, `NotesViewModel`, `NotesHomeBlock`, `NotesSearchProvider`, `NotesPlugin`  
- **Замечания**: подписка на EventBus (`TaskCreatedEvent`) корректна; нет собственного хранилища  

### feature-finance

- **Назначение**: финансы  
- **Содержит**: `FinanceScreen`, `FinanceViewModel`, `FinanceHomeBlock`, `FinanceSearchProvider`, `FinancePlugin`  
- **Замечания**: транзакции захардкожены; cross-feature взаимодействие через CommandBus и ServiceNavigator реализовано правильно  

### Дублирование

- Паттерн HomeBlock/SearchProvider/Plugin повторяется в трёх фичах — допустимая повторяемость при текущем масштабе.  
- Общая логика включения/выключения сервисов может быть вынесена в общий компонент, но пока объём небольшой.  

### Сильная связанность

- `app` зависит от всех feature-модулей напрямую — при росте числа фич это может стать узким местом; имеет смысл рассмотреть динамическую загрузку (если применимо).  

---

## ЭТАП 6 — Feature‑структура

### Реализованные feature

| Feature | Входная точка | ViewModel | UseCases / API | Repository | Data sources |
|---------|---------------|-----------|----------------|------------|--------------|
| **Planner** | `PlannerPlugin` → NavHost | `PlannerViewModel` | `PlannerApi` / `PlannerApiImpl` | — | EventBus, JobScheduler |
| **Notes** | `NotesPlugin` | `NotesViewModel` | — | — | EventBus (TaskCreatedEvent) |
| **Finance** | `FinancePlugin` | `FinanceViewModel` | CommandBus, ServiceNavigator | — | — (мок) |

### Экраны

| Экран | Модуль | Описание |
|-------|--------|----------|
| Bootstrap | app | Загрузка / выбор onboarding vs shell |
| Onboarding | app | Первый запуск |
| Home | app | Dashboard с блоками |
| Services | app | Каталог сервисов |
| Search | app | Поиск по сервисам |
| Settings | app | Тема и управление сервисами |
| Planner | feature-planner | Экран планировщика |
| Notes | feature-notes | Экран заметок |
| Finance | feature-finance | Экран финансов |

### Пользовательские сценарии

1. Создать задачу в Planner → `PlannerApi.createTask` → `TaskCreatedEvent` → Notes отображает  
2. Finance «Создать задачу» → `CreateTaskCommand` → Planner → `PlannerApi.createTask` → EventBus  
3. Finance «Открыть Planner» → `ServiceNavigator.execute(OpenService(planner))`  
4. Включить/выключить сервис в Settings → `PreferencesRepository.toggleService`  

---

## ЭТАП 7 — Data flow

### Общая схема

```
UI (Compose)
    │
    ▼
ViewModel
    │
    ├─► CommandBus.dispatch()     ──► Handler (PlannerCommandHandler) ──► PlannerApi
    ├─► EventBus.publish()        ──► Подписчики (NotesViewModel)
    ├─► ServiceNavigator.execute() ──► NavController
    ├─► PlannerApi.createTask()    ──► EventBus.publish(TaskCreatedEvent)
    └─► PreferencesRepository     ──► DataStore
```

### Преобразования данных

- `PreferencesRepository`: string → `AppTheme`, Set<String> → Set<ServiceId>  
- `NotesViewModel`: `TaskCreatedEvent` → String для отображения  
- Мапперов как отдельных слоёв нет; преобразования выполняются в ViewModel/Repository  

### Кэширование

- Явного кэширования нет; DataStore и Room выступают как persistence.  

### Сохранение

- DataStore: тема, enabled_services, onboarding_completed  
- Room: пока только placeholder (`SystemEntity`)  

---

## ЭТАП 8 — Анализ базы данных

### Тип

- Room 2.6.1  

### Текущая схема

- **Таблица**: `system_table`  
- **Entity**: `SystemEntity(id: Int)` — placeholder  
- **DAO**: нет объявленных DAO  

### Миграции

- `exportSchema = false`  
- Версия 1  
- В коде упоминается необходимость Migration при добавлении entities  

### Потенциальные проблемы

- Нет реальных entities и DAO  
- `SystemEntity` временная; её нужно удалить при появлении первых доменных entities  

---

## ЭТАП 9 — Анализ качества кода

### Хорошие практики

- Модульность: фичи изолированы, зависимости направлены вниз  
- Плагинная архитектура: `ServicePlugin`, `HomeBlockProvider`, `SearchProvider`  
- Разделение ответственности между CommandBus и EventBus  
- Использование `StateFlow`/`Flow` в ViewModels  
- Комментарии на русском объясняют замысел  
- Использование `@ApplicationScope` для долгоживущих корутин  

### Проблемы

| Тип | Пример |
|-----|--------|
| Захардкоженные данные | `PlannerHomeBlock`: «Встреча с командой, отчёт…»; Finance: `listOf("Продукты -1 450 ₽", …)` |
| Антипаттерн | Finance: `viewModel.openPlanner()` — связь по имени сервиса, хоть и через ServiceNavigator |
| TODO | `ReminderWorker`: «показать системное уведомление» |
| TODO | `data_extraction_rules.xml`: backup rules |

### SOLID

- **SRP**: в целом соблюдается (модули разбиты по ответственности)  
- **OCP**: CommandBus/EventBus позволяют добавлять новые команды/события без изменения ядра  
- **LSP**: `ServicePlugin`/`ServiceProvider` используются через интерфейсы  
- **ISP**: узкие интерфейсы (`HomeBlockProvider`, `SearchProvider`)  
- **DIP**: зависимости через Hilt и абстракции (API, interfaces)  

### Возможные баги

- `PlannerApiImpl.createTask` только публикует событие и не сохраняет задачу в БД  
- Notes: нет фильтрации по query; строки `incomingTasks` отображаются как есть  

### Производительность

- `SharingStarted.Eagerly` в BootstrapViewModel — возможная лишняя подписка; можно рассмотреть `WhileSubscribed`  

---

## ЭТАП 10 — Скрытые архитектурные проблемы

1. **Утечка абстракции**: `ShellViewModel` / `HomeViewModel` зависят от `ServiceRegistry` и `ServiceCapability` — это инфраструктурные типы, используемые в UI-слое  
2. **Несогласованность слоёв**: Planner публикует `TaskCreatedEvent`, но сама задача нигде не сохраняется; фактически «создание» — только событие  
3. **core-services в settings**: deprecated модуль всё ещё в `include`, риск случайных зависимостей  
4. **Версионирование libs**: `hilt-work` и `hilt-compiler` используют `hiltNavigationCompose` (1.2.0) — обычно у Hilt Work своя версия  

---

## ЭТАП 11 — Масштабируемость

### Добавление новых фич

- Добавить feature-модуль, реализовать `ServicePlugin`, `HomeBlockProvider`, `SearchProvider` (при необходимости)  
- Подключить модуль в `app` и в DI  
- Архитектура это поддерживает  

### Добавление новых сервисов

- Реализовать `ServicePlugin` и при необходимости `AppService` для фоновой работы  
- Зарегистрировать через `@Binds @IntoSet`  
- Текущая схема позволяет масштабирование  

### Риски при росте

- Рост числа feature-модулей увеличит compile time и размер app  
- Рекомендуется: feature-on-demand, динамические модули (если применимо на платформе)  

---

## ЭТАП 12 — Технический долг

| Элемент | Файл | Описание |
|---------|------|----------|
| TODO | `ReminderWorker.kt:35` | Показать системное уведомление |
| TODO | `data_extraction_rules.xml:8` | Настройка backup rules |
| Placeholder | `AppDatabase` | Реальные entities отсутствуют |
| Placeholder | `SystemEntity` | Удалить при появлении доменных таблиц |
| Deprecated | `core-services` | Удалить из settings, перенести нужное в api/runtime |
| Захардкоженные данные | `PlannerHomeBlock`, `FinanceScreen` | Заменить на данные из БД/сети |
| build.gradle.kts (app) | `compileSdk { version = release(36) }` | Нестандартный синтаксис AGP; проверить совместимость |

---

## ЭТАП 13 — Финальный отчёт

### 1. Что представляет собой проект

**SuperApp** — модульное Android‑приложение с плагинной архитектурой. Это «супер‑приложение», объединяющее сервисы Planner, Notes и Finance в единой оболочке. Сервисы регистрируются как плагины, взаимодействуют через CommandBus, EventBus и ServiceNavigator, без прямых зависимостей друг от друга.

### 2. Реализованные функции

- Bootstrap и Onboarding при первом запуске  
- Главный экран (Dashboard) с блоками от сервисов  
- Каталог сервисов с включением/отключением  
- Поиск по сервисам  
- Настройки (тема, управление сервисами)  
- Planner: UI создания задач, API, напоминания через WorkManager  
- Notes: отображение входящих задач из EventBus  
- Finance: мок транзакций, cross-feature взаимодействие  
- Межсервисная навигация через ServiceNavigator  

### 3. Используемая архитектура

- **Плагинная модульная архитектура** с feature-модулями  
- **MVVM** на уровне экранов  
- **Шины команд и событий** для развязки фич  
- **Единый ServiceRegistry** для каталога сервисов  

### 4. Dependency graph

- `app` → все core- и feature-модули  
- `feature-*` → только core-модули  
- Циклических зависимостей нет  

### 5. Сильные стороны

- Чёткое разделение фич и инфраструктуры  
- Межсервисная связь без прямых зависимостей  
- Использование Hilt multibindings  
- Плагинная система и возможность включать/выключать сервисы  
- Комментарии и осознанная структура  

### 6. Слабые стороны

- Захардкоженные данные в Planner и Finance  
- Отсутствие persistence для задач (только события)  
- Пустая база данных (placeholder)  
- Deprecated модуль в settings  

### 7. Архитектурные проблемы

- Зависимость UI-слоя от инфраструктурных типов (`ServiceRegistry`, `ServiceCapability`)  
- Несогласованность: «создание задачи» лишь публикует событие  
- Возможная необходимость слоя domain/use case между ViewModel и API  

### 8. Потенциальные риски

- Рост числа фич без динамической загрузки увеличит размер и время сборки  
- Отсутствие persistence — риск потери данных при закрытии приложения  
- `compileSdk` в app может вызывать ошибки сборки в части конфигураций  

### 9. Рекомендации

1. **База данных**: добавить доменные entities (Task, Note, Transaction), реализовать DAO и Repository.  
2. ** persistence в Planner**: сохранять задачи в Room, публиковать события после сохранения.  
3. **Удалить deprecated**: убрать `core-services` из `settings.gradle.kts`.  
4. **Убрать захардкоженные данные**: Planner и Finance получать данные из БД/репозиториев.  
5. **Реализовать TODO**: системные уведомления в `ReminderWorker`, правила backup в `data_extraction_rules.xml`.  
6. **Слой Domain**: ввести Use Cases для Planner, Notes, Finance.  
7. **Версии Hilt**: проверить и при необходимости исправить версии `hilt-work` в `libs.versions.toml`.  
8. **compileSdk**: заменить на стандартный `compileSdk = 36` при проблемах сборки.  

---

*Отчёт подготовлен на основе анализа кода без предварительных предположений о назначении проекта.*
