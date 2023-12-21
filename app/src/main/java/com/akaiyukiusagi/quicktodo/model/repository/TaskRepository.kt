package com.akaiyukiusagi.quicktodo.model.repository

import androidx.lifecycle.LiveData
import com.akaiyukiusagi.quicktodo.core.LogHelper
import com.akaiyukiusagi.quicktodo.model.room.entity.Task
import com.akaiyukiusagi.quicktodo.model.room.entity.TaskDao
import java.time.LocalDateTime
import javax.inject.Inject

class TaskRepository @Inject constructor(private val taskDao: TaskDao)  {
    val tasks: LiveData<List<Task>> = taskDao.getAllTasks()
    val todoTasks: LiveData<List<Task>> = taskDao.getTodoTasks()
    val doneTasks: LiveData<List<Task>> = taskDao.getDoneTasks()
    val notificationTasks: LiveData<List<Task>> = taskDao.getNotificationTasks()

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
