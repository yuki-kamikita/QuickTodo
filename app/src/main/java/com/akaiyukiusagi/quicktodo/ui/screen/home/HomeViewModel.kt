package com.akaiyukiusagi.quicktodo.ui.screen.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.akaiyukiusagi.quicktodo.model.room.entity.Task
import com.akaiyukiusagi.quicktodo.model.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val taskRepository: TaskRepository) : ViewModel() {
    val tasks: LiveData<List<Task>> = taskRepository.tasks
}

//@HiltViewModel
//class HomeViewModel @Inject constructor(private val taskRepository: TaskRepository) : ViewModel() {
//    val tasks: List<String> = listOf("11","WWAGADS")
//    val test: LiveData<List<Task>> = taskRepository.tasks
//}
