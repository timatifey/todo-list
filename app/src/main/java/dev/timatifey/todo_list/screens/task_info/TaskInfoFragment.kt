package dev.timatifey.todo_list.screens.task_info

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.doOnTextChanged
import dev.timatifey.todo_list.R
import dev.timatifey.todo_list.data.entities.TaskEntity
import dev.timatifey.todo_list.receivers.ReminderBroadcast
import dev.timatifey.todo_list.receivers.ReminderBroadcast.Companion.EXTRA_DESC
import dev.timatifey.todo_list.receivers.ReminderBroadcast.Companion.EXTRA_ID
import dev.timatifey.todo_list.receivers.ReminderBroadcast.Companion.EXTRA_TITLE
import dev.timatifey.todo_list.screens.common.base.BaseActivity
import dev.timatifey.todo_list.viewmodels.TaskInfoViewModel
import java.util.*

class TaskInfoFragment : Fragment() {

    private lateinit var taskInfoViewModel: TaskInfoViewModel

    private lateinit var title: AppCompatEditText
    private lateinit var description: AppCompatEditText
    private lateinit var datePicker: DatePicker
    private lateinit var timePicker: TimePicker
    private lateinit var saveBtn: AppCompatButton

    @RequiresApi(Build.VERSION_CODES.M)
    private fun View.init() {
        taskInfoViewModel = (activity as BaseActivity).viewModelFactory.createTaskInfoViewModel()

        title = findViewById(R.id.fragment_task_info__title)
        description = findViewById(R.id.fragment_task_info__desc)
        datePicker = findViewById(R.id.fragment_task_info__date_picker)
        timePicker = findViewById(R.id.fragment_task_info__time_picker)
        saveBtn = findViewById(R.id.fragment_task_info__save_btn)

        datePicker.minDate = Calendar.getInstance().timeInMillis

        taskInfoViewModel.taskProvider.apply {
            if (isEdit) {
                this@TaskInfoFragment.title.setText(title)
                this@TaskInfoFragment.description.setText(description)
                this@TaskInfoFragment.datePicker.updateDate(year, month, dayOfMonth)
                this@TaskInfoFragment.timePicker.hour = hours
                this@TaskInfoFragment.timePicker.minute = minutes
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_info, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.init()

        title.doOnTextChanged { text, _, _, _ -> taskInfoViewModel.onTitleChanged(text.toString()) }
        description.doOnTextChanged { text, _, _, _ -> taskInfoViewModel.onDescriptionChanged(text.toString()) }
        saveBtn.setOnClickListener {
            taskInfoViewModel.onDatePicked(datePicker.dayOfMonth, datePicker.month, datePicker.year)
            taskInfoViewModel.onTimePicked(timePicker.hour, timePicker.minute)
            val task = taskInfoViewModel.onSaveBtnClicked()
            createNotification(task)
        }
    }

    override fun onStart() {
        super.onStart()
        taskInfoViewModel.onStart()
    }

    override fun onStop() {
        super.onStop()
        taskInfoViewModel.onStop()
    }

    companion object {
        fun newInstance(): Fragment = TaskInfoFragment()
    }

    private fun createNotification(task: TaskEntity) {
        val intent = Intent(requireContext(), ReminderBroadcast::class.java)
        intent.apply {
            putExtra(EXTRA_ID, task.id)
            putExtra(EXTRA_TITLE, task.title)
            putExtra(EXTRA_DESC, task.description)
        }
        val pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, 0)
        val alarmManager = getSystemService(requireContext(), AlarmManager::class.java)
        alarmManager!!.set(AlarmManager.RTC_WAKEUP, task.deadlineTime, pendingIntent)
    }

}