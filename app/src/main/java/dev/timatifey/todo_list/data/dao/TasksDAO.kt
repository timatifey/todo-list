package dev.timatifey.todo_list.data.dao

import androidx.room.*
import dev.timatifey.todo_list.data.entities.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TasksDao {

    @Query("SELECT * FROM ${TaskEntity.TABLE_NAME}")
    fun readAllTasks(): Flow<List<TaskEntity>>

    @Insert(entity = TaskEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTask(task: TaskEntity)

    @Update(entity = TaskEntity::class)
    suspend fun updateTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)
}