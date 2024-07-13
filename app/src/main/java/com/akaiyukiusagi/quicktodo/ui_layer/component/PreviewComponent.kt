package com.akaiyukiusagi.quicktodo.ui_layer.component

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.content.res.Configuration.UI_MODE_TYPE_NORMAL
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers.BLUE_DOMINATED_EXAMPLE
import androidx.compose.ui.tooling.preview.Wallpapers.GREEN_DOMINATED_EXAMPLE
import androidx.compose.ui.tooling.preview.Wallpapers.RED_DOMINATED_EXAMPLE
import androidx.compose.ui.tooling.preview.Wallpapers.YELLOW_DOMINATED_EXAMPLE
import com.akaiyukiusagi.quicktodo.ui_layer.theme.QuickTodoTheme

@Composable
fun PreviewComponent(content: @Composable () -> Unit) {
    QuickTodoTheme {
        Surface (
            color = MaterialTheme.colorScheme.background
        ) {
            content()
        }
    }
}


/**
 * プレビューまとめ
 *
 * なぜか`apiLevel = 34`でDynamicColorが動かないので、DynamicColorのとこだけとりあえず33にしておく
 * https://stackoverflow.com/questions/78278321/dynamic-color-preview-not-working-in-android-studio
 */
@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.FUNCTION
)
@Preview(name = "1. Dark Blue", uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL)
@Preview(name = "2. Dark Red", apiLevel = 33, uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL, wallpaper = RED_DOMINATED_EXAMPLE)
@Preview(name = "3. Blue", apiLevel = 33, wallpaper = BLUE_DOMINATED_EXAMPLE)
@Preview(name = "4. Red", apiLevel = 33, wallpaper = RED_DOMINATED_EXAMPLE)
@Preview(name = "5. Green", apiLevel = 33, wallpaper = GREEN_DOMINATED_EXAMPLE)
@Preview(name = "6. Yellow", apiLevel = 33, wallpaper = YELLOW_DOMINATED_EXAMPLE, fontScale = 2f)
annotation class PreviewTemplate

