package com.akaiyukiusagi.quicktodo.ui.screen.home

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akaiyukiusagi.quicktodo.model.room.entity.Task
import com.akaiyukiusagi.quicktodo.model.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel(), LifecycleObserver {
    val tasks: LiveData<List<Task>> = taskRepository.tasks

    fun addTask(text: String) {
        val task = Task(
            id = 0,
            content = text,
            )
        viewModelScope.launch {
            taskRepository.insert(task)
        }
    }

    fun doneTask(task: Task) {
        viewModelScope.launch {
            taskRepository.update(task.copy(isCompleted = true))
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            taskRepository.update(task)
        }
    }

}