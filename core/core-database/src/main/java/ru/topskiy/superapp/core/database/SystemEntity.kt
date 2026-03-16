package ru.topskiy.superapp.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Placeholder-таблица, необходимая Room при пустой схеме.
 * Удалить после добавления первой сервисной entity.
 */
@Entity(tableName = "system_table")
data class SystemEntity(
    @PrimaryKey val id: Int = 0,
)
