package com.akaiyukiusagi.quicktodo.ui.screen.home

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akaiyukiusagi.quicktodo.model.room.entity.Task
import com.akaiyukiusagi.quicktodo.model.repository.TaskRepository
import com.akaiyukiusagi.quicktodo.notification.NotificationUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val notificationUtil: NotificationUtil
) : ViewModel(), LifecycleObserver {
    val tasks: LiveData<List<Task>> = taskRepository.todoTasks
    val doneTasks: LiveData<List<Task>> = taskRepository.doneTasks
    private val notificationTasks: LiveData<List<Task>> = taskRepository.notificationTasks

    init {
        // TODO: リマインド機能
        // 未完了のTODOが変更されたときに通知を送る
        notificationTasks.observeForever { tasks ->
            for (task in tasks) {
                notificationUtil.pushNotification(task)
            }
        }
        // 完了済みのものは通知から消す
        doneTasks.observeForever { doneTasks ->
            for (task in doneTasks) {
                notificationUtil.removePushedNotification(task)
            }
        }
    }

    fun addTask(text: String) {
        val task = Task(content = text)
        viewModelScope.launch {
            taskRepository.insert(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            taskRepository.update(task)
            if (!task.sendNotification) notificationUtil.removePushedNotification(task)
        }
    }

}