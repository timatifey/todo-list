package dev.timatifey.todo_list.screens.common.nav

import android.os.Bundle
import com.ncapdevi.fragnav.FragNavController

interface IAppRouter: FragNavController.RootFragmentListener {
    fun initialize(savedInstanceState: Bundle?)
    fun toTaskFragment()
    fun navigateUp()
    fun onSaveInstanceState(outState: Bundle?)
}