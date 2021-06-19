package dev.timatifey.todo_list.utils

import dev.timatifey.todo_list.data.entities.TaskEntity
import dev.timatifey.todo_list.providers.TaskProvider
import java.util.*
import kotlin.random.Random

fun generateTasks(): List<TaskEntity> {
    val res = mutableListOf<TaskEntity>()
    val calendar = Calendar.getInstance()
    for (i in 1..20) {
        res.add(
            TaskEntity(
                title = "Task$i",
                description = "Description $i",
                deadlineTime = calendar.timeInMillis + Random.nextLong(1000, 1000 * 50),
                isDone = Random.nextBoolean(),
                colorId = TaskProvider.colorIds[Random.nextInt(0, TaskProvider.colorIds.size)],
            )
        )
    }
    return res
}

//fun generateTask(): TaskEntity {
//    val calendar = Calendar.getInstance()
//    return TaskEntity(
//        title = "Task ${Random.nextInt(1000)}",
//        description = "Descr",
//        deadlineTime = calendar.timeInMillis,
//        isDone = false,
//    )
//}