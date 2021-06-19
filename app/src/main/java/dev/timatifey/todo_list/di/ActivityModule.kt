package dev.timatifey.todo_list.di

import android.app.Activity
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import dev.timatifey.todo_list.screens.common.base.BaseActivity
import dev.timatifey.todo_list.screens.common.nav.AppRouter
import dev.timatifey.todo_list.screens.common.nav.BackPressDispatcher
import dev.timatifey.todo_list.screens.common.nav.IAppRouter

@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {

    @Provides
    @ActivityScoped
    fun fragmentManager(baseActivity: BaseActivity): FragmentManager =
        baseActivity.supportFragmentManager

    @Provides
    @ActivityScoped
    fun baseActivity(activity: Activity): BaseActivity =
        activity as BaseActivity

    @Module
    @InstallIn(ActivityComponent::class)
    interface Binds {
        @dagger.Binds
        fun backPressDispatcher(baseActivity: BaseActivity): BackPressDispatcher

        @dagger.Binds
        fun appRouter(appRouter: AppRouter): IAppRouter
    }
}