package com.akaiyukiusagi.quicktodo

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.akaiyukiusagi.quicktodo.uiLayer.PreviewContent
import com.akaiyukiusagi.quicktodo.uiLayer.ScreenPreviewTemplate
import com.akaiyukiusagi.quicktodo.uiLayer.screen.home.CardDesign
import com.akaiyukiusagi.quicktodo.uiLayer.screen.home.HomeScreen
import com.akaiyukiusagi.quicktodo.uiLayer.screen.home.NewTask
import com.akaiyukiusagi.quicktodo.uiLayer.viewModel.PreviewHomeViewModel
import com.akaiyukiusagi.quicktodo.uiLayer.viewModel.PreviewSettingsViewModel

class HomeScreenshotTest {

    @ScreenPreviewTemplate
    @Composable
    fun PreviewScreen() {
        PreviewContent {
            HomeScreen(
                viewModel = PreviewHomeViewModel(),
                settings = PreviewSettingsViewModel()
            )
        }
    }

    @Preview
    @Composable
    fun PreviewCard() {
        PreviewContent {
            Column {
                CardDesign(false, "未完了のタスク") {}
                CardDesign(true, "完了したタスク") {}
            }
        }
    }

    @Preview
    @Composable
    fun PreviewNewTask() {
        PreviewContent {
            NewTask()
        }
    }

}