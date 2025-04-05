package com.akaiyukiusagi.quicktodo.uiLayer.notification

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
 * 起動を検知して通知を出す
 */
@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {
    @Inject lateinit var taskRepository: TaskRepository
    @Inject lateinit var notificationUtil: NotificationUtil

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            scope.launch {
                taskRepository.notificationTasks.collect { tasks ->
                    tasks.forEach { task ->
                        notificationUtil.pushNotification(task)
                    }
                }
            }
        }
    }
}
