package com.akaiyukiusagi.quicktodo.ui_layer.component

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

/**
 * スワイプして削除
 *
 * contentにはCardが入る前提
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwipeToDelete(
    modifier: Modifier = Modifier,
    onDelete: () -> Unit = {},
    content: @Composable (Modifier) -> Unit,
) {
    val density = LocalDensity.current
    val deleteButtonWidth = 60.dp
    val deleteButtonWidthPx = with(density) { deleteButtonWidth.toPx() }
    var contentHeight by remember { mutableIntStateOf(0) }
    val contentHeightDp = (with(density) { contentHeight.toDp() })
    val roundedCornerShape = 12.dp // contentにCardが入る前提の角Rの値 Card以外対応には単純にこれを引数に入れてもいいかも

    val state = remember {
        AnchoredDraggableState(
            initialValue = false,
            anchors = DraggableAnchors {
                false at 0f
                true at deleteButtonWidthPx
            },
            positionalThreshold = { distance: Float -> distance * 0.5f },
            velocityThreshold = { deleteButtonWidthPx },
            animationSpec = tween(),
        )
    }

    Box(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable(onClick = onDelete)
        ) {
            // 手前レイヤーの角R埋め用
            Spacer (modifier = Modifier
                .width(roundedCornerShape)
                .background(MaterialTheme.colorScheme.error)
                .height(contentHeightDp)
            )

            // 削除ボタン
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(topEnd = roundedCornerShape, bottomEnd = roundedCornerShape))
                    .background(MaterialTheme.colorScheme.error)
                    .height(contentHeightDp)
                    .width(deleteButtonWidth)
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.Center),
                    tint = MaterialTheme.colorScheme.onError
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterStart)
                .onSizeChanged { contentHeight = it.height }
                .offset {
                    IntOffset(
                        x = -state
                            .requireOffset()
                            .roundToInt(),
                        y = 0,
                    )
                }
                .anchoredDraggable(state, Orientation.Horizontal, reverseDirection = true),
        ) {
            content(Modifier)
        }
    }
}

@Composable
@PreviewLightDark
@PreviewDynamicColors
fun PreviewSwipeToDelete() {
    PreviewComponent {
        SwipeToDelete {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "aa\n\n\nbb")
            }
        }
    }
}