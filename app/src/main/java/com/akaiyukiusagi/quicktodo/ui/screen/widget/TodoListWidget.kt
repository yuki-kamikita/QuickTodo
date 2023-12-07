package com.akaiyukiusagi.quicktodo.ui.screen.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.compose.ui.unit.dp
import androidx.glance.Button
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.akaiyukiusagi.quicktodo.MainActivity
import com.akaiyukiusagi.quicktodo.model.repository.TaskRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class TodoListWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme {

                Column(
                    modifier = GlanceModifier.fillMaxSize().background(GlanceTheme.colors.surface),
                    verticalAlignment = Alignment.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
//                Card {
                    Text(
                        text = "Where to?",
                        modifier = GlanceModifier.padding(12.dp),
                        style = TextStyle(color = GlanceTheme.colors.onSurface)
                    )
                    Row(horizontalAlignment = Alignment.CenterHorizontally) {
                        Button(
                            text = "Home",
                            onClick = actionStartActivity<MainActivity>()
                        )
                        Button(
                            text = "Work",
                            onClick = actionStartActivity<MainActivity>(),
//                            colors = GlanceTheme.colors.secondary,
                        )
                    }
//                }
                }
            }
//            val fruitsList = listOf("もも", "りんご", "メロン", "バナナ", "ぶどう", "いちご")
//            Box(
//                modifier = GlanceModifier.clickable(actionStartActivity<MainActivity>()).fillMaxSize().background(Color(0, 100, 0)),
//                contentAlignment = Alignment.Center
//            ) {
//                LazyColumn(horizontalAlignment = Alignment.Horizontal.CenterHorizontally, modifier = GlanceModifier.fillMaxWidth()) {
//                    items(fruitsList) { fruits ->
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically,
//                            modifier = GlanceModifier.clickable(actionStartActivity<MainActivity>()).padding(horizontal = 8.dp)
//                        ) {
//                            Text(
//                                text = fruits,
//                                style = TextStyle(color = ColorProvider(Color.White), fontSize = 16.sp),
//                            )
//                        }
//                    }
//                }
//            }
        }
    }

//    @Composable
//    fun GlanceTheme(
//        colors: ColorProviders = MyAppWidgetGlanceColorScheme.colors,
//        content: @GlanceComposable @Composable () -> Unit
//    ) {}
}

@AndroidEntryPoint
class TodoListWidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget
        get() = TodoListWidget()

    private val scope = CoroutineScope(Dispatchers.IO + Job())

    @Inject
    lateinit var taskRepository: TaskRepository

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        update(context)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == ACTION_REQUEST_UPDATE) {
            update(context)
        }
    }

    private fun update(context: Context) {
        scope.launch {
            val todoList = taskRepository.todoTasks
            val ids = GlanceAppWidgetManager(context).getGlanceIds(TodoListWidget::class.java)
            ids.forEach { id ->
                updateAppWidgetState(context, id) { pref ->
//                    pref[stringPreferencesKey(TodoListWidget.KEY_PREFERENCES_FRUITS_LIST)] =
//                        todoList.joinToString(separator = TodoListWidget.FRUITS_SEPARATOR)
                }
                TodoListWidget().update(context, id)
            }
        }
    }

    companion object {
        private const val ACTION_REQUEST_UPDATE = "action_request_update"

        // 定期更新用
        fun createUpdatePendingIntent(context: Context): PendingIntent {
            return createUpdateIntent(context)
                .let { PendingIntent.getBroadcast(context, 1, it, PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE) }
        }

        // 手動更新用
        fun createUpdateIntent(context: Context): Intent {
            return Intent(context, GlanceAppWidgetReceiver::class.java)
                .setAction(ACTION_REQUEST_UPDATE)
        }
    }
}