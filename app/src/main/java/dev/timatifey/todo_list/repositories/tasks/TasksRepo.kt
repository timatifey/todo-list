package dev.timatifey.todo_list.repositories.tasks

import dev.timatifey.todo_list.data.dao.TasksDao
import dev.timatifey.todo_list.data.entities.TaskEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TasksRepo @Inject constructor(
    private val tasksDao: TasksDao
): ITasksRepo {

    override fun readAllTasks(): Flow<List<TaskEntity>> =
        tasksDao.readAllTasks()

    override suspend fun addNewTask(task: TaskEntity) {
        tasksDao.addTask(task)
    }

    override suspend fun updateTask(task: TaskEntity) {
        tasksDao.updateTask(task)
    }

    override suspend fun deleteTask(task: TaskEntity) {
        tasksDao.deleteTask(task)
    }
}