package com.akaiyukiusagi.quicktodo

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class QuickTodoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // ここで全体の初期化処理を行う
    }
}
