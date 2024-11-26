@file:OptIn(ExperimentalSharedTransitionApi::class)

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.akaiyukiusagi.quicktodo.data_layer.room.entity.Task
import com.akaiyukiusagi.quicktodo.ui_layer.view_model.IHomeViewModel
import com.akaiyukiusagi.quicktodo.ui_layer.view_model.PreviewHomeViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
fun AnimatedVisibilitySample(viewModel: IHomeViewModel = PreviewHomeViewModel()) {
    val context = LocalContext.current
    var selectedTask by remember { mutableStateOf<Task?>(null) }

    SharedTransitionLayout(
        modifier = Modifier
            .fillMaxSize()
            .clickable { selectedTask = null }
    ){
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(viewModel.initialDoneTasks) { task ->
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

        TodoEditDetail(task = selectedTask) {
            selectedTask = null
        }

    }
    
}
//
//@Composable
//fun SharedTransitionScope.TodoListItem(
//    task: Task,
//    animatedContentScope: AnimatedVisibilityScope,
//    onClick: () -> Unit
//) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .sharedElement(
//                rememberSharedContentState(key = "card_${task.id}"),
//                animatedVisibilityScope = animatedContentScope
//            ),
//        onClick = onClick
//    ) {
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Checkbox(
//                checked = task.isCompleted,
//                onCheckedChange = {},
//                modifier = Modifier
//                    .sharedElement(
//                        rememberSharedContentState(key = "checkbox_${task.id}"),
//                        animatedVisibilityScope = animatedContentScope
//                    )
//            )
//            Text(
//                task.content,
//                modifier = Modifier
//                    .sharedElement(
//                        rememberSharedContentState(key = "text_${task.id}"),
//                        animatedVisibilityScope = animatedContentScope
//                    )
//            )
//        }
//    }
//
//}
//
//@Preview
//@Composable
//fun SharedTransitionScope.TodoEditDetail(
//    task: Task? = Task(1, "Task1", false),
//    onConfirmClick: () -> Unit = {}
//) {
//    AnimatedContent(
//        targetState = task,
//        transitionSpec = { fadeIn() togetherWith fadeOut() },
//        label = "TodoEditDetails"
//    ) { targetTask ->
//
//        Box(
//            modifier = Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//            if (targetTask != null) {
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .clickable { onConfirmClick() }
//                        .background(Color.Black.copy(alpha = 0.5f))
//                )
//                Column {
//                    Card(
//                        modifier = Modifier
//                            .weight(1f)
//                            .fillMaxSize()
//                            .padding(24.dp)
//                            .sharedBounds(
//                                sharedContentState = rememberSharedContentState(key = "card_${targetTask.id}"),
//                                animatedVisibilityScope = this@AnimatedContent,
//                            )
//                    ) {
//                        Column {
//                            var text by remember { mutableStateOf("") }
//                            TextField(value = text, modifier = Modifier.fillMaxWidth(), onValueChange = { text = it })
//                            Text(
//                                targetTask.content,
//                                modifier = Modifier
//                                    .sharedElement(
//                                        rememberSharedContentState(key = "text_${targetTask.id}"),
//                                        animatedVisibilityScope = this@AnimatedContent
//                                    )
//                            )
//                            Row {
//                                Text("Delete")
//                                Text("Edit")
//                            }
//                        }
//                    }
//
//                    BottomAppBar {
//                        Row(
//                            modifier = Modifier.fillMaxWidth(),
//                            horizontalArrangement = Arrangement.End,
//                            verticalAlignment = Alignment.CenterVertically,
//                        ) {
//                            IconButton(onClick = {  }) {
//                                Icon(Icons.Default.Delete, "Delete")
//                            }
//                            Spacer(modifier = Modifier.weight(1f))
//                            IconButton(onClick = {  }) {
//                                Icon(Icons.Default.Edit, "Edit")
//                            }
//                            IconButton(onClick = {  }) {
//                                Icon(Icons.Default.Notifications, "Notifications")
//                            }
//                            Checkbox(
//                                checked = targetTask.isCompleted,
//                                onCheckedChange = {  },
//                                modifier = Modifier
//                                    .sharedElement(
//                                        rememberSharedContentState(key = "checkbox_${targetTask.id}"),
//                                        animatedVisibilityScope = this@AnimatedContent
//                                    )
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//}
