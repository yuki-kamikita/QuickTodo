package com.akaiyukiusagi.quicktodo.uiLayer.screen.home.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.akaiyukiusagi.quicktodo.uiLayer.PreviewContent
import com.akaiyukiusagi.quicktodo.uiLayer.ScreenPreviewTemplate
import com.akaiyukiusagi.quicktodo.uiLayer.component.ui.layout.Center
import com.akaiyukiusagi.quicktodo.uiLayer.component.ui.parts.RandomShapeIndicator
import com.akaiyukiusagi.quicktodo.uiLayer.component.ui.parts.initialShapeList
import com.akaiyukiusagi.quicktodo.uiLayer.screen.home.ToolbarMode

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeToolBar(
    expanded: Boolean,
    toolbarMode: ToolbarMode = ToolbarMode.ACTION,
    isSwap: Boolean = true,
    onAddClick: () -> Unit = {},
    onSwapClick: () -> Unit = {},
) {
    var toolbarMode by remember { mutableStateOf(toolbarMode) }
    var text by remember { mutableStateOf("") }
    var rememberShape by remember { mutableStateOf(initialShapeList.random()) }

    HorizontalFloatingToolbar(
        expanded = expanded,
        floatingActionButton = {
            FloatingToolbarDefaults.VibrantFloatingActionButton(
                onClick = {
                    onAddClick()
                    toolbarMode = ToolbarMode.ADD_TASK
                }
            ) {
                Icon(Icons.Filled.Add, "Localized description")
            }
        },
//        colors = FloatingToolbarDefaults.vibrantFloatingToolbarColors(),
        content = {
            when (toolbarMode) {
                ToolbarMode.ACTION -> {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.UnfoldLess, contentDescription = "全部閉じる")
                    }
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.UnfoldMore, contentDescription = "全部開く")
                    }
                    IconButton(
                        onClick = {
                            if (!isSwap) rememberShape = initialShapeList.random()
                            onSwapClick()
                        },
                        shape = rememberShape.toShape(),
                        colors = if (isSwap) IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer) else IconButtonDefaults.iconButtonColors()
                    ) {
                        Icon(Icons.Default.SwapVert, contentDescription = "並び替え")
                    }
                }
                ToolbarMode.ADD_TASK -> {
                    TextField(
                        value = text,
                        onValueChange = { text = it },
                        label = { Text("入力してください") }
                    )
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
                    onAddClick = { expanded = !expanded },
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