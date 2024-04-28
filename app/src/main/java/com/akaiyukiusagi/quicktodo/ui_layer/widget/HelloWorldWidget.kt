package com.akaiyukiusagi.quicktodo.ui_layer.widget

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalContext
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import androidx.glance.appwidget.CheckBox
import androidx.glance.layout.Column
import androidx.glance.layout.padding
import com.akaiyukiusagi.quicktodo.data_layer.repository.TaskRepository
import com.akaiyukiusagi.quicktodo.data_layer.room.entity.Task
import com.akaiyukiusagi.quicktodo.data_layer.room.entity.TaskDao
import com.akaiyukiusagi.quicktodo.ui_layer.screen.home.HomeViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HelloWorldWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            Content()
        }
    }
}

@Composable
fun Content() {
    GlanceTheme {
        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .appWidgetBackground()
                .background(GlanceTheme.colors.background)
        ) {
            CheckBox(
                text = "Hello World!",
                checked = false,
                onCheckedChange = {}
            )
        }
    }
}

class HelloWorldWidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget
        get() = HelloWorldWidget()
}


@EntryPoint
@InstallIn(SingletonComponent::class)
interface AppWidgetEntryPoint {
    fun homeViewModel(): HomeViewModel
}

class HomeWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            Content()
        }
    }
    @Composable
    fun Content() {
        val context = LocalContext.current.applicationContext
        val viewModel = EntryPoints.get(context, AppWidgetEntryPoint::class.java).homeViewModel()

        // ここでViewModelからデータを取得し、UIを定義します
        WidgetListTasks(context = context, viewModel = viewModel)
    }

    @Composable
    fun WidgetListTasks(context: Context, viewModel: HomeViewModel) {
        val tasks = viewModel.tasks.collectAsState(initial = emptyList()).value
        TaskListContent(tasks = tasks)
    }

    @Composable
    private fun TaskListContent(tasks: List<Task>) {
        GlanceTheme {
            Column(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .appWidgetBackground()
                    .background(GlanceTheme.colors.background)
                    .padding(8.dp)
            ) {
                tasks.forEach { task ->
                    CheckBox(
                        text = task.content,  // タスクのタイトル
                        checked = task.isCompleted,  // 完了状態
                        onCheckedChange = { }  // ウィジェットではアクションを設定できません
                    )
                }
            }
        }
    }
}

@AndroidEntryPoint
class HomeWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = HomeWidget()

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        // ここで必要なデータ更新処理を追加できます
    }
}
