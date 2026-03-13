package ru.topskiy.superapp.core.database

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Центральная база данных приложения.
 *
 * Сейчас таблиц нет — это архитектурный placeholder.
 * Каждый сервис будет добавлять свои @Entity классы сюда:
 *
 *   @Database(
 *       entities = [TaskEntity::class, NoteEntity::class, ...],
 *       version = 2
 *   )
 *
 * При добавлении новых entities нужно:
 * 1. Добавить @Entity в список entities
 * 2. Добавить абстрактный fun dao(): SomeDao
 * 3. Написать Migration (или увеличить version + fallbackToDestructiveMigration для dev)
 */
@Database(
    entities = [SystemEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase()
