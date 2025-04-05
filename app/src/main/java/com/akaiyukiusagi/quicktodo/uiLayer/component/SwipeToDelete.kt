package com.akaiyukiusagi.quicktodo.uiLayer.component

import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.snapTo
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
    val roundedCornerShape = 12.dp // contentにCardが入る前提の角Rの値 Card以外対応には単純にこれを引数に入れてもいいかも
    val deleteButtonWidth = 60.dp
    val deleteButtonWidthPx = with(density) { deleteButtonWidth.toPx() }
    var contentHeight by remember { mutableIntStateOf(0) }
    val contentHeightDp = (with(density) { contentHeight.toDp() })

    val decayAnimationSpec = exponentialDecay<Float>(
        frictionMultiplier = 1f, // 減速を調整するための乗数。値が大きいほど早く停止します。
        absVelocityThreshold = 0.1f // アニメーションが停止とみなされる速度の閾値。
    )
    val state = remember {
        AnchoredDraggableState(
            initialValue = false,
            anchors = DraggableAnchors {
                false at 0f
                true at -deleteButtonWidthPx
            },
            positionalThreshold = { distance: Float -> distance * 0.5f },
            velocityThreshold = { deleteButtonWidthPx },
            snapAnimationSpec = tween(),
            decayAnimationSpec = decayAnimationSpec
        )
    }

    // 削除が実行されたらスワイプを元に戻す
    // LazyColumnで使った時に削除後に下の行にスワイプ状態が残ってしまったため追加
    var triggerReset by remember { mutableStateOf(false) }
    val onDeleteAndReset = {
        onDelete()
        triggerReset = !triggerReset
    }
    LaunchedEffect(triggerReset) {
        state.snapTo(false)
    }

    Box(modifier = modifier.fillMaxWidth()) {
        val backgroundColor = if (state.offset == 0F) Color.Transparent else MaterialTheme.colorScheme.error // スライド前は背景は透明
        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable(onClick = onDeleteAndReset)
        ) {
            // 手前レイヤーの角R埋め用
            Spacer (modifier = Modifier
                .width(roundedCornerShape)
                .background(backgroundColor)
                .height(contentHeightDp)
            )

            // 削除ボタン
            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topEnd = roundedCornerShape,
                            bottomEnd = roundedCornerShape
                        )
                    )
                    .background(backgroundColor)
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
            
            Spacer(modifier = Modifier.width(1.dp)) // 若干DeleteButtonが見えている対策
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterStart)
                .onSizeChanged { contentHeight = it.height }
                .offset {
                    IntOffset(
                        x = state.requireOffset().roundToInt(),
                        y = 0,
                    )
                }
                .anchoredDraggable(state, Orientation.Horizontal),
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