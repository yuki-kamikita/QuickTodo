package com.akaiyukiusagi.quicktodo.ui_layer.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.akaiyukiusagi.quicktodo.ui_layer.theme.QuickTodoTheme

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