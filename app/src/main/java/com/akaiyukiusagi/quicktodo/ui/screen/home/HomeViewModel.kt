package com.akaiyukiusagi.quicktodo.ui.screen.home

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
) : ViewModel(), IHomeViewModel, LifecycleObserver {
    override val tasks: LiveData<List<Task>> = taskRepository.todoTasks
    override val doneTasks: LiveData<List<Task>> = taskRepository.doneTasks
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

    override fun addTask(text: String) {
        val task = Task(content = text)
        viewModelScope.launch {
            taskRepository.insert(task)
        }
    }

    override fun updateTask(task: Task) {
        viewModelScope.launch {
            taskRepository.update(task)
            if (!task.sendNotification) notificationUtil.removePushedNotification(task)
        }
    }

}

interface IHomeViewModel {
    val tasks: LiveData<List<Task>>
    val doneTasks: LiveData<List<Task>>
    fun addTask(text: String)
    fun updateTask(task: Task)
}

class PreviewHomeViewModel: IHomeViewModel {
    override val tasks = MutableLiveData<List<Task>>().apply {
        value = listOf(
            Task(1, "aaa", false, false),
            Task(2, "bbb", false, true)
        )
    }
    override val doneTasks = MutableLiveData<List<Task>>().apply {
        value = listOf(
            Task(3, "ccc", true, false)
        )
    }
    override fun addTask(text: String) {}

    override fun updateTask(task: Task) {}
}