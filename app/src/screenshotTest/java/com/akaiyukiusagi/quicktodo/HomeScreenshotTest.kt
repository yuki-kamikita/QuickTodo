package com.akaiyukiusagi.quicktodo

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.akaiyukiusagi.quicktodo.uiLayer.component.PreviewComponent
import com.akaiyukiusagi.quicktodo.uiLayer.screen.CardDesign
import com.akaiyukiusagi.quicktodo.uiLayer.screen.HomeScreen
import com.akaiyukiusagi.quicktodo.uiLayer.screen.NewTask
import com.akaiyukiusagi.quicktodo.uiLayer.viewModel.PreviewHomeViewModel
import com.akaiyukiusagi.quicktodo.uiLayer.viewModel.PreviewSettingsViewModel

class HomeScreenshotTest {

    @PreviewLightDark
    @PreviewDynamicColors
    @Preview(fontScale = 2.0F)
    @Composable
    fun PreviewScreen() {
        PreviewComponent {
            HomeScreen(
                viewModel = PreviewHomeViewModel(),
                settings = PreviewSettingsViewModel()
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