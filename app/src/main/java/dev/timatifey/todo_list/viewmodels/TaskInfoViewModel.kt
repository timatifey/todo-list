package dev.timatifey.todo_list.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.timatifey.todo_list.data.entities.TaskEntity
import dev.timatifey.todo_list.interactors.TasksInteractor
import dev.timatifey.todo_list.providers.TaskProvider
import dev.timatifey.todo_list.screens.common.nav.BackPressDispatcher
import dev.timatifey.todo_list.screens.common.nav.BackPressedListener
import dev.timatifey.todo_list.screens.common.nav.IAppRouter
import kotlinx.coroutines.launch

class TaskInfoViewModel constructor(
    private val tasksInteractor: TasksInteractor,
    val taskProvider: TaskProvider,
    private val appRouter: IAppRouter,
    private val backPressDispatcher: BackPressDispatcher
) : ViewModel(), BackPressedListener {

    fun onStart() {
        backPressDispatcher.registerListener(this)
    }

    fun onStop() {
        backPressDispatcher.unregisterListener(this)
    }

    fun onTitleChanged(text: String) {
        taskProvider.title = text
    }

    fun onDescriptionChanged(text: String) {
        taskProvider.description = text
    }

    fun onDatePicked(dayOfMonth: Int, month: Int, year: Int) {
        taskProvider.dayOfMonth = dayOfMonth
        taskProvider.month = month
        taskProvider.year = year
    }

    fun onTimePicked(hour: Int, minute: Int) {
        taskProvider.hours = hour
        taskProvider.minutes = minute
    }

    fun onSaveBtnClicked(): TaskEntity {
        val task = taskProvider.createTask()
        viewModelScope.launch {
            if (!taskProvider.isEdit) {
                tasksInteractor.addTask(task)
            } else {
                tasksInteractor.updateTask(task)
            }
            taskProvider.clear()
            appRouter.navigateUp()
        }
        return task
    }

    override fun onBackPressed(): Boolean {
        taskProvider.clear()
        appRouter.navigateUp()
        return true
    }
}