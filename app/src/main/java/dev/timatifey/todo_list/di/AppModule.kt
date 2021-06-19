package dev.timatifey.todo_list.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.timatifey.todo_list.repositories.tasks.ITasksRepo
import dev.timatifey.todo_list.repositories.tasks.TasksRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Module
    @InstallIn(SingletonComponent::class) interface Binds {
        @dagger.Binds fun blogItemRepo(tasksRepo: TasksRepo): ITasksRepo
    }
}