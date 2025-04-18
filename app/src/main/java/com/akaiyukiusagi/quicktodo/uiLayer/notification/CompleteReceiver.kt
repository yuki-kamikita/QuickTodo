package com.akaiyukiusagi.quicktodo.uiLayer.notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.akaiyukiusagi.quicktodo.dataLayer.repository.TaskRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 通知からタスクを完了させる
 */
@AndroidEntryPoint
class CompleteReceiver : BroadcastReceiver() {
    @Inject lateinit var taskRepository: TaskRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        val taskId = intent?.getIntExtra("taskId", -1)
        val action = intent?.action

        if (taskId == null || taskId == -1 || action != "com.akaiyukiusagi.quicktodo.ACTION_COMPLETE") {
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
                taskRepository.complete(taskId)

                // 通知をキャンセル
                val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(taskId)
        }
    }
}