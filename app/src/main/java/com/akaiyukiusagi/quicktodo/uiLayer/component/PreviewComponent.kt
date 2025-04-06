package com.akaiyukiusagi.quicktodo.uiLayer.component

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
import com.akaiyukiusagi.quicktodo.uiLayer.theme.QuickTodoTheme

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
 *
 * 33にしても動かなくなったので一旦本家`@PreviewDynamicColors`を使用して、色以外のところだけ残す
 */
@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.FUNCTION
)
//@Preview(name = "1. Dark Blue", showBackground = true, wallpaper = BLUE_DOMINATED_EXAMPLE, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL, locale = "Ja")
//@Preview(name = "2. Blue"     , showBackground = true, wallpaper = BLUE_DOMINATED_EXAMPLE, showSystemUi = true, locale = "Ja")
//@Preview(name = "3. Dark Red" , showBackground = true, wallpaper = RED_DOMINATED_EXAMPLE , uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL)
//@Preview(name = "4. Red"      , showBackground = true, wallpaper = RED_DOMINATED_EXAMPLE)
//@Preview(name = "5. Green"    , showBackground = true, wallpaper = GREEN_DOMINATED_EXAMPLE , locale = "Ja", fontScale = 2f)
//@Preview(name = "6. Yellow"   , showBackground = true, wallpaper = YELLOW_DOMINATED_EXAMPLE, fontScale = 2f)
@Preview(name = "1. Ja - dark" , showBackground = true, showSystemUi = true, locale = "Ja", uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL, wallpaper = RED_DOMINATED_EXAMPLE)
@Preview(name = "2. Ja - light", showBackground = true, showSystemUi = true, locale = "Ja", wallpaper = RED_DOMINATED_EXAMPLE)
@Preview(name = "3. Ja - 2f"   , showBackground = true, showSystemUi = true, fontScale = 2f ,locale = "Ja", wallpaper = RED_DOMINATED_EXAMPLE)
@Preview(name = "4. En - 2f"   , showBackground = true, showSystemUi = true, fontScale = 2f, wallpaper = RED_DOMINATED_EXAMPLE)
annotation class PreviewTemplate