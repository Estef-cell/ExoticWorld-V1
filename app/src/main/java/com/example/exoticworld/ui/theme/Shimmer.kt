package com.example.exoticworld.ui.theme

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.*


fun Modifier.shimmerEffect(
    toShow: Boolean,
    customWidth: Dp = 0.dp,
    customHeight: Dp = 0.dp,
    fillMaxWidth: Boolean = true,
    fillMaxHeight: Boolean = false,
): Modifier = composed {
    if (toShow) {
        var size by remember { mutableStateOf(IntSize.Zero) }
        val transition = rememberInfiniteTransition(label = "")
        val startOffsetX by transition.animateFloat(
            initialValue = -2 * size.width.toFloat(),
            targetValue = 2 * size.width.toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(1000)
            ), label = ""
        )
        background(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFFB8B5B5),
                    Color(0xFF8F8B8B),
                    Color(0xFFB8B5B5),
                ),
                start = Offset(startOffsetX, 0f),
                end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
            ),
            shape = RoundedCornerShape(12.dp)
        ).onGloballyPositioned {
            size = it.size
        }.alpha(0f)
            .then( if ( customWidth != 240.dp ) then(Modifier.width(customWidth)) else Modifier )
            .then( if ( customHeight != 240.dp ) then(Modifier.height(customHeight)) else Modifier )
            .then( if ( fillMaxWidth ) then(Modifier.fillMaxWidth()) else Modifier )
            .then( if ( fillMaxHeight ) then(Modifier.fillMaxHeight()) else Modifier )
    } else {
        Modifier
    }
}