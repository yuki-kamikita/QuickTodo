package com.akaiyukiusagi.quicktodo.ui_layer.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.akaiyukiusagi.quicktodo.data_layer.BooleanPreference
import com.akaiyukiusagi.quicktodo.ui_layer.view_model.IHomeViewModel
import com.akaiyukiusagi.quicktodo.ui_layer.view_model.ISettingsViewModel

@Composable
fun HomeScreen(
    viewModel: IHomeViewModel,
    settings: ISettingsViewModel,
    navigator: NavController = rememberNavController()
) {
    val simpleUI = settings.useSimpleUI.collectAsState(initial = BooleanPreference.USE_SIMPLE_UI.initialValue).value

    if (simpleUI) {
        SimpleListScreen(viewModel, settings, navigator)
    } else {
        FullListScreen(viewModel, settings, navigator)
    }
}
