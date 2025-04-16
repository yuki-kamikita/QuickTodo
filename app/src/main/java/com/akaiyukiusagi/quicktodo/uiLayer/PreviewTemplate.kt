package com.akaiyukiusagi.quicktodo.uiLayer

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.content.res.Configuration.UI_MODE_TYPE_NORMAL
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import com.akaiyukiusagi.quicktodo.uiLayer.theme.QuickTodoTheme

@Composable
fun PreviewContent(content: @Composable () -> Unit) {
    QuickTodoTheme {
        Surface (
            color = MaterialTheme.colorScheme.background
        ) {
            content()
        }
    }
}

/**
 * 画面用プレビュー
 *
 * DynamicColorがなぜか`androidx.compose.ui.tooling.preview.Wallpapers.`を付けないと効かない
 */
@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.FUNCTION
)
@Preview(name = "1. Dark Blue Ja", showBackground = true, showSystemUi = true,
    wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
    uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL, locale = "Ja")
@Preview(name = "2. Blue Ja"     , showBackground = true, showSystemUi = true,
    wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
    locale = "Ja")
@Preview(name = "3. Dark Red En" , showBackground = true, showSystemUi = true,
    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE,
    uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL)
@Preview(name = "4. Red En"      , showBackground = true, showSystemUi = true,
    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE)
@Preview(name = "5. Green Ja 2f" , showBackground = true, showSystemUi = true,
    wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE,
    locale = "Ja", fontScale = 2f)
@Preview(name = "6. Yellow En 2f", showBackground = true, showSystemUi = true,
    wallpaper = Wallpapers.YELLOW_DOMINATED_EXAMPLE,
    fontScale = 2f)
annotation class ScreenPreviewTemplate

/**
 * 部品用プレビュー
 *
 * DynamicColorがなぜか`androidx.compose.ui.tooling.preview.Wallpapers.`を付けないと効かない
 */
@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.FUNCTION
)
@Preview(name = "1. Dark Blue Ja", showBackground = true,
    wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
    uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL, locale = "Ja")
@Preview(name = "2. Blue Ja"     , showBackground = true,
    wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
    locale = "Ja")
@Preview(name = "3. Dark Red En" , showBackground = true,
    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE,
    uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL)
@Preview(name = "4. Red En"      , showBackground = true,
    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE)
@Preview(name = "5. Green Ja 2f" , showBackground = true,
    wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE,
    locale = "Ja", fontScale = 2f)
@Preview(name = "6. Yellow En 2f", showBackground = true,
    wallpaper = Wallpapers.YELLOW_DOMINATED_EXAMPLE,
    fontScale = 2f)
annotation class ComponentPreviewTemplate