# Полный архитектурный аудит проекта SuperApp

**Дата проведения:** на основе анализа кодовой базы  
**Метод:** reverse-engineering, анализ исходного кода и конфигурации сборки

---

## ЭТАП 1 — ИССЛЕДОВАНИЕ ПРОЕКТА

### 1.1 Структура директорий и модули

```
superapp/
├── app/                    # Точка входа, Application, MainActivity, shell (bootstrap, onboarding, AppShell)
├── core-common/            # Общие типы: AppTheme (enum), AppBootstrap, ApplicationScope
├── core-database/          # Room: AppDatabase, SystemEntity (placeholder)
├── core-datastore/         # DataStore: PreferencesRepository, DatastoreModule
├── core-jobs/              # WorkManager: ReminderWorker, JobScheduler, AppJob
├── core-navigation/        # AppRoutes (маршруты приложения)
├── core-ui/                # Design system: компоненты, тема, токены
├── platform-api/           # Контракты: ServicePlugin, ServiceDescriptor, ServiceId, HomeBlockProvider, SearchProvider, NavigableServiceProvider
├── platform-commands/      # CommandBus, AppCommand
├── platform-events/        # EventBus, AppEvent
├── platform-runtime/       # Только build.gradle.kts — исходников нет
├── platform-navigation/    # ServiceNavigator, ServiceAction, расширения AppRoutes
├── platform-ui/            # ServiceIconMapping (String.asImageVector())
├── feature-home/           # HomeScreen, HomeViewModel
├── feature-services/       # ServicesScreen, ServicesViewModel, ServiceUiItem
├── feature-search/         # SearchScreen, SearchViewModel, SearchUiState
├── feature-settings/       # SettingsScreen
├── service-planner/        # PlannerPlugin, PlannerScreen (placeholder), HomeBlock, SearchProvider
├── service-notes/          # NotesPlugin, NotesScreen (placeholder), HomeBlock, SearchProvider
├── service-finance/        # FinancePlugin, FinanceScreen (placeholder), HomeBlock, SearchProvider
├── service-diary/          # DiaryPlugin, DiaryScreen (placeholder), HomeBlock, SearchProvider
├── gradle/
│   └── libs.versions.toml
├── settings.gradle.kts
└── build.gradle.kts
```

**Пакеты (основные):**

- `ru.topskiy.superapp` — app (MainActivity, SuperApp, shell, di)
- `ru.topskiy.superapp.core.common` — AppTheme (enum)
- `ru.topskiy.superapp.core.bootstrap` — AppBootstrap
- `ru.topskiy.superapp.core.di` — ApplicationScope
- `ru.topskiy.superapp.core.datastore` — PreferencesRepository
- `ru.topskiy.superapp.core.navigation` — AppRoutes
- `ru.topskiy.superapp.core.ui` — компоненты, тема, токены
- `ru.topskiy.superapp.core.database` — AppDatabase, SystemEntity
- `ru.topskiy.superapp.core.jobs` — ReminderWorker, JobScheduler
- `ru.topskiy.superapp.platform.api` — контракты сервисов
- `ru.topskiy.superapp.platform.runtime` — **типы импортируются, исходников в репозитории нет**
- `ru.topskiy.superapp.platform.navigation` — ServiceNavigator, ServiceAction, AppRoutesExtensions
- `ru.topskiy.superapp.platform.commands` — CommandBus
- `ru.topskiy.superapp.platform.events` — EventBus
- `ru.topskiy.superapp.platform.ui` — ServiceIconMapping
- `ru.topskiy.superapp.features.home|services|search|settings` — экраны оболочки
- `ru.topskiy.superapp.services.planner|notes|finance|diary` — плагины сервисов

### 1.2 Стек технологий

| Категория | Технология | Подтверждение |
|-----------|------------|---------------|
| Язык | Kotlin 2.0.21 | [gradle/libs.versions.toml](gradle/libs.versions.toml), build.gradle.kts |
| Сборка | Gradle (Kotlin DSL), AGP 9.0.1 | settings.gradle.kts, libs.versions.toml |
| UI | Jetpack Compose (BOM 2024.09), Material 3 | libs.versions.toml, app/build.gradle.kts |
| Навигация | Navigation Compose 2.8.0 | libs.versions.toml, core-navigation, AppShell |
| DI | Hilt 2.59, KSP | libs.versions.toml, @HiltViewModel, @Inject, AppModule |
| Локальное хранилище | DataStore Preferences 1.1.1 | core-datastore, PreferencesRepository |
| База данных | Room 2.6.1 | core-database, AppDatabase |
| Фоновые задачи | WorkManager 2.9.1, Hilt Work | core-jobs, ReminderWorker, SuperApp |
| Конкуррентность | Kotlin Coroutines 1.8.1 | Flow, stateIn, viewModelScope в ViewModel'ах |
| Сеть | Не используется | Нет зависимостей retrofit/okhttp/ktor |
| Сериализация | Нет отдельной библиотеки | Только Preferences (ключи/значения) |
| Тестирование | JUnit 4, AndroidX Test, Espresso, Compose UI Test | app/build.gradle.kts (testImplementation, androidTestImplementation) |

---

## ЭТАП 2 — REVERSE ENGINEERING ПРИЛОЖЕНИЯ

### 2.1 Назначение приложения

По коду это **супер-приложение (super app)** или **личный ассистент**: единая оболочка с нижней навигацией (Главная, Сервисы, Поиск, Настройки), внутри которой подключаются **модули-сервисы** (Планировщик, Заметки, Финансы, Дневник). Пользователь может включать/выключать сервисы, видеть блоки на главной и искать по всем сервисам.

### 2.2 Решаемые проблемы

- Единая точка входа для нескольких функциональных модулей (планировщик, заметки, финансы, дневник).
- Динамический каталог сервисов с включением/выключением через настройки.
- Единый поиск по всем включённым сервисам.
- Общая тема (светлая/тёмная/системная) и онбординг при первом запуске.
- Расширяемость за счёт регистрации плагинов (ServicePlugin) через DI.

### 2.3 Пользовательские сценарии (по коду)

1. **Первый запуск:** Bootstrap → проверка onboardingCompleted → Onboarding → по завершении Shell.
2. **Повторный запуск:** Bootstrap → Shell (главная вкладка).
3. **Главная:** блоки от сервисов (HomeBlockProvider), фильтрация по включённым сервисам и capability HOME_BLOCK; переход по клику на rootRoute сервиса.
4. **Сервисы:** список всех провайдеров с включённостью из DataStore; переход в сервис по rootRoute.
5. **Поиск:** ввод запроса → debounce → вызов search() у всех включённых SearchProvider; отображение результатов по сервисам; переход по result.route.
6. **Настройки:** смена темы (AppTheme), заглушки разделов (Services, Notifications, About).
7. **Открытие сервиса:** маршрут `service/{id}` → ServiceContainer (TopBar + кнопка «Назад») → экран сервиса (сейчас везде placeholder).

### 2.4 Ключевые сущности

- **ServiceId** — идентификатор сервиса (value class над String).
- **ServiceDescriptor** — id, title, description, iconKey, rootRoute, category, enabledByDefault, capabilities.
- **ServicePlugin** — descriptor + serviceProvider (точка регистрации в DI).
- **ServiceProvider** / **NavigableServiceProvider** — регистрация графа в NavHost.
- **HomeBlockProvider** — блок на главной (serviceId, priority, Content).
- **SearchProvider** — search(query): List<SearchResult>.
- **PreferencesRepository** — тема, enabledServiceIds, onboardingCompleted (DataStore).
- **AppShellState** — проекция Preferences (theme, enabledServiceIds, onboardingCompleted).

### 2.5 Бизнес-идея

Единое приложение-оболочка с набором подключаемых «сервисов» (продуктивность, финансы, здоровье и т.д.) с общим UX: главная с блоками, каталог сервисов, глобальный поиск и настройки. Сейчас сервисы представлены заглушками; каркас рассчитан на последующую реализацию логики в каждом модуле.

---

## ЭТАП 3 — АНАЛИЗ АРХИТЕКТУРЫ

### 3.1 Стиль

- **Модульная архитектура** (многомодульный Gradle).
- **Разделение по слоям:** app (точка входа + shell), **features** (экраны оболочки), **services** (модули-плагины), **platform** (контракты и инфраструктура платформы), **core** (общая инфраструктура).
- **MVVM** в экранах: Composable + hiltViewModel() + ViewModel с StateFlow/Flow.
- Элементы **event-driven**: CommandBus, EventBus для взаимодействия сервисов без прямых зависимостей.

### 3.2 Слои и обязанности

| Слой | Модули | Назначение |
|------|--------|------------|
| **App** | app | Application, MainActivity, AppRoot (bootstrap/onboarding/shell), AppShell (NavHost + bottom bar), DI (AppModule). Не содержит экранов оболочки — они в feature-модулях. |
| **Features** | feature-home, feature-services, feature-search, feature-settings | Экраны оболочки: главная, каталог сервисов, поиск, настройки. Зависят от platform и core. |
| **Services** | service-planner, service-notes, service-finance, service-diary | Плагины: ServicePlugin + NavigableServiceProvider, при необходимости HomeBlockProvider, SearchProvider. Реализация экранов пока — placeholder. |
| **Platform** | platform-api, platform-commands, platform-events, platform-navigation, platform-ui, platform-runtime | Контракты (api), шины (commands, events), навигация сервисов, иконки; runtime — регистрация/жизненный цикл сервисов (в коде отсутствует). |
| **Core** | core-common, core-ui, core-navigation, core-datastore, core-database, core-jobs | Общие типы, дизайн-система, маршруты, настройки (DataStore), БД (Room), фоновые задачи (WorkManager). |

### 3.3 Логическая карта

```
                    ┌─────────────────────────────────────────────────────────┐
                    │                         app                               │
                    │  MainActivity, SuperApp, AppRoot, AppShell, DI            │
                    └───────────────────────────┬─────────────────────────────┘
                                                │
        ┌───────────────────────────────────────┼───────────────────────────────────────┐
        │                                       │                                       │
        ▼                                       ▼                                       ▼
┌───────────────┐                   ┌─────────────────────┐                   ┌─────────────────────┐
│   features    │                   │      platform       │                   │      services       │
│ home,services │                   │ api,runtime,nav,    │                   │ planner,notes,      │
│ search,       │                   │ commands,events,ui  │                   │ finance,diary       │
│ settings      │                   └──────────┬──────────┘                   └──────────┬──────────┘
└───────┬───────┘                                │                                        │
        │                                        │                                        │
        │                              ┌─────────┴─────────┐                              │
        └─────────────────────────────►     core         ◄──────────────────────────────┘
                                        common,ui,navigation,
                                        datastore,database,jobs
```

---

## ЭТАП 4 — DEPENDENCY GRAPH МОДУЛЕЙ

Зависимости выведены из `implementation(project(...))` в build.gradle.kts (направление стрелки: «модуль → от кого зависит»).

```
app
  → core-common, core-database, core-datastore, core-jobs, core-navigation, core-ui
  → platform-api, platform-commands, platform-events, platform-runtime, platform-navigation, platform-ui
  → feature-home, feature-services, feature-search, feature-settings
  → service-planner, service-notes, service-finance, service-diary

feature-home
  → core-common, core-datastore, core-navigation, core-ui, platform-api, platform-runtime

feature-services
  → core-common, core-datastore, core-navigation, core-ui, platform-api, platform-runtime, platform-ui

feature-search
  → core-common, core-datastore, core-navigation, core-ui, platform-api, platform-runtime

feature-settings
  → core-common, core-datastore, core-navigation, core-ui
  (без platform-runtime и Hilt — только UI и маршруты)

service-planner
  → core-common, core-navigation, core-ui, platform-api, platform-commands, platform-navigation

service-notes, service-finance, service-diary
  → core-common, core-navigation, core-ui, platform-api, platform-navigation
  (service-diary без platform-commands)

platform-api
  → (только Kotlin/AndroidX: coroutines, navigation-compose)

platform-runtime
  → platform-api, core-common, core-datastore

platform-navigation
  → platform-api, core-navigation

platform-ui
  → core-ui

platform-commands
  → (только kotlinx-coroutines-core)

platform-events
  → platform-api, kotlinx-coroutines-core

core-datastore
  → core-common, platform-api

core-navigation
  → core-common

core-ui
  → core-common (api)

core-database
  → (Room, Hilt)

core-jobs
  → (WorkManager, Hilt)

core-common
  → (только javax.inject)
```

**Циклов зависимостей нет.**  
**Нарушения:** feature-модули и app зависят от **platform-runtime**, в котором в репозитории **нет исходного кода** (нет ServiceRegistry, ServiceManager) — сборка без их реализации невозможна.

---

## ЭТАП 5 — АНАЛИЗ МОДУЛЕЙ

Кратко по назначению и связям.

| Модуль | Назначение | Замечания |
|--------|------------|-----------|
| **app** | Точка входа, составление графа навигации (bootstrap → onboarding → shell), инжект ServiceRegistry/ServiceNavigator в MainActivity/AppRoot. | Shell-экраны вынесены в feature-модули; AppShell собирает NavHost из feature- и service-графов. |
| **core-common** | AppTheme (enum), AppBootstrap, ApplicationScope. | Минимальные зависимости (javax.inject). |
| **core-datastore** | PreferencesRepository (DataStore), DatastoreModule (Hilt). | Зависит от platform-api (ServiceId). |
| **core-navigation** | AppRoutes (константы и serviceRoute/serviceSubRoute). | Нет зависимости от platform. |
| **core-ui** | Компоненты (AppCard, AppTopBar, ServiceContainer, ServicePlaceholderScreen, FloatingBottomBar, AppTextField и др.), тема (AppTheme Composable, ColorScheme, Typography, Shapes), токены (Spacing, Radius, Elevation). | api(core-common) — корректно. |
| **core-database** | AppDatabase (Room), SystemEntity (placeholder), DatabaseModule. | Нет зависимостей от других модулей проекта. |
| **core-jobs** | ReminderWorker (HiltWorker), JobScheduler, AppJob. | Только WorkManager/Hilt. |
| **platform-api** | ServiceId, ServiceCategory, ServiceCapability, ServiceDescriptor, ServicePlugin, ServiceProvider, NavigableServiceProvider, HomeBlockProvider, SearchProvider, SearchResult. | Чистые контракты + navigation-compose для NavGraphBuilder. |
| **platform-runtime** | По замыслу — ServiceRegistry, ServiceManager. | **В репозитории нет ни одного .kt файла** — только build.gradle.kts. Критический пробел. |
| **platform-navigation** | ServiceNavigator (attach/detach NavController, execute(ServiceAction)), ServiceAction (OpenService, Navigate, GoBack), AppRoutes.service(ServiceId). | Зависит от core-navigation и platform-api. |
| **platform-commands** | CommandBus (SharedFlow<AppCommand>). | JVM-библиотека, без Android. |
| **platform-events** | EventBus (SharedFlow<AppEvent>). | Зависит от platform-api (события с ServiceId). |
| **platform-ui** | ServiceIconMapping: String.asImageVector(). | Зависит от core-ui. |
| **feature-home** | HomeScreen, HomeViewModel; список блоков из HomeBlockProvider с учётом enabledServiceIds и capability. | Использует ServiceRegistry (отсутствует). |
| **feature-services** | ServicesScreen, ServicesViewModel; список сервисов из ServiceRegistry, переход по rootRoute. | То же. |
| **feature-search** | SearchScreen, SearchViewModel; запрос к SearchProvider, группировка по сервисам. | То же. |
| **feature-settings** | SettingsScreen; тема (AppTheme), заглушки разделов. | Без Hilt и platform-runtime. |
| **service-planner/notes/finance/diary** | ServicePlugin + NavigableServiceProvider, при необходимости HomeBlockProvider, SearchProvider; экраны — ServicePlaceholderScreen внутри ServiceContainer. | Регистрация в Hilt через @IntoSet; граф регистрируется в AppShell через provider.registerGraph(). |

**Дублирование:** повторяющийся паттерн «Plugin + ServiceProvider + descriptor + registerGraph(ServiceContainer { PlaceholderScreen })» в четырёх сервисах — нормально для плагинной модели.  
**Границы модулей:** features не зависят от services напрямую; связь только через platform (ServiceRegistry, контракты).  
**Связанность:** app и feature-модули жёстко зависят от platform-runtime, реализация которого отсутствует.

---

## ЭТАП 6 — FEATURE СТРУКТУРА

### 6.1 Реализованные фичи оболочки

| Feature | Вход | ViewModel | Use case / данные | Экраны |
|---------|------|-----------|-------------------|--------|
| **Home** | AppRoutes.HOME | HomeViewModel | homeBlockProviders (Set), serviceRegistry, preferences.enabledServiceIds → blocks (StateFlow); routeFor(serviceId) | HomeScreen (LazyColumn: блоки, секции) |
| **Services** | AppRoutes.SERVICES | ServicesViewModel | serviceRegistry.getAllProviders(), preferences.enabledServiceIds → services (StateFlow<ServiceUiItem>); toggleService | ServicesScreen (LazyColumn карточек сервисов) |
| **Search** | AppRoutes.SEARCH | SearchViewModel | searchProviders, serviceRegistry, preferences; queryFlow → debounce → performSearch → state (SearchUiState) | SearchScreen (поле ввода, результаты по сервисам) |
| **Settings** | AppRoutes.SETTINGS | — | Параметры: theme, onThemeChange (из ShellViewModel) | SettingsScreen (тема, заглушки разделов) |

Отдельных UseCase-слоёв нет: логика в ViewModel; данные — PreferencesRepository и (по замыслу) ServiceRegistry.

### 6.2 Сервисы как «фичи»

Каждый сервис (planner, notes, finance, diary):

- **Вход:** маршрут `service/{id}`, регистрация в NavHost через NavigableServiceProvider.registerGraph().
- **ViewModel:** в текущей реализации не используются (экраны — заглушки).
- **Repository/DataSource:** не представлены; AppDatabase — общий placeholder.
- **Экран:** ServicePlaceholderScreen внутри ServiceContainer.

То есть сценарии «открыть сервис → увидеть заглушку → назад» реализованы; сценарии с реальными данными и бизнес-логикой — нет.

---

## ЭТАП 7 — DATA FLOW

### 7.1 Оболочка (shell/features)

- **Тема:** MainActivity (ThemeViewModel.theme) → AppTheme(themeSelection) → MaterialTheme.  
  ThemeViewModel → PreferencesRepository.theme (Flow) → stateIn.  
  Запись: SettingsScreen onThemeChange → ShellViewModel.setTheme → preferences.setTheme().
- **Включённые сервисы:** PreferencesRepository.enabledServiceIds (Flow) → HomeViewModel/ServicesViewModel/SearchViewModel (фильтрация блоков/списка/провайдеров поиска).  
  Запись: (в коде настроек переключение сервисов не привязано к UI; метод toggleService есть в PreferencesRepository и вызывается из ServicesViewModel).
- **Онбординг:** PreferencesRepository.onboardingCompleted → BootstrapViewModel.destination → навигация на Onboarding или Shell.  
  Запись: OnboardingViewModel → preferences.setOnboardingCompleted().

Итого: **UI → ViewModel → PreferencesRepository (DataStore)**; маппингов и кэша нет, только чтение/запись ключей.

### 7.2 Сервисы и платформа

- **Список сервисов для оболочки:** по задумке — **ServiceRegistry.getAllProviders()** / getService(id). В коде эти типы из platform-runtime не реализованы.
- **Поиск:** SearchViewModel вызывает provider.search(query) у каждого включённого SearchProvider; результаты объединяются и отображаются. Преобразований данных нет.
- **Навигация между сервисами:** ServiceNavigator.execute(ServiceAction) → NavController.navigate / popBackStack. NavController передаётся из AppShell через attach().

Кэширование и отдельный data-слой для сервисов в коде не прослеживаются.

---

## ЭТАП 8 — БАЗА ДАННЫХ

- **Тип:** Room (androidx.room 2.6.1).
- **Модуль:** core-database.
- **Классы:** [core-database/AppDatabase.kt](core-database/src/main/java/ru/topskiy/superapp/core/database/AppDatabase.kt) — @Database(entities = [SystemEntity::class], version = 1, exportSchema = false); [SystemEntity.kt](core-database/src/main/java/ru/topskiy/superapp/core/database/SystemEntity.kt) — одна placeholder-таблица (id: Int) для удовлетворения Room.
- **DAO:** в коде не объявлены (только комментарий в AppDatabase о добавлении entities и dao).
- **Миграции:** не используются (exportSchema = false).
- **Использование:** ни один модуль в текущем дереве не инжектирует AppDatabase или DAO; сервисы и features с данными не реализованы.

**Вывод:** БД зарезервирована под будущие entity сервисов; реальной схемы и миграций нет.

---

## ЭТАП 9 — КАЧЕСТВО КОДА

### 9.1 Плюсы

- Чёткое разделение на app / features / services / platform / core.
- Контракты (platform-api) не зависят от реализации сервисов.
- Единая точка маршрутов (AppRoutes), расширения в platform-navigation.
- DI через Hilt и multibindings (Set<ServicePlugin>, Set<HomeBlockProvider>, Set<SearchProvider>) без жёсткой привязки app к конкретным сервисам.
- Композиция UI (ServiceContainer, ServicePlaceholderScreen) переиспользуется.
- Использование StateFlow/Flow и stateIn для реактивного UI.
- Комментарии в ключевых местах (русский язык).

### 9.2 Проблемы

- **Отсутствует реализация platform-runtime:** ServiceRegistry и ServiceManager нигде не объявлены; проект не соберётся без их добавления.
- **Дублирование в platform-runtime build:** в [platform-runtime/build.gradle.kts](platform-runtime/build.gradle.kts) дважды указан core-common.
- **Нет явного domain/data слоёв:** сценарии и репозитории сервисов не выделены; при росте логики возможна «толстая» ViewModel и смешение слоёв.
- **Один TODO в коде:** [ReminderWorker.kt](core-jobs/src/main/java/ru/topskiy/superapp/core/jobs/ReminderWorker.kt) — «показать системное уведомление».
- **Два типа с именем AppTheme:** core.common.AppTheme (enum) и core.ui.theme.AppTheme (Composable); в theme используется alias — ок, но имя совпадает.
- **Тесты:** только ExampleUnitTest и ExampleInstrumentedTest; бизнес-логика и навигация не покрыты.

---

## ЭТАП 10 — СКРЫТЫЕ АРХИТЕКТУРНЫЕ ПРОБЛЕМЫ

1. **Отсутствующий platform-runtime:** Все ссылки на ServiceRegistry/ServiceManager ведут в пустой модуль. Это не «скрытая» зависимость, а провал сборки и дизайна: без реализаций регистрации и жизненного цикла сервисов оболочка неработоспособна.
2. **Core-datastore зависит от platform-api:** PreferencesRepository оперирует ServiceId (platform.api). Для «чистого» core слой настроек оболочки оказывается завязан на платформенный тип; при переносе core в другой проект понадобится либо дублировать тип, либо выносить его в ещё более общий модуль.
3. **Нет абстракции «источник списка сервисов»:** Features напрямую зависят от platform-runtime (ServiceRegistry). Подмена реализации (например, для тестов или другой стратегии регистрации) возможна только подменой модуля.
4. **Риск «толстого» AppShell:** Регистрация графов сервисов в одном месте (getAllProviders().filterIsInstance<NavigableServiceProvider>().forEach { registerGraph }) при большом числе сервисов может разрастаться; альтернативы (например, регистрация через конвенции или отдельный навигационный модуль) не заложены.
5. **Супер-приложение без реальных сервисов:** Все четыре сервиса — заглушки. Архитектура рассчитана на рост, но пока нет примера полного цикла (данные → репозиторий → use case → ViewModel → UI), по которому можно валидировать границы модулей и поток данных.

---

## ЭТАП 11 — МАСШТАБИРУЕМОСТЬ

- **Добавление нового сервиса:** создать модуль по образцу service-diary/planner, реализовать ServicePlugin + при необходимости HomeBlockProvider/SearchProvider, зарегистрировать в DI и добавить зависимость в app. Маршруты и иконки (platform-ui) расширяются одним местом. Масштабируемость хорошая при условии появления platform-runtime.
- **Добавление нового экрана оболочки:** новый feature-модуль + регистрация в AppShell и нижней панели. Ясно и предсказуемо.
- **Рост проекта:** разделение на core/platform/features/services выдержано; риск — накопление логики в ViewModel при отсутствии domain/data и зависимость всего от одной реализации ServiceRegistry/ServiceManager.

---

## ЭТАП 12 — ТЕХНИЧЕСКИЙ ДОЛГ

- **Критично:** реализовать в **platform-runtime** классы **ServiceRegistry** и **ServiceManager** (регистрация Set<ServicePlugin> из DI, предоставление списка провайдеров/ getService(id)), иначе сборка невозможна.
- **TODO в коде:** ReminderWorker — показ системного уведомления.
- **Незавершённое:** все четыре сервиса — заглушки; нет доменной логики, репозиториев, использования AppDatabase.
- **Недостающие слои:** явный domain (use cases, модели) и data (репозитории, источники) для сервисов не выделены.
- **Тесты:** только шаблонные; нет unit-тестов для ViewModel, PreferencesRepository, навигации, поиска.
- **Документация:** ARCHITECTURE_REORGANIZATION_PLAN.md и REORGANIZATION_STATUS.md описывают целевую архитектуру и статус; актуальный аудит и граф зависимостей до этого отчёта в одном месте не сводились.

---

## ЭТАП 13 — ФИНАЛЬНЫЙ ОТЧЕТ

### 1. Что представляет собой проект

**SuperApp** — Android-приложение-оболочка (супер-приложение / личный ассистент) с нижней навигацией (Главная, Сервисы, Поиск, Настройки) и подключаемыми модулями-сервисами (Планировщик, Заметки, Финансы, Дневник). Реализованы: первый/повторный запуск (bootstrap, onboarding), настройка темы и списка включённых сервисов (DataStore), заглушки экранов сервисов в общей оболочке (ServiceContainer). Регистрация сервисов задумана через Hilt (Set<ServicePlugin>) и runtime (ServiceRegistry/ServiceManager), но **реализация platform-runtime в репозитории отсутствует**.

### 2. Реализованные функции

- Запуск и маршрутизация: bootstrap → onboarding или shell.
- Shell: 4 вкладки (Home, Services, Search, Settings).
- Главная: блоки от сервисов (при наличии ServiceRegistry), переход в сервис по маршруту.
- Сервисы: список сервисов (при наличии ServiceRegistry), переход в сервис.
- Поиск: ввод запроса, вызов SearchProvider у включённых сервисов, отображение результатов.
- Настройки: выбор темы (светлая/тёмная/системная).
- Открытие сервиса по маршруту `service/{id}` с ServiceContainer и placeholder-экраном.
- Сохранение темы, включённых сервисов и флага онбординга в DataStore.
- Заготовка БД (Room) и фоновых задач (WorkManager, ReminderWorker).

### 3. Архитектура

Модульная: **app** (точка входа + shell) → **features** (экраны оболочки) и **services** (плагины) → **platform** (контракты, шины, навигация, иконки, runtime) → **core** (общие типы, UI, навигация, datastore, database, jobs). MVVM в экранах; взаимодействие сервисов через CommandBus/EventBus и навигацию через ServiceNavigator.

### 4. Dependency graph (кратко)

- **app** → все core, все platform, все feature, все service.
- **feature-*** → core-common, core-datastore, core-navigation, core-ui, platform-api; feature-home/services/search ещё platform-runtime; feature-services ещё platform-ui.
- **service-*** → core-common, core-navigation, core-ui, platform-api, platform-navigation; service-planner ещё platform-commands.
- **platform-runtime** → platform-api, core-common, core-datastore.
- **platform-navigation** → platform-api, core-navigation.
- **platform-ui** → core-ui.
- **core-datastore** → core-common, platform-api.
- **core-ui** → core-common (api).
- **core-navigation** → core-common.
- Циклов нет. Исходников в **platform-runtime** нет.

### 5. Сильные стороны

- Чёткое разделение модулей и слоёв (app/features/services/platform/core).
- Контракты в platform-api, расширяемость через плагины и multibindings.
- Единые маршруты (AppRoutes) и переиспользуемые UI-компоненты (ServiceContainer, placeholder).
- Реактивный поток настроек (DataStore → ViewModel → UI).
- Подготовка к нескольким сервисам и к Room/WorkManager.

### 6. Слабые стороны

- **Нет реализации ServiceRegistry и ServiceManager** — сборка и запуск оболочки в текущем виде невозможны.
- Нет domain/data слоёв для сервисов; сценарии и репозитории не выделены.
- Сервисы только в виде заглушек; нет примера полного цикла данных.
- Минимальное тестовое покрытие; нет unit-тестов на логику и навигацию.
- Дублирование зависимости core-common в platform-runtime.

### 7. Архитектурные проблемы

- Критическое: зависимость app и трёх feature-модулей от несуществующей реализации platform-runtime.
- Связь core-datastore с platform-api (ServiceId) размывает границу «чистого» core.
- Отсутствие абстракции над «источником списка сервисов» затрудняет тестирование и смену реализации.

### 8. Риски

- Невозможность сборки до появления ServiceRegistry и ServiceManager.
- При добавлении реальной логики в сервисы без domain/data — раздувание ViewModel и смешение слоёв.
- Рост количества сервисов и логики в AppShell без рефакторинга может усложнить поддержку навигации.

### 9. Рекомендации

1. **Обязательно:** реализовать в **platform-runtime** (или перенести в другой модуль с сохранением пакета `ru.topskiy.superapp.platform.runtime`):
   - **ServiceRegistry:** приём Set<ServicePlugin> (через конструктор или Hilt), методы getAllProviders(), getService(ServiceId).
   - **ServiceManager:** жизненный цикл (например, инициализация при старте приложения, опционально — учёт enabledServiceIds из PreferencesRepository). Подключить в SuperApp и DI.
2. Убрать дублирование зависимости core-common в platform-runtime/build.gradle.kts.
3. Ввести для сервисов слой domain (use cases / интерфейсы репозиториев) и data (реализации, DAO при использовании Room); начать с одного сервиса (например, planner) как эталон.
4. Добавить unit-тесты для ViewModel (Home, Services, Search), PreferencesRepository и (после появления) ServiceRegistry.
5. Рассмотреть вынос типа «идентификатор сервиса» в core (или общий api) при желании сохранить core без зависимостей от platform; либо явно зафиксировать, что core-datastore — часть «оболочки», зависящей от platform.
6. Реализовать отображение уведомлений в ReminderWorker и закрыть TODO.
7. По мере реализации сервисов: использовать AppDatabase, добавить миграции и exportSchema = true для Room.

---

*Отчёт составлен на основе анализа репозитория (структура, build-файлы, исходный код). Выводы опираются только на присутствующий в репозитории код.*
