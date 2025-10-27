package com.akaiyukiusagi.quicktodo.uiLayer.screen.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.akaiyukiusagi.quicktodo.uiLayer.ComponentPreviewTemplate
import com.akaiyukiusagi.quicktodo.uiLayer.PreviewContent
import com.akaiyukiusagi.quicktodo.uiLayer.component.system.performVibration
import com.akaiyukiusagi.quicktodo.uiLayer.component.ui.behavior.OnPause
import com.akaiyukiusagi.quicktodo.uiLayer.component.ui.behavior.SwipeToDelete


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
