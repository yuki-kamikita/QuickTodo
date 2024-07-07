package com.akaiyukiusagi.quicktodo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.akaiyukiusagi.quicktodo.ui_layer.screen.HomeScreen
import com.akaiyukiusagi.quicktodo.ui_layer.screen.SettingsScreen
import com.akaiyukiusagi.quicktodo.ui_layer.theme.QuickTodoTheme
import com.akaiyukiusagi.quicktodo.ui_layer.view_model.HomeViewModel
import com.akaiyukiusagi.quicktodo.ui_layer.view_model.PreviewSettingsViewModel
import com.akaiyukiusagi.quicktodo.ui_layer.view_model.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // 描画領域をシステムバー最下部まで広げる
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            QuickTodoTheme {
                val homeViewModel: HomeViewModel = hiltViewModel()
                val settingsViewModel: SettingsViewModel = hiltViewModel()
                val navController = rememberNavController()
                val currentRoute = remember { mutableStateOf(ScreenNavigator.Home) }

                navController.addOnDestinationChangedListener { _, destination, _ ->
                    val screenName =  destination.route ?: ScreenNavigator.Home.name
                    currentRoute.value = ScreenNavigator.fromName(screenName) ?: ScreenNavigator.Home
                }

                NavHost(
                    navController,
                    startDestination = ScreenNavigator.Home.name,
                    modifier = Modifier.safeDrawingPadding()
                ){
                    composable(ScreenNavigator.Home.name) {
                        HomeScreen(homeViewModel, settingsViewModel, navController)
                    }
                    composable(ScreenNavigator.Settings.name) {
                        SettingsScreen(settingsViewModel, navController)
                    }
                }
            }
        }
    }
}

enum class ScreenNavigator {
    Home,
    Settings;

    // nameを使用することは定義されていないからミスる可能性があるかなと思ったけどやりすぎかなって気もしなくもない
    // けど見るの自分だけだし、navController.navigate覚えられないから予測で出てくるこっちでいいか
    fun navigate(navController: NavController) {
        navController.navigate(this.name)
    }

    companion object {
        fun fromName(name: String): ScreenNavigator? {
            return entries.find { it.name == name }
        }
    }
}