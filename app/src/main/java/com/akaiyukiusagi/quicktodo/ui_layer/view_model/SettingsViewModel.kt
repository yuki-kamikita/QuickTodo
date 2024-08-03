package com.akaiyukiusagi.quicktodo.ui_layer.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akaiyukiusagi.quicktodo.data_layer.BooleanPreference
import com.akaiyukiusagi.quicktodo.data_layer.DataStoreManager
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
    private val _showDoneTasks = MutableStateFlow(BooleanPreference.SHOW_DONE_TASKS.initialValue)
    override val showDoneTasks = _showDoneTasks.asStateFlow()
    private val _useSimpleUI = MutableStateFlow(BooleanPreference.USE_SIMPLE_UI.initialValue)
    override val useSimpleUI = _useSimpleUI.asStateFlow()

    init {
        viewModelScope.launch {
            dataStore.getBooleanFlow(BooleanPreference.SHOW_DONE_TASKS).collect { value ->
                _showDoneTasks.value = value
            }
        }
        viewModelScope.launch {
            dataStore.getBooleanFlow(BooleanPreference.USE_SIMPLE_UI).collect { value ->
                _useSimpleUI.value = value
            }
        }
    }

    override fun changeShowDoneTask(show: Boolean) {
        viewModelScope.launch {
            dataStore.saveBoolean(BooleanPreference.SHOW_DONE_TASKS, show)
            _showDoneTasks.value = show
        }
    }

    override fun changeUseSimpleUI(simpleUI: Boolean) {
        viewModelScope.launch {
            dataStore.saveBoolean(BooleanPreference.USE_SIMPLE_UI, simpleUI)
            _useSimpleUI.value = simpleUI
        }
    }
}

interface ISettingsViewModel {
    val showDoneTasks: Flow<Boolean>
    val useSimpleUI: Flow<Boolean>
    fun changeShowDoneTask(show: Boolean)
    fun changeUseSimpleUI(simpleUI: Boolean)
}

class PreviewSettingsViewModel: ISettingsViewModel {
    override val showDoneTasks: Flow<Boolean> = flowOf(BooleanPreference.SHOW_DONE_TASKS.initialValue)
    override val useSimpleUI: Flow<Boolean> = flowOf(BooleanPreference.USE_SIMPLE_UI.initialValue)

    override fun changeShowDoneTask(show: Boolean) {}
    override fun changeUseSimpleUI(simpleUI: Boolean) {}
}