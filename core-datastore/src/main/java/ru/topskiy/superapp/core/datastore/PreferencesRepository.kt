package ru.topskiy.superapp.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.topskiy.superapp.core.common.AppTheme
import ru.topskiy.superapp.platform.api.ServiceId
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Репозиторий системных настроек приложения.
 *
 * Хранит только инфраструктурные данные оболочки:
 * - тема оформления
 * - список включённых сервисов
 * - завершённость онбординга
 *
 * Бизнес-данные сервисов хранятся в AppDatabase, а не здесь.
 */
@Singleton
class PreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    // ── Ключи ──────────────────────────────────────────────────────────
    private object Keys {
        val THEME = stringPreferencesKey("theme")
        val ENABLED_SERVICES = stringSetPreferencesKey("enabled_services")
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
    }

    // ── Reads ──────────────────────────────────────────────────────────

    val theme: Flow<AppTheme> = dataStore.data.map { prefs ->
        prefs[Keys.THEME]?.let { runCatching { AppTheme.valueOf(it) }.getOrNull() }
            ?: AppTheme.SYSTEM
    }

    /**
     * Пустое множество = пользователь ещё не менял настройки.
     * Система использует [ServiceDescriptor.enabledByDefault] в этом случае.
     */
    val enabledServiceIds: Flow<Set<ServiceId>> = dataStore.data.map { prefs ->
        prefs[Keys.ENABLED_SERVICES]?.map { ServiceId(it) }?.toSet() ?: emptySet()
    }

    val onboardingCompleted: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[Keys.ONBOARDING_COMPLETED] ?: false
    }

    // ── Writes ─────────────────────────────────────────────────────────

    suspend fun setTheme(theme: AppTheme) {
        dataStore.edit { it[Keys.THEME] = theme.name }
    }

    suspend fun setOnboardingCompleted() {
        dataStore.edit { it[Keys.ONBOARDING_COMPLETED] = true }
    }

    /**
     * Переключить сервис.
     *
     * При первом вызове (DataStore пуст) инициализируем набор из
     * [allServiceIds] — все сервисы считаются включёнными, — затем
     * применяем переключение. Так пустое множество всегда означает
     * «не настроено», а не «все отключены».
     */
    suspend fun toggleService(
        serviceId: ServiceId,
        enabled: Boolean,
        allServiceIds: Set<ServiceId>,
    ) {
        dataStore.edit { prefs ->
            val stored = prefs[Keys.ENABLED_SERVICES]
            val current: MutableSet<String> = if (stored.isNullOrEmpty()) {
                allServiceIds.map { it.value }.toMutableSet()
            } else {
                stored.toMutableSet()
            }

            // Нельзя выключить последний включённый сервис
            if (!enabled && current.size == 1 && serviceId.value in current) return@edit

            if (enabled) current.add(serviceId.value) else current.remove(serviceId.value)
            prefs[Keys.ENABLED_SERVICES] = current
        }
    }
}
