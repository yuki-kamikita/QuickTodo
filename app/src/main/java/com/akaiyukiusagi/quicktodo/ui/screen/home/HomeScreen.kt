@file:OptIn(ExperimentalMaterial3Api::class)

package com.akaiyukiusagi.quicktodo.ui.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.akaiyukiusagi.quicktodo.model.room.entity.Task
import com.akaiyukiusagi.quicktodo.ui.component.OnPause
import com.akaiyukiusagi.quicktodo.ui.theme.QuickTodoTheme

@Composable
fun HomeScreen() {
    val viewModel: HomeViewModel = hiltViewModel()
    val tasks by viewModel.tasks.observeAsState(emptyList())
    val doneTasks by viewModel.doneTasks.observeAsState(emptyList())
    val focusManager = LocalFocusManager.current

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) { focusManager.clearFocus() },
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            // TODO: 外に出す
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Top
            ) {
                items(tasks, key = { task -> task.id }) { task ->
                    TaskItem(
                        task = task,
                        tapCheckbox = { tappedTask ->
                            viewModel.doneTask(tappedTask)
                        },
                        editComplete = { updatedTask ->
                            viewModel.updateTask(updatedTask)
                        }
                    )
                }
                item { Divider() }
                items(doneTasks, key = { task -> task.id }) { task ->
                    TaskItem(
                        task = task,
                        tapCheckbox = { tappedTask ->
                            viewModel.restoreTask(tappedTask)
                        },
                        editComplete = {}
                    )
                }
            }
            Divider()
            NewTask { text ->
                viewModel.addTask(text)
            }
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    tapCheckbox: (Task) -> Unit,
    editComplete: (Task) -> Unit
) {
    var textFieldValue by remember { mutableStateOf(task.content) }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
    ) {
        Checkbox(
            checked = task.isCompleted,
            onCheckedChange = { isChecked ->
                tapCheckbox(task.copy(isCompleted = isChecked))
            }
        )

        TextField(
            value = textFieldValue,
            maxLines = 1,
            onValueChange = { newText ->
                textFieldValue = newText
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    if (!focusState.isFocused) {
                        editComplete(task.copy(content = textFieldValue))
                    }
                },
            enabled = !task.isCompleted
        )

        OnPause {
            editComplete(task.copy(content = textFieldValue))
        }
    }
}

@Composable
fun NewTask(onAddTask: (String) -> Unit) {
    var text by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextField(
            value = text,
            onValueChange = { newText -> text = newText },
            label = { Text("New Task") },
            modifier = Modifier.weight(1f)
        )

        IconButton(
            onClick = { onAddTask(text) },
//            modifier = Modifier.padding(start = 8.dp)
        ) {
            Icon(imageVector = Icons.Default.Send, contentDescription = "Send")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    QuickTodoTheme {
        HomeScreen()
    }
}