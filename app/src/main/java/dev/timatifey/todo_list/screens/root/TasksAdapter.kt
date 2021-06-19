package dev.timatifey.todo_list.screens.root

import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import dev.timatifey.todo_list.screens.common.ItemSwipeManager
import dev.timatifey.todo_list.R
import dev.timatifey.todo_list.data.entities.TaskEntity
import java.text.SimpleDateFormat
import java.util.*

class TasksAdapter(
    private val listener: Listener
) : RecyclerView.Adapter<TasksAdapter.TaskViewHolder>(), ItemSwipeManager.SwipeListener {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.item_task__title)
        val tvDesc: TextView = itemView.findViewById(R.id.item_task__desc)
        val tvDeadline: TextView = itemView.findViewById(R.id.item_task__deadline)
        val ibCheck: ImageButton = itemView.findViewById(R.id.item_task__check_btn)
        val colorView: View = itemView.findViewById(R.id.item_task__color)
        val cardContent: View = itemView.findViewById(R.id.item_task__content)

        val checkImage = ContextCompat.getDrawable(itemView.context, R.drawable.ic_tick_true)
        val checkNotImage = ContextCompat.getDrawable(itemView.context, R.drawable.ic_tick_false)

        val blackColor = ContextCompat.getColor(itemView.context, R.color.blue_dark)
        val greyColor = ContextCompat.getColor(itemView.context, R.color.gray)
        val backGreyColor = ContextCompat.getColor(itemView.context, R.color.back_gray)
        val whiteColor = ContextCompat.getColor(itemView.context, R.color.white)
    }

    private val taskList = mutableListOf<TaskEntity>()
    private val calendar = Calendar.getInstance()
    private val formatter = SimpleDateFormat("d MMM, HH:mm")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        val holder = TaskViewHolder(view)
        holder.apply {
            itemView.setOnClickListener {
                listener.onTaskClicked(itemView.tag as TaskEntity)
            }
            ibCheck.setOnClickListener {
                listener.onCheckBtnClicked(itemView.tag as TaskEntity)
                notifyDataSetChanged()
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.apply {
            itemView.tag = task
            calendar.timeInMillis = task.deadlineTime
            tvDeadline.text = formatter.format(calendar.time)
            tvTitle.text = task.title
            tvDesc.text = task.description
            if (task.isDone) {
                tvTitle.paintFlags = tvTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                tvTitle.setTextColor(greyColor)
                ibCheck.background = checkImage
                cardContent.setBackgroundColor(backGreyColor)
                colorView.visibility = View.INVISIBLE
            } else {
                tvTitle.paintFlags = tvTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                tvTitle.setTextColor(blackColor)
                ibCheck.background = checkNotImage
                cardContent.setBackgroundColor(whiteColor)
                colorView.setBackgroundColor(ContextCompat.getColor(itemView.context, task.colorId))
                colorView.visibility = View.VISIBLE
            }
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    interface Listener {
        fun onTaskClicked(task: TaskEntity)
        fun onCheckBtnClicked(task: TaskEntity)
        fun onTaskSwiped(task: TaskEntity)
    }

    fun bindTasks(list: List<TaskEntity>) {
        taskList.clear()
        taskList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder) {
        val position = viewHolder.absoluteAdapterPosition
        Log.e("Adapter", "Swiped on $position")
        if (position != RecyclerView.NO_POSITION) {
            listener.onTaskSwiped(taskList[position])
            notifyDataSetChanged()
        }
    }
}
