package dev.timatifey.todo_list.screens.root

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import dev.timatifey.todo_list.screens.common.ItemSwipeManager
import dev.timatifey.todo_list.R
import dev.timatifey.todo_list.screens.common.base.BaseActivity
import dev.timatifey.todo_list.viewmodels.RootViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class RootFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var emptyText: TextView
    private lateinit var fabAddNewTask: FloatingActionButton
    private lateinit var fabClearDone: AppCompatImageButton

    private lateinit var rootViewModel: RootViewModel

    private lateinit var tasksAdapter: TasksAdapter
    private lateinit var swipeManager: ItemSwipeManager

    private fun View.init() {
        rootViewModel = (activity as BaseActivity).viewModelFactory.createRootViewModel()

        recyclerView = findViewById(R.id.fragment_root__recycler)
        swipeRefreshLayout = findViewById(R.id.fragment_root__swipe)
        progressBar = findViewById(R.id.fragment_root__progress)
        emptyText = findViewById(R.id.fragment_root__empty_text)
        fabAddNewTask = findViewById(R.id.fragment_root__fab_add)
        fabClearDone = findViewById(R.id.fragment_root__fab_clear)

        tasksAdapter = TasksAdapter(rootViewModel)
        recyclerView.apply {
            adapter = tasksAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            val dividerItemDecoration = DividerItemDecoration(
                recyclerView.context,
                RecyclerView.VERTICAL
            )
            addItemDecoration(dividerItemDecoration)
        }

        swipeManager = ItemSwipeManager(context, tasksAdapter)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_root, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.init()

        rootViewModel.state.onEach { state ->
            when (state) {
                is RootViewModel.State.Data -> {
                    progressBar.isVisible = false
                    emptyText.isVisible = false
                    tasksAdapter.bindTasks(state.data)
                    fabAddNewTask.isVisible = true
                    fabClearDone.isVisible = true
                    recyclerView.isVisible = true
                    swipeRefreshLayout.isRefreshing = false
                }
                is RootViewModel.State.Loading -> {
                    fabAddNewTask.isVisible = false
                    fabClearDone.isVisible = false
                    progressBar.isVisible = true
                    emptyText.isVisible = false
                    recyclerView.isVisible = false
                    swipeRefreshLayout.isRefreshing = true
                }
                is RootViewModel.State.EmptyData -> {
                    fabAddNewTask.isVisible = true
                    fabClearDone.isVisible = false
                    fabClearDone.isVisible = false
                    progressBar.isVisible = false
                    emptyText.isVisible = true
                    recyclerView.isVisible = false
                    swipeRefreshLayout.isRefreshing = false
                }
            }
        }.launchIn(lifecycleScope)

        swipeRefreshLayout.setOnRefreshListener {
            rootViewModel.refreshData()
        }

        fabAddNewTask.setOnClickListener {
            rootViewModel.addNewTask()
        }

        fabClearDone.setOnClickListener {
            rootViewModel.clearDoneTasks()
        }

        rootViewModel.tasks.onEach { list ->
            tasksAdapter.bindTasks(list)
        }.launchIn(lifecycleScope)
    }

    override fun onStart() {
        super.onStart()
        swipeManager.attachToRecyclerView(recyclerView)
    }

    override fun onStop() {
        super.onStop()
        swipeManager.detachFromRecyclerView()
    }

    companion object {
        fun newInstance(): Fragment = RootFragment()
    }
}