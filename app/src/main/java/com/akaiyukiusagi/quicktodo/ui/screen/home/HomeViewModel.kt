package com.akaiyukiusagi.quicktodo.ui.screen.home

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akaiyukiusagi.quicktodo.MainActivity
import com.akaiyukiusagi.quicktodo.R
import com.akaiyukiusagi.quicktodo.model.room.entity.Task
import com.akaiyukiusagi.quicktodo.model.repository.TaskRepository
import com.akaiyukiusagi.quicktodo.notification.CompleteReceiver
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val taskRepository: TaskRepository
) : ViewModel(), LifecycleObserver {
    val tasks: LiveData<List<Task>> = taskRepository.todoTasks
    val doneTasks: LiveData<List<Task>> = taskRepository.doneTasks

    init {
        // TODO: 全部通知に出すんじゃなくて、個別に出すかどうか設定する
        // TODO: リマインド機能
        // 未完了のTODOが変更されたときに通知を送る
        tasks.observeForever { tasks ->
            for (task in tasks) {
                pushNotification(context, task)
            }
        }
        // 完了済みのものは通知から消す
        doneTasks.observeForever { doneTasks ->
            for (task in doneTasks) {
                cancelNotification(context, task)
            }
        }
    }

    fun addTask(text: String) {
        val task = Task(content = text)
        viewModelScope.launch {
            taskRepository.insert(task)
        }
    }

    fun doneTask(task: Task) {
        viewModelScope.launch {
            taskRepository.update(task.copy(isCompleted = true))
        }
    }

    fun restoreTask(task: Task) {
        viewModelScope.launch {
            taskRepository.update(task.copy(isCompleted = false))
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            taskRepository.update(task)
        }
    }

    private fun pushNotification(@ApplicationContext context: Context, task: Task) {
        val channelId = "QuickTodo" // TODO: アプリIDあたりから引っ張ってくる
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(channelId, "Todo", NotificationManager.IMPORTANCE_LOW)
        notificationManager.createNotificationChannel(channel)
        channel.setSound(null, null)

        // クリックされた時の遷移先
        val intentOpen = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntentOpen: PendingIntent = PendingIntent.getActivity(context, 0, intentOpen, PendingIntent.FLAG_IMMUTABLE)

        val intentDone = Intent(context, CompleteReceiver::class.java).apply {
            action = "com.akaiyukiusagi.quicktodo.ACTION_COMPLETE" // TODO: 定数化
            putExtra("taskId", task.id)
        }
        val pendingIntentDone: PendingIntent = PendingIntent.getBroadcast(context, task.id, intentDone, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle(task.content)
//            .setContentText(task.content) // TODO: 長くなったらこっちにずらす
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntentOpen) // 通知タップ時
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC) // ロック画面で内容を表示 https://developer.android.com/training/notify-user/build-notification?hl=ja#lockscreenNotification
            .addAction(R.drawable.ic_launcher_foreground, "Done", pendingIntentDone) // 完了ボタン
            .build()

        notificationManager.notify(task.id, notification)
    }

    // 完了したTODOの通知をキャンセルする
    private fun cancelNotification(context: Context, task: Task) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(task.id)
    }

}