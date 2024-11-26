package com.akaiyukiusagi.quicktodo.ui_layer.view_model

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akaiyukiusagi.quicktodo.data_layer.room.entity.Task
import com.akaiyukiusagi.quicktodo.data_layer.repository.TaskRepository
import com.akaiyukiusagi.quicktodo.ui_layer.notification.NotificationUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val notificationUtil: NotificationUtil
) : ViewModel(), IHomeViewModel, LifecycleObserver {
    override val initialTasks: List<Task> = emptyList()
    override val initialDoneTasks: List<Task> = emptyList()
    override val tasks: Flow<List<Task>> = taskRepository.todoTasks
    override val doneTasks: Flow<List<Task>> = taskRepository.doneTasks
    private val notificationTasksFlow: Flow<List<Task>> = taskRepository.notificationTasks

    init {
        viewModelScope.launch {
            notificationTasksFlow.collect { tasks ->
                tasks.forEach { task ->
                    notificationUtil.pushNotification(task)
                }
            }
        }

        viewModelScope.launch {
            doneTasks.collect { doneTasks ->
                doneTasks.forEach { task ->
                    notificationUtil.removePushedNotification(task)
                }
            }
        }
    }

    override fun addTask(text: String) {
        if (text.isNotEmpty()) {
            val task = Task(content = text)
            viewModelScope.launch {
                taskRepository.insert(task)
            }
        }
    }
    override fun addTask(task: Task) {
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

    override fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskRepository.delete(task)
            if (task.sendNotification) notificationUtil.removePushedNotification(task)
        }
    }
}


interface IHomeViewModel {
    val initialTasks: List<Task>
    val tasks: Flow<List<Task>>
    val initialDoneTasks: List<Task>
    val doneTasks: Flow<List<Task>>
    fun addTask(text: String)
    fun addTask(task: Task)
    fun updateTask(task: Task)
    fun deleteTask(task: Task)
}

class PreviewHomeViewModel: IHomeViewModel {
    override val initialTasks: List<Task> = listOf(
        Task(1, "aaa", false, false),
        Task(2, "bbb", false, true),
        Task(3, "cccc", false, false),
        Task(4, "dddd", false, true)
    )
    override val tasks: Flow<List<Task>> = flowOf(initialTasks)

    private val today = LocalDateTime.now()
    override val initialDoneTasks: List<Task> =
        listOf(
            Task(11, "当日", true, false, today),
            Task(12, "1日前", true, false, today.minusDays(1)),
            Task(13, "2日前", true, false, today.minusDays(2)),
            Task(14, "3日前", true, false, today.minusDays(3)),
            Task(15, "7日前", true, false, today.minusDays(7)),
            Task(16, "8日前", true, false, today.minusDays(8)),
            Task(17, "30日前", true, false, today.minusDays(30)),
            Task(18, "31日前", true, false, today.minusDays(31)),
            Task(19, "100日前", true, false, today.minusDays(100)),
        )
    override val doneTasks: Flow<List<Task>> = flowOf(initialDoneTasks)

    override fun addTask(text: String) {}
    override fun addTask(task: Task) {}
    override fun updateTask(task: Task) {}
    override fun deleteTask(task: Task) {}
}