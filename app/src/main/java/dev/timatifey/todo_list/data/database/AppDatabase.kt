package dev.timatifey.todo_list.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.timatifey.todo_list.data.dao.TasksDao
import dev.timatifey.todo_list.data.entities.TaskEntity

@Database(entities = [TaskEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun tasksDao(): TasksDao

    companion object {
        const val DATABASE_NAME = "app_database"
    }
}