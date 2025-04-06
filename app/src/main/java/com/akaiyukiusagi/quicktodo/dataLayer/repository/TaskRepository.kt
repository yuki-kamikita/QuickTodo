package com.akaiyukiusagi.quicktodo.dataLayer.repository

import com.akaiyukiusagi.quicktodo.core.LogHelper
import com.akaiyukiusagi.quicktodo.dataLayer.room.entity.Task
import com.akaiyukiusagi.quicktodo.dataLayer.room.entity.TaskDao
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import javax.inject.Inject

class TaskRepository @Inject constructor(private val taskLocalDataSource: TaskDao)  {
    val tasks: Flow<List<Task>> = taskLocalDataSource.getAllTasks()
    val todoTasks: Flow<List<Task>> = taskLocalDataSource.getTodoTasks()
    val doneTasks: Flow<List<Task>> = taskLocalDataSource.getDoneTasks()
    val notificationTasks: Flow<List<Task>> = taskLocalDataSource.getNotificationTasks()

    suspend fun insert(task: Task) {
        taskLocalDataSource.insert(task)
        LogHelper.d("insert: $task")
    }

    suspend fun update(task: Task) {
        taskLocalDataSource.update(task)
        LogHelper.d("update: $task")
    }

    suspend fun complete(id: Int) {
        taskLocalDataSource.markAsCompleted(id, LocalDateTime.now())
        LogHelper.d("complete: $id")
    }

    suspend fun delete(task: Task) {
        taskLocalDataSource.delete(task)
        LogHelper.d("delete: $task")
    }

}
