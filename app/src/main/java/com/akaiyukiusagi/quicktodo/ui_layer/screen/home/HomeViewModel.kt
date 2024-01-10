package com.akaiyukiusagi.quicktodo.ui_layer.screen.home

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
    val tasks: Flow<List<Task>>
    val doneTasks: Flow<List<Task>>
    fun addTask(text: String)
    fun updateTask(task: Task)
}

class PreviewHomeViewModel: IHomeViewModel {
    override val tasks: Flow<List<Task>> = flowOf(
        listOf(
            Task(1, "aaa", false, false),
            Task(2, "bbb", false, true)
        )
    )

    private val today = LocalDateTime.now()
    override val doneTasks: Flow<List<Task>> = flowOf(
        listOf(
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
    )

    override fun addTask(text: String) {}

    override fun updateTask(task: Task) {}

//    override fun taskComplete(task: Task) {}
}