package com.akaiyukiusagi.quicktodo

import android.os.Bundle
import androidx.navigation3.scene.Scene
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.akaiyukiusagi.quicktodo.uiLayer.screen.home.HomeScreen
import com.akaiyukiusagi.quicktodo.uiLayer.screen.SettingsScreen
import com.akaiyukiusagi.quicktodo.uiLayer.theme.QuickTodoTheme
import com.akaiyukiusagi.quicktodo.uiLayer.viewModel.HomeViewModel
import com.akaiyukiusagi.quicktodo.uiLayer.viewModel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            QuickTodoTheme {
                val homeViewModel: HomeViewModel = hiltViewModel()
                val settingsViewModel: SettingsViewModel = hiltViewModel()
                val backStack = remember { mutableStateListOf<ScreenNavigator>(ScreenNavigator.Home) }

                fun <T : Any> AnimatedContentTransitionScope<Scene<T>>.forwardTransform() =
                    ContentTransform(
                        targetContentEnter = slideInHorizontally(
                            initialOffsetX = { fullWidth -> fullWidth / 5 },
                            animationSpec = tween(durationMillis = 200)
                        ) + fadeIn(
                            animationSpec = tween(durationMillis = 500)
                        ),
                        initialContentExit = fadeOut(
                            animationSpec = tween(durationMillis = 700)
                        )
                    )

                fun <T : Any> AnimatedContentTransitionScope<Scene<T>>.popTransform() =
                    ContentTransform(
                        targetContentEnter = fadeIn(
                            animationSpec = tween(durationMillis = 700)
                        ),
                        initialContentExit = slideOutHorizontally(
                            targetOffsetX = { fullWidth -> fullWidth / 5 },
                            animationSpec = tween(durationMillis = 500)
                        ) + fadeOut(
                            animationSpec = tween(durationMillis = 200)
                        )
                    )

                Surface(
                    // 予測型「戻る」ジェスチャー時に一瞬見えるので背景色を指定
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                ) {
                    NavDisplay(
                        backStack = backStack,
                        onBack = { backStack.removeLastOrNull() },
                        entryProvider = { screen: ScreenNavigator ->
                            when (screen) {
                                is ScreenNavigator.Home -> NavEntry(screen) {
                                    HomeScreen(homeViewModel, settingsViewModel) {
                                        backStack.add(ScreenNavigator.Settings)
                                    }
                                }
                                is ScreenNavigator.Settings -> NavEntry(screen) {
                                    SettingsScreen(settingsViewModel) {
                                        backStack.removeLastOrNull()
                                    }
                                }
                            }
                        },
                        transitionSpec = { forwardTransform() },
                        popTransitionSpec = { popTransform() },
                        predictivePopTransitionSpec = { _ -> popTransform() },
                    )
                }
            }
        }
    }
}


@Serializable
sealed class ScreenNavigator {
    @Serializable
    object Home : ScreenNavigator()
    @Serializable
    object Settings : ScreenNavigator()
}