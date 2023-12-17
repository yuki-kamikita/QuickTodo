@file:OptIn(ExperimentalMaterial3Api::class)

package com.akaiyukiusagi.quicktodo.ui.screen.home

import android.Manifest
import android.content.res.Configuration
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.akaiyukiusagi.quicktodo.R
import com.akaiyukiusagi.quicktodo.core.extension.category
import com.akaiyukiusagi.quicktodo.core.extension.view
import com.akaiyukiusagi.quicktodo.model.room.entity.Task
import com.akaiyukiusagi.quicktodo.ui.component.OnPause
import com.akaiyukiusagi.quicktodo.ui.theme.QuickTodoTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.time.LocalDateTime

@Composable
fun HomeScreen(viewModel: IHomeViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 4.dp, end = 4.dp, top = 4.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        TaskList(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            viewModel = viewModel
        )
        Divider()
        NewTask { text -> viewModel.addTask(text) }
    }
}

/** タスク一覧 */
@Composable
fun TaskList(
    modifier: Modifier,
    viewModel: IHomeViewModel
) {
    val context = LocalContext.current
    val tasks by viewModel.tasks.observeAsState(emptyList())
    val doneTasks by viewModel.doneTasks.observeAsState(emptyList())

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.Top
    ) {
        // 未完
        items(tasks, key = { task -> task.id }) { task ->
            TaskItem(
                task = task,
                updateTask = { updatedTask -> viewModel.updateTask(updatedTask) }
            )
        }

        item { Divider() }

        // 完了
        var currentCategory: String? = null
        doneTasks.forEach { task ->
            val taskCategory = task.completedAt.category(context)

            // カテゴリが変わったら見出しを表示
            if (taskCategory != currentCategory) {
                item {
                    // TODO: 見栄え調整
                    Text(
                        text = taskCategory,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = 8.dp, top = 8.dp)
                    )
                }
                currentCategory = taskCategory
            }

            item {
                TaskItem(
                    task = task,
                    updateTask = { updatedTask -> viewModel.updateTask(updatedTask) }
                )
            }
        }
    }
}

/** タスク一行 */
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

    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { isChecked ->
                    // そもそもここはcompletedAtでいいのか、updatedAtにすべきなのか悩む。両方入れおくべきな気もする
                    val completed = if (isChecked) LocalDateTime.now() else null
                    updateTask(task.copy(isCompleted = isChecked, completedAt = completed))
                }
            )

            TextField(
                value = textFieldValue,
                singleLine = true,
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
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
            )

            if (!task.isCompleted) NotificationButton(task, updateTask)
            else CompletedDateTime(task = task)
        }
    }
}

/** 通知on/offボタン */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NotificationButton(
    task: Task,
    updateTask: (Task) -> Unit
) {
    // プレビューモードでなければ、通知権限の状態を取得
    val isPreview = LocalInspectionMode.current
    val notificationPermissionState = if (!isPreview) {
        rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS) // ここの旧バージョン対応はもういいかな……
    } else null // プレビュー時はnull

    IconButton(
        onClick = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // FIXME: !!を使わないようにする
                if (!notificationPermissionState?.status?.isGranted!!) {
                    notificationPermissionState.launchPermissionRequest() // 通知権限の取得
                }
            }
            updateTask(task.copy(sendNotification = !task.sendNotification)) },
    ) {
        val icon = if (task.sendNotification) Icons.Filled.Notifications else Icons.Outlined.Notifications
        Icon(
            imageVector = icon,
            contentDescription = "Notification"
        )
    }
}

@Composable
fun CompletedDateTime(task: Task) {
    Text(
        text = task.completedAt.view(),
        modifier = Modifier.padding(end = 4.dp)
    )
}

/** タスク追加 */
@Composable
fun NewTask(onAddTask: (String) -> Unit) {
    var text by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val isFocused = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, top = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextField(
            value = text,
            singleLine = true,
            label = { Text(stringResource(id = R.string.new_task)) },
            onValueChange = { newText -> text = newText },
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester)
                .onFocusChanged { focusState -> isFocused.value = focusState.isFocused },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
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

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun PreviewScreen() {
    QuickTodoTheme {
        Surface {
            HomeScreen(
                viewModel = PreviewHomeViewModel()
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewNewTask() {
    QuickTodoTheme {
        Surface (
            color = MaterialTheme.colorScheme.background
        ) {
            NewTask(onAddTask = {})
        }
    }
}