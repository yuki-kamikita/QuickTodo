@file:OptIn(ExperimentalMaterial3Api::class)

package com.akaiyukiusagi.quicktodo.ui.screen.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.akaiyukiusagi.quicktodo.model.room.entity.Task
import com.akaiyukiusagi.quicktodo.ui.theme.QuickTodoTheme

@Composable
fun HomeScreen() {
    val viewModel: HomeViewModel = hiltViewModel()
//    val tasks by viewModel.tasks.observeAsState(emptyList())

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        LazyColumn {
            val sample = listOf(Task(1), Task(2))
            items(viewModel.tasks.value ?: sample) { task ->
                TaskItem(task = task)
            }
        }

//        var text by remember { mutableStateOf("") }
//
//        TextField(
//            value = text,
//            onValueChange = { newText -> text = newText },
//            label = { Text("New Task") }
//        )
//
//        Button(onClick = { /* Add to database */ }) {
//            Text("Add Task")
//        }

    }
}



@Composable
fun TaskItem(task: Task) {
    Checkbox(
        checked = task.isCompleted,
        onCheckedChange = { /* Update in database */ }
    )
    TextField(
        value = task.content,
        onValueChange = { /* Update in database */ }
    )
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    QuickTodoTheme {
        HomeScreen()
    }
}