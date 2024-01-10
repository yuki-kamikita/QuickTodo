package com.akaiyukiusagi.quicktodo.ui_layer.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

/**
 * Lifecycle監視用
 *
 * 今までのonCreateとかのライフサイクルを取る方法がデフォルトでないらしく、このテンプレ使うといいらしい
 * [参考元](https://engawapg.net/jetpack-compose/1946/lifecycleeventobserver/)
 */
@Composable
fun ObserveLifecycleEvent(onEvent: (Lifecycle.Event) -> Unit = {}) {
    val currentOnEvent by rememberUpdatedState(onEvent)
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            currentOnEvent(event)
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

/**
 * 旧onPause()
 *
 * このタイミングでしか使わない処理はこんな感じで今までと同じように使おうかなと
 */
@Composable
fun OnPause(onEvent: () -> Unit = {}) {
    ObserveLifecycleEvent { event ->
        if (event == Lifecycle.Event.ON_PAUSE) onEvent()
    }
}