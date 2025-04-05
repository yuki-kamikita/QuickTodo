package com.akaiyukiusagi.quicktodo.uiLayer.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.akaiyukiusagi.quicktodo.uiLayer.theme.QuickTodoTheme

@Composable
fun PreviewComponent(content: @Composable () -> Unit) {
    QuickTodoTheme {
        Surface (
            color = MaterialTheme.colorScheme.background
        ) {
            content()
        }
    }
}