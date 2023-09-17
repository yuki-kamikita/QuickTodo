package com.akaiyukiusagi.quicktodo.ui.screen.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akaiyukiusagi.quicktodo.model.room.entity.Task
import com.akaiyukiusagi.quicktodo.model.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val taskRepository: TaskRepository) : ViewModel() {
    val tasks: LiveData<List<Task>> = taskRepository.tasks

    fun addTask(text: String) {
        val task = Task(
            id = 0,
            content = text,
            )
        viewModelScope.launch {
            taskRepository.addTask(task)
        }
    }
}

//@HiltViewModel
//class HomeViewModel @Inject constructor(private val taskRepository: TaskRepository) : ViewModel() {
//    val tasks: List<String> = listOf("11","WWAGADS")
//    val test: LiveData<List<Task>> = taskRepository.tasks
//}
