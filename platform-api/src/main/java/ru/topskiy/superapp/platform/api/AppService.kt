package ru.topskiy.superapp.platform.api

interface AppService {
    suspend fun start()
    suspend fun stop()
}
