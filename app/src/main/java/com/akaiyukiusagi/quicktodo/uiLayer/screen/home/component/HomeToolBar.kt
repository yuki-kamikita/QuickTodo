package com.akaiyukiusagi.quicktodo.uiLayer.screen.home.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.UnfoldLess
import androidx.compose.material.icons.filled.UnfoldMore
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.FloatingToolbarDefaults.floatingToolbarVerticalNestedScroll
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.akaiyukiusagi.quicktodo.R
import com.akaiyukiusagi.quicktodo.uiLayer.PreviewContent
import com.akaiyukiusagi.quicktodo.uiLayer.ScreenPreviewTemplate
import com.akaiyukiusagi.quicktodo.uiLayer.component.system.performVibration
import com.akaiyukiusagi.quicktodo.uiLayer.component.ui.parts.TransparentBackgroundTextField
import com.akaiyukiusagi.quicktodo.uiLayer.component.ui.parts.initialShapeList
import com.akaiyukiusagi.quicktodo.uiLayer.screen.home.ToolbarMode

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalAnimationApi::class)
@Composable
fun HomeToolBar(
    expanded: Boolean,
    toolbarMode: ToolbarMode = ToolbarMode.ACTION,
    isSwap: Boolean = true,
    onAddClick: () -> Unit = {},
    onSendClick: (String) -> Unit = {},
    onSwapClick: () -> Unit = {},
) {
    val context = LocalContext.current
    var toolbarMode by remember { mutableStateOf(toolbarMode) }
    var rememberShape by remember { mutableStateOf(initialShapeList.random()) }
    var text by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val isFocused = remember { mutableStateOf(false) }

    HorizontalFloatingToolbar(
        expanded = expanded,
        floatingActionButton = {
            FloatingToolbarDefaults.VibrantFloatingActionButton(
                onClick = {
                    if (toolbarMode == ToolbarMode.ADD_TASK) {
                        onSendClick(text)
                        performVibration(context, 5)
                        text = ""
                        focusManager.clearFocus()
                        toolbarMode = ToolbarMode.ACTION
                    } else if (toolbarMode == ToolbarMode.ACTION) {
                        onAddClick()
                        performVibration(context, 5)
                        toolbarMode = ToolbarMode.ADD_TASK
                    }
                }
            ) {
                AnimatedContent(
                    targetState = expanded && toolbarMode == ToolbarMode.ADD_TASK,
                    transitionSpec = { fadeIn().togetherWith(fadeOut()) }
                ) { showSend ->
                    if (showSend) {
                        Icon(Icons.AutoMirrored.Filled.Send, "Send")
                    } else {
                        Icon(Icons.Filled.Add, "Add")
                    }
                }
            }
        },
        content = {
            AnimatedContent(
                targetState = toolbarMode,
                transitionSpec = {
                    fadeIn(animationSpec = tween()).togetherWith(fadeOut(animationSpec = tween()))
                }
            ) { mode ->
                when (mode) {
                    ToolbarMode.ADD_TASK -> {
                        TransparentBackgroundTextField(
                            value = text,
                            labelText = stringResource(id = R.string.new_task),
                            focusRequester = focusRequester,
                            onValueChange = { text = it },
                            onFocusChanged = { isFocused.value = it },
                            keyboardDone = {
                                onSendClick(text)
                                text = ""
                                focusManager.clearFocus()
                                toolbarMode = ToolbarMode.ACTION
                            }
                        )
                        LaunchedEffect(Unit) {
                            focusRequester.requestFocus()
                        }
                    }
                    ToolbarMode.ACTION -> {
//                        Row {
//                            IconButton(onClick = { /* TODO */ }) {
//                                Icon(Icons.Default.UnfoldLess, contentDescription = "全部閉じる")
//                            }
//                            IconButton(onClick = { /* TODO */ }) {
//                                Icon(Icons.Default.UnfoldMore, contentDescription = "全部開く")
//                            }
//                            IconButton(
//                                onClick = {
//                                    if (!isSwap) rememberShape = initialShapeList.random()
//                                    onSwapClick()
//                                },
//                                shape = rememberShape.toShape(),
//                                colors = if (isSwap) IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer) else IconButtonDefaults.iconButtonColors()
//                            ) {
//                                Icon(Icons.Default.SwapVert, contentDescription = "並び替え")
//                            }
//                        }
                    }
                }
            }
        },
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@ScreenPreviewTemplate
@Composable
fun HomeToolBarPreview() {
    PreviewContent {
        var expanded by rememberSaveable { mutableStateOf(true) }
        var isSwap by rememberSaveable { mutableStateOf(true) }
        Scaffold(
            floatingActionButton = {
                HomeToolBar(
                    expanded = expanded,
                    isSwap = isSwap,
                    onAddClick = { expanded = true },
                    onSwapClick = { isSwap = !isSwap },
                )
            },
            floatingActionButtonPosition = FabPosition.End,
        ) { innerPadding ->
            Box(Modifier.padding(innerPadding)) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .then(
                            Modifier.floatingToolbarVerticalNestedScroll(
                                expanded = expanded,
                                onExpand = { expanded = true },
                                onCollapse = { expanded = false },
                            )
                        )
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(text = remember { LoremIpsum().values.first() })
                }
            }
        }
    }
}