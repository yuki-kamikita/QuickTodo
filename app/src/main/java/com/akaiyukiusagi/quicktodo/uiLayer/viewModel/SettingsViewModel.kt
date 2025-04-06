package com.akaiyukiusagi.quicktodo.uiLayer.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akaiyukiusagi.quicktodo.dataLayer.BooleanPreference
import com.akaiyukiusagi.quicktodo.dataLayer.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStore: DataStoreManager,
) : ViewModel(), ISettingsViewModel {
    // TODO: 項目増やすたびに増やさないといけない変数が多いからどこかにまとめたい
    private val _showDoneTasks = MutableStateFlow(BooleanPreference.SHOW_DONE_TASKS.initialValue)
    override val showDoneTasks = _showDoneTasks.asStateFlow()
    private val _showNotificationOnCreate = MutableStateFlow(BooleanPreference.SHOW_NOTIFICATION_ON_CREATE.initialValue)
    override val showNotificationOnCreate = _showNotificationOnCreate.asStateFlow()

    init {
        viewModelScope.launch {
            dataStore.getBooleanFlow(BooleanPreference.SHOW_DONE_TASKS).collect { value ->
                _showDoneTasks.value = value
            }
        }
        viewModelScope.launch {
            dataStore.getBooleanFlow(BooleanPreference.SHOW_NOTIFICATION_ON_CREATE).collect { value ->
                _showNotificationOnCreate.value = value
            }
        }
    }

    override fun changeShowDoneTask(show: Boolean) {
        viewModelScope.launch {
            dataStore.saveBoolean(BooleanPreference.SHOW_DONE_TASKS, show)
            _showDoneTasks.value = show
        }
    }

    override fun changeShowNotificationOnCreate(show: Boolean) {
        viewModelScope.launch {
            dataStore.saveBoolean(BooleanPreference.SHOW_NOTIFICATION_ON_CREATE, show)
            _showNotificationOnCreate.value = show
        }
    }
}

interface ISettingsViewModel {
    val showDoneTasks: Flow<Boolean>
    val showNotificationOnCreate: Flow<Boolean>
    fun changeShowDoneTask(show: Boolean)
    fun changeShowNotificationOnCreate(show: Boolean)
}

class PreviewSettingsViewModel: ISettingsViewModel {
    override val showDoneTasks: Flow<Boolean> = flowOf(BooleanPreference.SHOW_DONE_TASKS.initialValue)
    override val showNotificationOnCreate: Flow<Boolean> = flowOf(BooleanPreference.SHOW_NOTIFICATION_ON_CREATE.initialValue)

    override fun changeShowDoneTask(show: Boolean) {}
    override fun changeShowNotificationOnCreate(show: Boolean) {}
}