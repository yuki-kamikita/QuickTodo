package com.akaiyukiusagi.quicktodo

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.akaiyukiusagi.quicktodo.ui_layer.component.PreviewComponent
import com.akaiyukiusagi.quicktodo.ui_layer.screen.CardDesign
import com.akaiyukiusagi.quicktodo.ui_layer.screen.HomeScreen
import com.akaiyukiusagi.quicktodo.ui_layer.screen.NewTask
import com.akaiyukiusagi.quicktodo.ui_layer.view_model.PreviewHomeViewModel

class HomeScreenshotTest {

    @PreviewLightDark
    @PreviewDynamicColors
    @Preview(fontScale = 2.0F)
    @Composable
    fun PreviewScreen() {
        PreviewComponent {
            HomeScreen(
                viewModel = PreviewHomeViewModel()
            )
        }
    }

    @Preview
    @Composable
    fun PreviewCard() {
        PreviewComponent {
            Column {
                CardDesign(false, "未完了のタスク") {}
                CardDesign(true, "完了したタスク") {}
            }
        }
    }

    @Preview
    @Composable
    fun PreviewNewTask() {
        PreviewComponent {
            NewTask()
        }
    }

}