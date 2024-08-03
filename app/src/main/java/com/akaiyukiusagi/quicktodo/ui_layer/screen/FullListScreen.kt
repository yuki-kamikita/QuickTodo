package com.akaiyukiusagi.quicktodo.ui_layer.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.akaiyukiusagi.quicktodo.ScreenNavigator
import com.akaiyukiusagi.quicktodo.ui_layer.component.PreviewTemplate
import com.akaiyukiusagi.quicktodo.ui_layer.theme.QuickTodoTheme
import com.akaiyukiusagi.quicktodo.ui_layer.view_model.IHomeViewModel
import com.akaiyukiusagi.quicktodo.ui_layer.view_model.ISettingsViewModel
import com.akaiyukiusagi.quicktodo.ui_layer.view_model.PreviewHomeViewModel
import com.akaiyukiusagi.quicktodo.ui_layer.view_model.PreviewSettingsViewModel

/**
 *
 */
@Composable
fun FullListScreen(
    viewModel: IHomeViewModel,
    settings: ISettingsViewModel,
    navigator: NavController = rememberNavController()
) {
    Scaffold(
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { ScreenNavigator.Settings.navigate(navigator) }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                    FloatingActionButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Default.Add, "")
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
        ) {
        }
    }
}

@PreviewTemplate
@Composable
fun PreviewFullListScreen() {
    QuickTodoTheme {
        FullListScreen(PreviewHomeViewModel(), PreviewSettingsViewModel())
    }
}