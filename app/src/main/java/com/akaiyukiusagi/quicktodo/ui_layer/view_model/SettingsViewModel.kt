package com.akaiyukiusagi.quicktodo.ui_layer.view_model

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akaiyukiusagi.quicktodo.data_layer.room.entity.Task
import com.akaiyukiusagi.quicktodo.data_layer.repository.TaskRepository
import com.akaiyukiusagi.quicktodo.ui_layer.notification.NotificationUtil
import com.akaiyukiusagi.quicktodo.ui_layer.theme.LightColorScheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val notificationUtil: NotificationUtil
) : ViewModel(), ISettingsViewModel, LifecycleObserver {
    override val themeColor: Flow<ColorScheme> = flowOf(LightColorScheme)

    override fun deleteTask(task: Task) {
        viewModelScope.launch {

        }
    }
}


interface ISettingsViewModel {
    val themeColor: Flow<ColorScheme>
    fun deleteTask(task: Task)
}

class PreviewSettingsViewModel: ISettingsViewModel {
    override val themeColor: Flow<ColorScheme> = flowOf(LightColorScheme)

    override fun deleteTask(task: Task) {}
}