package com.akaiyukiusagi.quicktodo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.akaiyukiusagi.quicktodo.ui_layer.screen.HomeScreen
import com.akaiyukiusagi.quicktodo.ui_layer.view_model.HomeViewModel
import com.akaiyukiusagi.quicktodo.ui_layer.theme.QuickTodoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge() // Android14以降はこれだけで良さげ？
        super.onCreate(savedInstanceState)

        // 描画領域をシステムバー最下部まで広げる
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            QuickTodoTheme {
                val homeViewModel: HomeViewModel = hiltViewModel()
                val snackbarHostState = remember { SnackbarHostState() }

                Scaffold(
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                    modifier = Modifier.safeDrawingPadding(), // ナビゲーションバーに被せない
//                    floatingActionButton = {
//                        FloatingActionButton(onClick = { /* スクロール描写で 完了/未完 切り替え */ }) {
//                            Icon(imageVector = Icons.Default.History, contentDescription = stringResource(id = R.string.history))
//                        }
//                    },
//                    topBar = { // 追加画面遷移用
//                        TopAppBar(
//                            title = { Text(text = stringResource(id = R.string.app_name)) },
//                            actions = { IconButton(onClick = { }) {
//                                Icon(imageVector = Icons.Default.Settings, contentDescription = "")
//                            }},
//                        )},
                ) { padding ->
                    HomeScreen(homeViewModel, snackbarHostState, padding)
                }
            }
        }
    }
}