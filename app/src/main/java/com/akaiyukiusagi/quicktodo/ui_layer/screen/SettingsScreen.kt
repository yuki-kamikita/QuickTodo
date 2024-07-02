package com.akaiyukiusagi.quicktodo.ui_layer.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.akaiyukiusagi.quicktodo.ui_layer.component.PreviewComponent
import com.akaiyukiusagi.quicktodo.ui_layer.view_model.ISettingsViewModel
import com.akaiyukiusagi.quicktodo.ui_layer.view_model.PreviewSettingsViewModel

@Composable
fun SettingsScreen(viewModel: ISettingsViewModel) {
    val themeColor = viewModel.themeColor.collectAsState(initial = null)

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Text(text = "設定")
            Text(text = "テーマカラー")
            Text(text = "完了済みを表示する")
//        DropdownMenu(
//            items = listOf("Light", "Dark", "System"),
//            selectedItem = themeColor.value,
//            onItemSelected = viewModel::setThemeColor
//        )
        }
    }
}

@PreviewLightDark
@PreviewDynamicColors
@Composable
fun PreviewSettingsScreen() {
    PreviewComponent {
        SettingsScreen(viewModel = PreviewSettingsViewModel())
    }
}