package dev.timatifey.todo_list.interactors

import dev.timatifey.todo_list.data.entities.TaskEntity
import dev.timatifey.todo_list.repositories.tasks.ITasksRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TasksInteractor @Inject constructor(
    private val tasksRepo: ITasksRepo,
    private val ioDispatcher: CoroutineDispatcher,
) {
    suspend fun getTaskList(): List<TaskEntity> = withContext(ioDispatcher) {
        return@withContext tasksRepo.readAllTasks().firstOrNull() ?: emptyList()
    }

    suspend fun addTask(taskEntity: TaskEntity) = withContext(ioDispatcher) {
        tasksRepo.addNewTask(taskEntity)
    }

    suspend fun updateTask(taskEntity: TaskEntity) = withContext(ioDispatcher) {
        tasksRepo.updateTask(taskEntity)
    }

    suspend fun deleteTask(taskEntity: TaskEntity) = withContext(ioDispatcher) {
        tasksRepo.deleteTask(taskEntity)
    }
}