package com.akaiyukiusagi.quicktodo.uiLayer.screen.home.component

import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import com.akaiyukiusagi.quicktodo.R
import com.akaiyukiusagi.quicktodo.uiLayer.ComponentPreviewTemplate
import com.akaiyukiusagi.quicktodo.uiLayer.PreviewContent
import com.akaiyukiusagi.quicktodo.uiLayer.component.system.performVibration
import com.akaiyukiusagi.quicktodo.uiLayer.component.ui.parts.TransparentBackgroundTextField

/** タスク追加 */
@Deprecated("Material 3 Expressive で非推奨 HomeToolBarに移行")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeBottomAppBar(onAddTask: (String) -> Unit = {}) {
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

@ComponentPreviewTemplate
@Composable
fun HomeBottomAppBarPreview() {
    PreviewContent {
        HomeBottomAppBar()
    }
}