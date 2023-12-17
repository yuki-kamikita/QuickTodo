package com.akaiyukiusagi.quicktodo.ui.component

import android.content.Context
import android.os.VibrationEffect
import android.os.VibratorManager

/**
 * バイブレーション実行
 *
 * ディレクトリ悩むところ
 */
fun performVibration(context: Context, durationMillis: Long) {
    val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
    val vibrator = vibratorManager.defaultVibrator

    vibrator.vibrate(VibrationEffect.createOneShot(durationMillis, VibrationEffect.DEFAULT_AMPLITUDE))
}