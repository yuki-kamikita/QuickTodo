package com.akaiyukiusagi.quicktodo.model.repository

import androidx.lifecycle.LiveData
import com.akaiyukiusagi.quicktodo.model.room.entity.Task
import com.akaiyukiusagi.quicktodo.model.room.entity.TaskDao
import javax.inject.Inject

class TaskRepository @Inject constructor(private val taskDao: TaskDao)  {
    val tasks: LiveData<List<Task>> = taskDao.getAllTasks()

    suspend fun addTask(task: Task) {
        taskDao.insert(task)
    }
}

//class TaskRepository @Inject constructor()  {
//    val tasks: List<String> = listOf("11","WWAGADS")
//}
