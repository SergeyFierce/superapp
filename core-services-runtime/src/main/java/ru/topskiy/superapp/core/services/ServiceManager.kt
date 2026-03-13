package ru.topskiy.superapp.core.services

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.topskiy.superapp.core.di.ApplicationScope
import ru.topskiy.superapp.core.preferences.PreferencesRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Менеджер жизненного цикла background-сервисов.
 *
 * Получает провайдеры через [ServiceRegistry] (единственный источник истины),
 * подписывается на [PreferencesRepository.enabledServiceIds] и управляет
 * lifecycle [AppService] для сервисов с [ServiceCapability.BACKGROUND_SERVICE].
 */
@Singleton
class ServiceManager @Inject constructor(
    private val registry: ServiceRegistry,
    private val preferences: PreferencesRepository,
    @ApplicationScope private val scope: CoroutineScope,
) {
    private val running = mutableMapOf<ServiceId, AppService>()

    init {
        scope.launch {
            preferences.enabledServiceIds.collect { storedIds ->
                reconcile(resolveEffectiveIds(storedIds))
            }
        }
    }

    // ── Приватные методы ───────────────────────────────────────────────

    private fun resolveEffectiveIds(stored: Set<ServiceId>): Set<ServiceId> =
        if (stored.isEmpty()) {
            registry.getServiceProviders()
                .filter { it.descriptor.enabledByDefault }
                .map { it.descriptor.id }
                .toSet()
        } else {
            stored
        }

    private suspend fun reconcile(newEnabledIds: Set<ServiceId>) {
        val currentIds = running.keys.toSet()
        val toStart = newEnabledIds - currentIds
        val toStop  = currentIds - newEnabledIds

        toStop.forEach { id ->
            val service = running.remove(id) ?: return@forEach
            runCatching { service.stop() }
                .onFailure { Log.e(TAG, "Ошибка остановки сервиса $id", it) }
            Log.d(TAG, "Остановлен: $id")
        }

        toStart.forEach { id ->
            val provider = registry.getService(id) ?: return@forEach

            // Запускаем background-часть только если сервис заявил возможность
            if (!provider.descriptor.supports(ServiceCapability.BACKGROUND_SERVICE)) return@forEach

            val service = provider.createService() ?: return@forEach
            running[id] = service
            runCatching { service.start() }
                .onFailure { Log.e(TAG, "Ошибка запуска сервиса $id", it) }
            Log.d(TAG, "Запущен: $id")
        }
    }

    private companion object {
        const val TAG = "ServiceManager"
    }
}

