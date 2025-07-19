package com.akaiyukiusagi.quicktodo.uiLayer.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingToolbarDefaults.floatingToolbarVerticalNestedScroll
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
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.akaiyukiusagi.quicktodo.R
import com.akaiyukiusagi.quicktodo.ScreenNavigator
import com.akaiyukiusagi.quicktodo.core.extension.category
import com.akaiyukiusagi.quicktodo.core.extension.view
import com.akaiyukiusagi.quicktodo.dataLayer.room.entity.Task
import com.akaiyukiusagi.quicktodo.uiLayer.PreviewContent
import com.akaiyukiusagi.quicktodo.uiLayer.ScreenPreviewTemplate
import com.akaiyukiusagi.quicktodo.uiLayer.component.system.performVibration
import com.akaiyukiusagi.quicktodo.uiLayer.component.system.rememberNotificationPermissionRequester
import com.akaiyukiusagi.quicktodo.uiLayer.screen.home.component.CardDesign
import com.akaiyukiusagi.quicktodo.uiLayer.screen.home.component.HomeToolBar
import com.akaiyukiusagi.quicktodo.uiLayer.viewModel.IHomeViewModel
import com.akaiyukiusagi.quicktodo.uiLayer.viewModel.ISettingsViewModel
import com.akaiyukiusagi.quicktodo.uiLayer.viewModel.PreviewHomeViewModel
import com.akaiyukiusagi.quicktodo.uiLayer.viewModel.PreviewSettingsViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeScreen(
    viewModel: IHomeViewModel,
    settings: ISettingsViewModel,
    navigator: NavController = rememberNavController()
) {
    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember { SnackbarHostState() } // TODO: SnackbarHostStateは結構入り組むからもっと増えてきたらCompositionLocalを検討
    var expanded by rememberSaveable { mutableStateOf(false) }
    var isSwap by rememberSaveable { mutableStateOf(true) }

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
        floatingActionButton = {
            Box(
                modifier = Modifier.padding(WindowInsets.ime.asPaddingValues())
            ) {
                HomeToolBar(
                    expanded = expanded,
                    isSwap = isSwap,
                    onAddClick = { expanded = true },
                    onSendClick = { text -> viewModel.addTask(text) },
                    onSwapClick = { isSwap = !isSwap },
                )
            }
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
                            .fillMaxHeight()
                            .then(
                                Modifier.floatingToolbarVerticalNestedScroll(
                                    expanded = expanded,
                                    onExpand = { expanded = true },
                                    onCollapse = { expanded = false },
                                )
                            ),
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
