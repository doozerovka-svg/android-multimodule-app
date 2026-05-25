package com.example.multimodule.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.multimodule.core.database.dao.TaskDao
import com.example.multimodule.core.database.dao.TaskListDao
import com.example.multimodule.core.database.dao.UserDao
import com.example.multimodule.core.database.entity.TaskEntity
import com.example.multimodule.core.database.entity.TaskListEntity
import com.example.multimodule.core.database.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        TaskListEntity::class,
        TaskEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun taskListDao(): TaskListDao
    abstract fun taskDao(): TaskDao
}
