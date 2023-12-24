package com.akaiyukiusagi.quicktodo.model.repository

import com.akaiyukiusagi.quicktodo.core.LogHelper
import com.akaiyukiusagi.quicktodo.model.room.entity.Task
import com.akaiyukiusagi.quicktodo.model.room.entity.TaskDao
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import javax.inject.Inject

class TaskRepository @Inject constructor(private val taskDao: TaskDao)  {
    val tasks: Flow<List<Task>> = taskDao.getAllTasks()
    val todoTasks: Flow<List<Task>> = taskDao.getTodoTasks()
    val doneTasks: Flow<List<Task>> = taskDao.getDoneTasks()
    val notificationTasks: Flow<List<Task>> = taskDao.getNotificationTasks()

    suspend fun insert(task: Task) {
        taskDao.insert(task)
        LogHelper.d("$task")
    }

    suspend fun update(task: Task) {
        taskDao.update(task)
        LogHelper.d("$task")
    }

    suspend fun complete(id: Int) {
        taskDao.markAsCompleted(id, LocalDateTime.now())
        LogHelper.d("$id")
    }

}
