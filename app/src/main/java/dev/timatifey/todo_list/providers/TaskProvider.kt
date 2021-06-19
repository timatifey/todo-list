package dev.timatifey.todo_list.providers

import dev.timatifey.todo_list.R
import dev.timatifey.todo_list.data.entities.TaskEntity
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random


@Singleton
class TaskProvider @Inject constructor() {
    var isEdit: Boolean = false
    var id: Int = 0
    var isDone: Boolean = false

    var title: String = ""
    var description: String = ""
    var dayOfMonth: Int = 0
    var month: Int = 0
    var year: Int = 0
    var hours: Int = 0
    var minutes: Int = 0
    var colorId: Int = R.color.blue_start

    companion object {
        val colorIds = listOf(
            R.color.red,
            R.color.purple,
            R.color.yellow,
            R.color.green,
            R.color.blue_start
        )
    }

    fun clear() {
        isEdit = false
        id = 0
        isDone = false
        title = ""
        description = ""
        dayOfMonth = 0
        month = 0
        year = 0
        hours = 0
        minutes = 0
        colorId = R.color.blue_start
    }

    fun createTask(): TaskEntity {
        val calendar: Calendar = GregorianCalendar(
            year,
            month,
            dayOfMonth,
            hours,
            minutes
        )

        return TaskEntity(
            id = id,
            title = title,
            description = description,
            deadlineTime = calendar.timeInMillis,
            isDone = isDone,
            colorId = colorIds[Random.nextInt(0, colorIds.size)],
        )
    }

    fun bindTask(taskEntity: TaskEntity) {
        title = taskEntity.title
        id = taskEntity.id
        isDone = taskEntity.isDone
        description = taskEntity.description
        val calendar: Calendar = GregorianCalendar()
        calendar.timeInMillis = taskEntity.deadlineTime
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        month = calendar.get(Calendar.MONTH)
        year = calendar.get(Calendar.YEAR)
        hours = calendar.get(Calendar.HOUR)
        minutes = calendar.get(Calendar.MINUTE)
        colorId = taskEntity.colorId
    }
}