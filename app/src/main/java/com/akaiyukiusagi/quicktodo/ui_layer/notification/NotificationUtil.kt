package com.akaiyukiusagi.quicktodo.ui_layer.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.compose.ui.res.stringResource
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getString
import com.akaiyukiusagi.quicktodo.MainActivity
import com.akaiyukiusagi.quicktodo.R
import com.akaiyukiusagi.quicktodo.data_layer.room.entity.Task
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


class NotificationUtil(@ApplicationContext private val context: Context) {

    fun pushNotification(task: Task) {
        // TODO: チャンネル作成は毎回しないで関数分けて最初だけする
        val channelId = "QuickTodo" // TODO: アプリIDあたりから引っ張ってくる
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(channelId, "Todo", NotificationManager.IMPORTANCE_LOW).apply {
            setSound(null, null)
            setShowBadge(false)
        }
        notificationManager.createNotificationChannel(channel)

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
            .setSmallIcon(R.drawable.baseline_checklist_24)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC) // ロック画面で内容を表示 https://developer.android.com/training/notify-user/build-notification?hl=ja#lockscreenNotification
            .setContentIntent(pendingIntentOpen) // 通知タップ時
            .addAction(R.drawable.baseline_checklist_24, getString(context, R.string.notification_done), pendingIntentDone) // 完了ボタン
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true) // 通知をスワイプで消さない Android14以降はスワイプすると消える
            .setAutoCancel(false) // スワイプで消さない 効いてはいない お守り
            .build()

        notificationManager.notify(task.id, notification)
    }

    // 完了したTODOの通知をキャンセルする
    fun removePushedNotification(task: Task) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(task.id)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {

    @Provides
    @Singleton
    fun provideNotificationUtil(@ApplicationContext context: Context): NotificationUtil {
        return NotificationUtil(context)
    }
}
