package com.akaiyukiusagi.quicktodo.ui_layer.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun Center(
    content: @Composable () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        content()
    }
}

@Composable
fun CenterArrangement(
    content: @Composable () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCenter() {
    Center {
        Text(text = "Center")
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewCenterArrangement() {
    CenterArrangement {
        Text(text = "Center")
    }
}

