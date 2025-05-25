@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.akaiyukiusagi.quicktodo.uiLayer.component.ui.parts

import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.RoundedPolygon
import com.akaiyukiusagi.quicktodo.uiLayer.ComponentPreviewTemplate
import com.akaiyukiusagi.quicktodo.uiLayer.PreviewContent

/**
 * [ShapeSet](https://m3.material.io/styles/shape/overview-principles#1e5ec5be-a651-43c3-9fb2-dc9668a5f424)からランダムで
 */
@Composable
fun RandomShapeIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    polygons: List<RoundedPolygon> = initialShapeList,
) {
    Surface(
        shape = polygons.random().toShape(),
        color = color,
        modifier = modifier,
    ){}
}

val initialShapeList = listOf<RoundedPolygon>(
    MaterialShapes.Square,
    MaterialShapes.Slanted,
    MaterialShapes.Arch,
    MaterialShapes.Oval,
    MaterialShapes.Pill,
    MaterialShapes.Triangle,
    MaterialShapes.Arrow,
    MaterialShapes.Diamond,
    MaterialShapes.ClamShell,
    MaterialShapes.Pentagon,
    MaterialShapes.Gem,
    MaterialShapes.VerySunny,
    MaterialShapes.Sunny,
    MaterialShapes.Cookie4Sided,
    MaterialShapes.Cookie6Sided,
    MaterialShapes.Cookie7Sided,
    MaterialShapes.Cookie9Sided,
    MaterialShapes.Cookie12Sided,
    MaterialShapes.Ghostish,
    MaterialShapes.Clover4Leaf,
    MaterialShapes.Clover8Leaf,
    MaterialShapes.SoftBurst,
    MaterialShapes.Flower,
    MaterialShapes.Puffy,
    MaterialShapes.PixelCircle,
)

@Composable
@ComponentPreviewTemplate
fun RandomShapeIndicatorPreview() {
    PreviewContent {
        RandomShapeIndicator(Modifier.size(80.dp))
    }
}

@Composable
@ComponentPreviewTemplate
fun MaterialShapeCatalog() {
    PreviewContent {
        FlowRow(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
        ) {
            initialShapeList.forEach {
                RandomShapeIndicator(
                    modifier = Modifier.size(80.dp),
                    polygons = listOf(it)
                )
            }
        }
    }
}