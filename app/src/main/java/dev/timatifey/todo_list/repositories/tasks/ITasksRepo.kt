package dev.timatifey.todo_list.repositories.tasks

import dev.timatifey.todo_list.data.entities.TaskEntity
import kotlinx.coroutines.flow.Flow

interface ITasksRepo {
    fun readAllTasks(): Flow<List<TaskEntity>>
    suspend fun addNewTask(task: TaskEntity)
    suspend fun updateTask(task: TaskEntity)
    suspend fun deleteTask(task: TaskEntity)
}