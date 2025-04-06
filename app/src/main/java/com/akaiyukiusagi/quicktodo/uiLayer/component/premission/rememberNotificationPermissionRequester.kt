package com.akaiyukiusagi.quicktodo.uiLayer.component.premission

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import com.akaiyukiusagi.quicktodo.R
import kotlinx.coroutines.launch

/**
 * 通知 実行時権限リクエスト
 *
 * 権限がなければ要求
 * 権限があればそのまま実行
 */
@Composable
fun rememberNotificationPermissionRequester(
    snackbarHostState: SnackbarHostState,
    onPermissionGranted: () -> Unit,
): () -> Unit {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val permissionRequiredMessage = stringResource(id = R.string.snackbar_notification_permission_required)
    val openSettingsLabel = stringResource(id = R.string.snackbar_open_notification_settings)

    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.POST_NOTIFICATIONS
    } else {
        null
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onPermissionGranted()
        } else {
            // 通知権限要求が拒否されら SnackBar で権限が必要な旨を表示
            coroutineScope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = permissionRequiredMessage,
                    actionLabel = openSettingsLabel
                )
                if (result == SnackbarResult.ActionPerformed) {
                    // アクションがタップされた場合、設定アプリの通知設定画面を開く
                    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                    }
                    context.startActivity(intent)
                }
            }
        }
    }

    // 呼び出し時に権限チェックを行い、必要ならリクエストを実施
    return {
        if (permission == null || ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            onPermissionGranted()
        } else {
            launcher.launch(permission)
        }
    }
}