@file:OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)

package com.akaiyukiusagi.quicktodo.ui_layer.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.akaiyukiusagi.quicktodo.ScreenNavigator
import com.akaiyukiusagi.quicktodo.data_layer.BooleanPreference
import com.akaiyukiusagi.quicktodo.data_layer.room.entity.Task
import com.akaiyukiusagi.quicktodo.ui_layer.component.PreviewTemplate
import com.akaiyukiusagi.quicktodo.ui_layer.theme.QuickTodoTheme
import com.akaiyukiusagi.quicktodo.ui_layer.view_model.IHomeViewModel
import com.akaiyukiusagi.quicktodo.ui_layer.view_model.ISettingsViewModel
import com.akaiyukiusagi.quicktodo.ui_layer.view_model.PreviewHomeViewModel
import com.akaiyukiusagi.quicktodo.ui_layer.view_model.PreviewSettingsViewModel

private val shapeForSharedElement = RoundedCornerShape(16.dp)

/**
 * タイトル以外の要素を追加したリスト画面
 *
 * FIXME: List→Detailに遷移するときに一瞬カードが透明になる？
 * FIXME: チェックボックスがDetail→Listに戻るタイミングでアニメーションしていない
 */
@Composable
fun FullListScreen(
    viewModel: IHomeViewModel,
    settings: ISettingsViewModel,
    navigator: NavController = rememberNavController(),
    initialSelectedTask: Task? = null // プレビュー用
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedTask by remember { mutableStateOf<Task?>(initialSelectedTask) }

    val tasks by viewModel.tasks.collectAsState(initial = viewModel.initialTasks)
    val doneTasks by viewModel.doneTasks.collectAsState(initial = viewModel.initialDoneTasks)
    val showDoneTasks by settings.showDoneTasks.collectAsState(initial = BooleanPreference.SHOW_DONE_TASKS.initialValue)

    SharedTransitionLayout(
        modifier = Modifier
            .fillMaxSize()
            .then(
                if (selectedTask != null) Modifier.clickable { selectedTask = null }
                else Modifier
            )
    ){
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            bottomBar = {
                BottomAppBar {
                    AnimatedContent(
                        targetState = selectedTask,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "BottomAppBar"
                    ) { targetTask ->
                        if (targetTask == null) {
                            ListAppBarContent(navigator)
                        } else {
                            DetailAppBarContent(
                                targetTask,
                                this@AnimatedContent
                            )
                        }
                    }
                }
            },
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
//                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(tasks) { task ->
                    AnimatedVisibility(
                        visible = task != selectedTask,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut() + scaleOut(),
                        modifier = Modifier.animateItem()
                    ) {
                        TodoListItem(
                            task = task,
                            this@AnimatedVisibility
                        ) {
                            selectedTask = task
                        }
                    }
                }
            }

            TodoEditDetail(task = selectedTask, modifier = Modifier.padding(innerPadding)) {
                selectedTask = null
            }
        }
    }
}


@Composable
fun SharedTransitionScope.TodoListItem(
    task: Task,
    animatedContentScope: AnimatedVisibilityScope,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .sharedElement(
                rememberSharedContentState(key = "card_${task.id}"),
                animatedVisibilityScope = animatedContentScope,
                clipInOverlayDuringTransition = OverlayClip(shapeForSharedElement)
            )
            .clip(shapeForSharedElement),
        onClick = onClick
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = {},
                modifier = Modifier
                    .sharedElement(
                        rememberSharedContentState(key = "checkbox_${task.id}"),
                        animatedVisibilityScope = animatedContentScope
                    )
            )
            TextField(
                value = task.content,
                modifier = Modifier
                    .sharedElement(
                        rememberSharedContentState(key = "text_${task.id}"),
                        animatedVisibilityScope = animatedContentScope
                    ),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
                enabled = false,
                onValueChange = { }
            )
        }
    }

}

@Composable
fun SharedTransitionScope.TodoEditDetail(
    task: Task? = Task(1, "Task1", false),
    modifier: Modifier = Modifier,
    onConfirmClick: () -> Unit = {}
) {
    AnimatedContent(
        targetState = task,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "TodoEditDetails"
    ) { targetTask ->

        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (targetTask != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { onConfirmClick() }
                        .background(Color.Black.copy(alpha = 0.5f))
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState(key = "card_${targetTask.id}"),
                            animatedVisibilityScope = this@AnimatedContent,
                            clipInOverlayDuringTransition = OverlayClip(shapeForSharedElement)
                        )
                        .clip(shapeForSharedElement)
                ) {
                    Column {
                        TextField(
                            targetTask.content,
                            modifier = Modifier
                                .fillMaxWidth()
                                .sharedElement(
                                    rememberSharedContentState(key = "text_${targetTask.id}"),
                                    animatedVisibilityScope = this@AnimatedContent
                                ),
                            colors = TextFieldDefaults.textFieldColors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                            ),
                            enabled = false,
                            onValueChange = {}
                        )
                        Row {
                            Text("Delete")
                            Text("Edit")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SharedTransitionScope.ListAppBarContent(
    navigator: NavController,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row {
            IconButton(onClick = { ScreenNavigator.Settings.navigate(navigator) }) {
                Icon(Icons.Outlined.Settings, contentDescription = "Settings")
            }
            IconButton(onClick = {  }) {
                Icon(Icons.Outlined.Edit, contentDescription = "Edit")
            }
        }
        FloatingActionButton(
            onClick = { /*TODO*/ },
            modifier = Modifier.padding(8.dp)
        ){
            Icon(Icons.Outlined.Add, "")
        }
    }
}

@Composable
fun SharedTransitionScope.DetailAppBarContent(
    targetTask: Task,
    animatedContentScope: AnimatedVisibilityScope
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = {  }) {
            Icon(Icons.Outlined.Delete, "Delete")
        }
        IconButton(onClick = {  }) {
            Icon(Icons.Outlined.Notifications, "Notifications")
        }
        Checkbox(
            checked = targetTask.isCompleted,
            onCheckedChange = {  },
            modifier = Modifier
                .sharedElement(
                    rememberSharedContentState(key = "checkbox_${targetTask.id}"),
                    animatedVisibilityScope = animatedContentScope
                )
        )
    }
}




@PreviewTemplate
@Composable
fun PreviewFullListScreen() {
    QuickTodoTheme {
        FullListScreen(PreviewHomeViewModel(), PreviewSettingsViewModel())
    }
}

@PreviewTemplate
@Composable
fun PreviewFullListScreenWithSelectedTask() {
    QuickTodoTheme {
        FullListScreen(
            viewModel = PreviewHomeViewModel(),
            settings = PreviewSettingsViewModel(),
            initialSelectedTask = PreviewHomeViewModel().initialTasks[1]
        )
    }
}