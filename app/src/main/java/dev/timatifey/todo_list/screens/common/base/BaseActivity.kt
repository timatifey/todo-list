package dev.timatifey.todo_list.screens.common.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import dev.timatifey.todo_list.screens.common.ViewModelFactory
import dev.timatifey.todo_list.screens.common.nav.BackPressDispatcher
import dev.timatifey.todo_list.screens.common.nav.BackPressedListener
import dev.timatifey.todo_list.screens.common.nav.IAppRouter
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity(), BackPressDispatcher {

    private val backPressedListeners: ArrayDeque<BackPressedListener> = ArrayDeque()

    @Inject
    lateinit var appRouter: IAppRouter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appRouter.initialize(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        appRouter.onSaveInstanceState(outState)
    }

    override fun registerListener(listener: BackPressedListener) {
        backPressedListeners.addFirst(listener);
    }

    override fun unregisterListener(listener: BackPressedListener) {
        backPressedListeners.remove(listener)
    }

    override fun onBackPressed() {
        for (listener in backPressedListeners) {
            if (listener.onBackPressed()) return
        }
        super.onBackPressed()
    }
}