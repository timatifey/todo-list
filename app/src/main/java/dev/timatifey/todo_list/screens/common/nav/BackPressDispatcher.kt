package dev.timatifey.todo_list.screens.common.nav

interface BackPressDispatcher {
    fun registerListener(listener: BackPressedListener)
    fun unregisterListener(listener: BackPressedListener)
}