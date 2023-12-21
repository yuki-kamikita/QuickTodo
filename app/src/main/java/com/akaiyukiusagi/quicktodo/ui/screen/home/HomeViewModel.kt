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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
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
        val today = LocalDateTime.now()
        value = listOf(
            Task(3, "当日", true, false, today),
            Task(4, "1日前", true, false, today.minusDays(1)),
            Task(5, "2日前", true, false, today.minusDays(2)),
            Task(6, "3日前", true, false, today.minusDays(3)),
            Task(7, "7日前", true, false, today.minusDays(7)),
            Task(8, "8日前", true, false, today.minusDays(8)),
            Task(9, "30日前", true, false, today.minusDays(30)),
            Task(10, "31日前", true, false, today.minusDays(31)),
            Task(11, "100日前", true, false, today.minusDays(100)),
        )
    }
    override fun addTask(text: String) {}

    override fun updateTask(task: Task) {}

//    override fun taskComplete(task: Task) {}
}