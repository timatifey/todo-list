package dev.timatifey.todo_list.screens.common.nav

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.ncapdevi.fragnav.FragNavController
import dagger.hilt.android.scopes.ActivityScoped
import dev.timatifey.todo_list.R
import dev.timatifey.todo_list.screens.task_info.TaskInfoFragment
import dev.timatifey.todo_list.screens.root.RootFragment
import javax.inject.Inject

@ActivityScoped
class AppRouter @Inject constructor(
    fragmentManager: FragmentManager
): IAppRouter {

    private val fragNavController: FragNavController =
        FragNavController(fragmentManager, R.id.activity_main__container)

    override fun initialize(savedInstanceState: Bundle?){
        fragNavController.apply {
            rootFragmentListener = this@AppRouter
            initialize(savedInstanceState = savedInstanceState)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        fragNavController.onSaveInstanceState(outState)
    }

    override val numberOfRootFragments: Int
        get() = 1

    override fun getRootFragment(index: Int): Fragment = RootFragment.newInstance()

    override fun toTaskFragment() {
        fragNavController.pushFragment(TaskInfoFragment.newInstance())
    }

    override fun navigateUp() {
        fragNavController.popFragment()
    }
}