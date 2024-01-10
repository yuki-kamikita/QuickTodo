package com.akaiyukiusagi.quicktodo.data_layer.repository

import com.akaiyukiusagi.quicktodo.core.LogHelper
import com.akaiyukiusagi.quicktodo.data_layer.room.entity.Task
import com.akaiyukiusagi.quicktodo.data_layer.room.entity.TaskDao
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
        LogHelper.d("$task")
    }

    suspend fun update(task: Task) {
        taskLocalDataSource.update(task)
        LogHelper.d("$task")
    }

    suspend fun complete(id: Int) {
        taskLocalDataSource.markAsCompleted(id, LocalDateTime.now())
        LogHelper.d("$id")
    }

}
