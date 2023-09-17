package com.akaiyukiusagi.quicktodo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.akaiyukiusagi.quicktodo.ui.screen.home.HomeScreen
import com.akaiyukiusagi.quicktodo.ui.theme.QuickTodoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuickTodoTheme {
                HomeScreen()
            }
        }
    }
}