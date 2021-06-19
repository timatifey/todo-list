package dev.timatifey.todo_list.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = TaskEntity.TABLE_NAME)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    var isDone: Boolean,
    val description: String,
    val deadlineTime: Long,
    val colorId: Int,
) {
    companion object {
        const val TABLE_NAME = "tasks_table"
    }
}
