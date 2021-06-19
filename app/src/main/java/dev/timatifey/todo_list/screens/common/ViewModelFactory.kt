package dev.timatifey.todo_list.screens.common

import dagger.hilt.android.scopes.ActivityScoped
import dev.timatifey.todo_list.interactors.TasksInteractor
import dev.timatifey.todo_list.providers.TaskProvider
import dev.timatifey.todo_list.screens.common.nav.BackPressDispatcher
import dev.timatifey.todo_list.screens.common.nav.IAppRouter
import dev.timatifey.todo_list.viewmodels.RootViewModel
import dev.timatifey.todo_list.viewmodels.TaskInfoViewModel
import javax.inject.Inject

@ActivityScoped
class ViewModelFactory @Inject constructor(
    private val appRouter: IAppRouter,
    private val backPressDispatcher: BackPressDispatcher,
    private val taskProvider: TaskProvider,
    private val tasksInteractor: TasksInteractor
) {
    fun createRootViewModel(): RootViewModel =
        RootViewModel(
            taskProvider,
            tasksInteractor,
            appRouter,
        )

    fun createTaskInfoViewModel(): TaskInfoViewModel =
        TaskInfoViewModel(tasksInteractor, taskProvider, appRouter, backPressDispatcher)
}