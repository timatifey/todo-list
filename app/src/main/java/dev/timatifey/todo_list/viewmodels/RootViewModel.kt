package dev.timatifey.todo_list.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.timatifey.todo_list.data.entities.TaskEntity
import dev.timatifey.todo_list.interactors.TasksInteractor
import dev.timatifey.todo_list.providers.TaskProvider
import dev.timatifey.todo_list.screens.common.nav.BackPressDispatcher
import dev.timatifey.todo_list.screens.common.nav.BackPressedListener
import dev.timatifey.todo_list.screens.common.nav.IAppRouter
import dev.timatifey.todo_list.screens.root.TasksAdapter
import dev.timatifey.todo_list.utils.generateTasks
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RootViewModel constructor(
    private val taskProvider: TaskProvider,
    private val tasksInteractor: TasksInteractor,
    private val appRouter: IAppRouter
) : ViewModel(), TasksAdapter.Listener {

    companion object {
        const val TAG = "RootViewModel"
    }

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Loading)
    val state: StateFlow<State>
        get() = _state

    private val _tasks: MutableStateFlow<List<TaskEntity>> = MutableStateFlow(emptyList())
    val tasks: StateFlow<List<TaskEntity>>
        get() = _tasks

    init {
        viewModelScope.launch {
            val tasksResult = tasksInteractor.getTaskList()
            Log.e(TAG, tasksResult.toString())
            _tasks.value = tasksResult
            _state.value = if (tasksResult.isEmpty()) State.EmptyData else State.Data(tasksResult)
        }
    }

    override fun onTaskClicked(task: TaskEntity) {
        taskProvider.bindTask(task)
        taskProvider.isEdit = true
        appRouter.toTaskFragment()
    }

    override fun onCheckBtnClicked(task: TaskEntity) {
        viewModelScope.launch {
            task.isDone = task.isDone.not()
            tasksInteractor.updateTask(task)
            fetchTasks()
        }
    }

    override fun onTaskSwiped(task: TaskEntity) {
        viewModelScope.launch {
            Log.e(TAG, "SWIPED")
            tasksInteractor.deleteTask(task)
            fetchTasks()
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            _state.value = State.Loading
            generateTasks().forEach { tasksInteractor.addTask(it) }
            fetchTasks()
        }
    }

    sealed class State {
        data class Data(val data: List<TaskEntity>) : State()
        object EmptyData : State()
        object Loading : State()
        //data class Error(val msgId: Int) : State()
    }

    private suspend fun fetchTasks() {
        val tasksResult = tasksInteractor.getTaskList()
        _tasks.value = tasksResult
        _state.value = if (tasksResult.isEmpty()) State.EmptyData else State.Data(tasksResult)
    }

    fun addNewTask() {
        appRouter.toTaskFragment()
    }

    fun clearDoneTasks() {
        viewModelScope.launch {
            _tasks.value
                .filter { it.isDone }
                .forEach { tasksInteractor.deleteTask(it) }
            fetchTasks()
        }
    }
}