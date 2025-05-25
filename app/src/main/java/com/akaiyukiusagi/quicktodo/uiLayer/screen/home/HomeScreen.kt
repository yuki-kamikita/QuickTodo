package com.akaiyukiusagi.quicktodo.uiLayer.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.akaiyukiusagi.quicktodo.R
import com.akaiyukiusagi.quicktodo.ScreenNavigator
import com.akaiyukiusagi.quicktodo.core.extension.category
import com.akaiyukiusagi.quicktodo.core.extension.view
import com.akaiyukiusagi.quicktodo.dataLayer.room.entity.Task
import com.akaiyukiusagi.quicktodo.uiLayer.ComponentPreviewTemplate
import com.akaiyukiusagi.quicktodo.uiLayer.PreviewContent
import com.akaiyukiusagi.quicktodo.uiLayer.ScreenPreviewTemplate
import com.akaiyukiusagi.quicktodo.uiLayer.component.ui.behavior.OnPause
import com.akaiyukiusagi.quicktodo.uiLayer.component.ui.behavior.SwipeToDelete
import com.akaiyukiusagi.quicktodo.uiLayer.component.system.performVibration
import com.akaiyukiusagi.quicktodo.uiLayer.component.system.rememberNotificationPermissionRequester
import com.akaiyukiusagi.quicktodo.uiLayer.component.ui.parts.TransparentBackgroundTextField
import com.akaiyukiusagi.quicktodo.uiLayer.viewModel.IHomeViewModel
import com.akaiyukiusagi.quicktodo.uiLayer.viewModel.ISettingsViewModel
import com.akaiyukiusagi.quicktodo.uiLayer.viewModel.PreviewHomeViewModel
import com.akaiyukiusagi.quicktodo.uiLayer.viewModel.PreviewSettingsViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: IHomeViewModel,
    settings: ISettingsViewModel,
    navigator: NavController = rememberNavController()
) {
    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember { SnackbarHostState() } // TODO: SnackbarHostStateは結構入り組むからもっと増えてきたらCompositionLocalを検討

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                actions = {
                    IconButton(onClick = { ScreenNavigator.Settings.navigate(navigator) }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                },
            )
        },
        bottomBar = {
            NewTask { text -> viewModel.addTask(text) }
        },
        content = {
            Surface(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) { focusManager.clearFocus() }
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    TaskList(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        viewModel = viewModel,
                        settings = settings,
                        snackbarHostState = snackbarHostState
                    )
                }
            }
        }
    )
}

/** タスク一覧 */
@Composable
fun TaskList(
    viewModel: IHomeViewModel,
    settings: ISettingsViewModel,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val tasks by viewModel.tasks.collectAsState(initial = viewModel.initialTasks)
    val doneTasks by viewModel.doneTasks.collectAsState(initial = viewModel.initialDoneTasks)
    val showDoneTasks by settings.showDoneTasks.collectAsState(initial = false)
    val scope = rememberCoroutineScope()

    val message = stringResource(id = R.string.snackbar_delete_suffix)
    val label = stringResource(id = R.string.snackbar_undo)

    LazyColumn(
        modifier = modifier.padding(horizontal = 2.dp),
        verticalArrangement = Arrangement.Top
    ) {
        item { Spacer(modifier = Modifier.padding(2.dp)) } // TODO: AppBar入れたら不要になる

        // 未完
        items(tasks, key = { task -> task.id }) { task ->
            TodoItem(
                task = task,
                snackbarHostState = snackbarHostState,
                updateTask = { updatedTask -> viewModel.updateTask(updatedTask) },
                onDelete = {
                    viewModel.deleteTask(task)
                    performVibration(context, 5)

                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = task.content + " " + message,
                            actionLabel = label,
                            duration = SnackbarDuration.Short
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            viewModel.addTask(task)
                        }
                    }
                }
            )
        }


        // 完了
        if (showDoneTasks) {
            item { HorizontalDivider() }

            var currentCategory: String? = null
            doneTasks.forEach { task ->
                val taskCategory = task.completedAt?.category(context) ?: "" // FIXME

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
                    CompletedItem(
                        task = task,
                        updateTask = { updatedTask -> viewModel.updateTask(updatedTask) },
                        onDelete = {
                            viewModel.deleteTask(task)

                            scope.launch {
                                val result = snackbarHostState.showSnackbar(
                                    message = task.content + message,
                                    actionLabel = label,
                                    duration = SnackbarDuration.Short
                                )
                                if (result == SnackbarResult.ActionPerformed) {
                                    viewModel.addTask(task)
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

/** 未完の一行 */
@Composable
fun TodoItem(
    task: Task,
    snackbarHostState: SnackbarHostState,
    updateTask: (Task) -> Unit,
    onDelete: () -> Unit,
) {
    // 編集可能にするため、rememberにする
    var textFieldValue by remember { mutableStateOf(task.content) }

    CardDesign(
        isChecked = false,
        text = textFieldValue,
        changeCheck = { updateTask(task.copy(isCompleted = true, completedAt = LocalDateTime.now())) },
        offFocus = { updateTask(task.copy(content = textFieldValue)) },
        onPause = { updateTask(task.copy(content = textFieldValue)) },
        onDelete = onDelete,
        changeText = { newText -> textFieldValue = newText }
    ) {
        NotificationButton(task, snackbarHostState, updateTask)
    }
}

/** 完了の一行 */
@Composable
fun CompletedItem(
    task: Task,
    updateTask: (Task) -> Unit,
    onDelete: () -> Unit,
) {
    // ORDER BY completedAt のせいか、チェックつけ外しすると表示するtaskが狂ったのでrememberを外す
    val textFieldValue = task.content

    CardDesign(
        isChecked = true,
        text = textFieldValue,
        changeCheck = { updateTask(task.copy(isCompleted = false, completedAt = null)) },
        onDelete = onDelete,
        suffix = {
            Text(
                text = task.completedAt.view(),
                modifier = Modifier.padding(end = 4.dp)
            )
        }
    )
}

/** 完/未完 の共通部分 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardDesign(
    isChecked: Boolean,
    text: String,
    changeCheck: () -> Unit = {},
    offFocus: () -> Unit = {},
    onPause: () -> Unit = {},
    changeText: (String) -> Unit = {},
    onDelete: () -> Unit = {},
    suffix: @Composable () -> Unit
) {
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var hadFocus by remember { mutableStateOf(false) }

    OnPause { if (hadFocus) onPause() }

    SwipeToDelete(
        modifier = Modifier.padding(4.dp),
        onDelete = onDelete
    ) {
        Card (modifier = Modifier.fillMaxWidth()) {
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = {
                        changeCheck()
                        performVibration(context, 5)
                    }
                )

//            Text(text = task.id.toString()) // しばらくデバッグ用に入れとく

                TextField(
                    value = text,
                    singleLine = true,
                    onValueChange = changeText,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    enabled = !isChecked,
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester)
                        .onFocusChanged { focusState ->
                            if (focusState.isFocused) hadFocus = true
                            else if (hadFocus) {
                                // フォーカスが失われた場合にのみ実行
                                offFocus()
                                hadFocus = false
                            }
                        },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                )

                suffix()
            }
        }
    }

}

/** 通知on/offボタン */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NotificationButton(
    task: Task,
    snackbarHostState: SnackbarHostState,
    updateTask: (Task) -> Unit
) {
    val context = LocalContext.current
    val changeNotification = rememberNotificationPermissionRequester(snackbarHostState) {
        updateTask(task.copy(sendNotification = !task.sendNotification))
    }

    IconButton(
        onClick = {
            changeNotification()
            performVibration(context, 5)
        },
    ) {
        val icon = if (task.sendNotification) Icons.Filled.Notifications else Icons.Outlined.Notifications
        Icon(
            imageVector = icon,
            contentDescription = "Notification"
        )
    }
}

/** タスク追加 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTask(onAddTask: (String) -> Unit = {}) {
    val context = LocalContext.current
    var text by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val isFocused = remember { mutableStateOf(false) }

    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.imePadding()
    ) {
        TransparentBackgroundTextField(
            value = text,
            labelText = stringResource(id = R.string.new_task),
            focusRequester = focusRequester,
            onValueChange = { text = it },
            onFocusChanged = { isFocused.value = it },
            keyboardDone = {
                focusManager.clearFocus()
                if (text.isNotBlank()) {
                    onAddTask(text)
                    performVibration(context, 5)
                    text = ""
                }
            }
        )
    }
}

enum class ToolbarMode {
    ACTION,
    ADD_TASK,
}

@ScreenPreviewTemplate
@Composable
fun PreviewScreen() {
    PreviewContent {
        HomeScreen(PreviewHomeViewModel(), PreviewSettingsViewModel())
    }
}

@ComponentPreviewTemplate
@Composable
fun PreviewCard() {
    PreviewContent {
        Column {
            CardDesign(false, "未完了のタスク") {}
            CardDesign(true, "完了したタスク") {}
        }
    }
}

@ComponentPreviewTemplate
@Composable
fun PreviewNewTask() {
    PreviewContent {
        NewTask()
    }
}