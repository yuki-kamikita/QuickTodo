@file:OptIn(ExperimentalMaterial3Api::class)

package com.akaiyukiusagi.quicktodo.ui.screen.home

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.Notifications
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
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@Composable
fun HomeScreen() {
    val viewModel: HomeViewModel = hiltViewModel()
    val tasks by viewModel.tasks.observeAsState(emptyList())
    val doneTasks by viewModel.doneTasks.observeAsState(emptyList())
    val focusManager = LocalFocusManager.current

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding() // システムバーと被らせない？
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
                        updateTask = { updatedTask -> viewModel.updateTask(updatedTask) }
                    )
                }
                item { Divider() }
                items(doneTasks, key = { task -> task.id }) { task ->
                    TaskItem(
                        task = task,
                        updateTask = { tappedTask -> viewModel.updateTask(tappedTask) },
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
    updateTask: (Task) -> Unit
) {
    var textFieldValue by remember { mutableStateOf(task.content) }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current


    OnPause {
        updateTask(task.copy(content = textFieldValue))
    }

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
    ) {
        Checkbox(
            checked = task.isCompleted,
            onCheckedChange = { isChecked ->
                updateTask(task.copy(isCompleted = isChecked))
            }
        )

        TextField(
            value = textFieldValue,
            maxLines = 1,
            onValueChange = { newText -> textFieldValue = newText },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            enabled = !task.isCompleted,
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    if (!focusState.isFocused) updateTask(task.copy(content = textFieldValue))
                },
        )

        // 通知ボタン
        // AndroidOSバージョンによってパーミッションを要求する
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            RequirePermissionNotificationButton(task = task, updateTask = updateTask)
        } else {
            NotificationButton(task = task, updateTask = updateTask)
        }

    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequirePermissionNotificationButton(
    task: Task,
    updateTask: (Task) -> Unit
) {
    if (!task.isCompleted) {
        if (task.sendNotification) {
            IconButton(
                onClick = { updateTask(task.copy(sendNotification = false)) },
            ) {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = "Send Notification"
                )
            }
        } else {
            val notificationPermissionState = rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)

            IconButton(
                onClick = {
                    if (!notificationPermissionState.status.isGranted) {
                        notificationPermissionState.launchPermissionRequest() // 通知権限の取得
                    }
                    updateTask(task.copy(sendNotification = true))
                },
            ) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Don't Send Notification"
                )
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NotificationButton(
    task: Task,
    updateTask: (Task) -> Unit
) {
    if (!task.isCompleted) {
        if (task.sendNotification) {
            IconButton(
                onClick = { updateTask(task.copy(sendNotification = false)) },
            ) {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = "Send Notification"
                )
            }
        } else {
            IconButton(
                onClick = { updateTask(task.copy(sendNotification = true)) },
            ) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Don't Send Notification"
                )
            }
        }
    }
}

@Composable
fun NewTask(onAddTask: (String) -> Unit) {
    var text by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, top = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextField(
            value = text,
            maxLines = 1,
            label = { Text("New Task") },
            onValueChange = { newText -> text = newText },
            modifier = Modifier.weight(1f),
            keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    onAddTask(text)
                    text = ""
                }
            ),
        )

        IconButton(
            onClick = {
                focusManager.clearFocus()
                onAddTask(text)
                text = ""
            },
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