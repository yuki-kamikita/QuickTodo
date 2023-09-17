package com.akaiyukiusagi.quicktodo.model.repository

import androidx.lifecycle.LiveData
import com.akaiyukiusagi.quicktodo.model.room.entity.Task
import com.akaiyukiusagi.quicktodo.model.room.entity.TaskDao
import javax.inject.Inject

class TaskRepository @Inject constructor(private val taskDao: TaskDao)  {
    val tasks: LiveData<List<Task>> = taskDao.getAllTasks()
    val todoTasks: LiveData<List<Task>> = taskDao.getTodoTasks()
    val doneTasks: LiveData<List<Task>> = taskDao.getDoneTasks()

    suspend fun insert(task: Task) {
        taskDao.insert(task)
    }

    suspend fun update(task: Task) {
        taskDao.update(task)
    }

}
