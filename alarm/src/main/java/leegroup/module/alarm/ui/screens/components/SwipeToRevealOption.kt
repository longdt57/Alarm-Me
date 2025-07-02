package leegroup.module.alarm.ui.screens.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
internal fun SwipeToRevealOption(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
    options: @Composable RowScope.() -> Unit,
    optionWidth: Dp = 80.dp // Accept Dp instead of Float
) {
    val density = LocalDensity.current
    var swipeOffset by remember { mutableFloatStateOf(0f) }

    // Convert Dp to Px once
    val optionWidthPx = with(density) { optionWidth.toPx() }

    val animatedOffset by animateFloatAsState(
        targetValue = swipeOffset,
        label = "swipeAnimation"
    )

    Box(
        modifier = modifier
            .pointerInput(optionWidthPx) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        swipeOffset = if (swipeOffset > optionWidthPx / 2) optionWidthPx else 0f
                    },
                    onHorizontalDrag = { _, dragAmount ->
                        swipeOffset = (swipeOffset - dragAmount).coerceIn(0f, optionWidthPx)
                    }
                )
            }
    ) {
        Row(
            modifier = Modifier.wrapContentSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Main content
            Box(modifier = Modifier.weight(1f)) {
                content()
            }

            // Options shown only when swiped
            if (animatedOffset > 1f) {
                Row(
                    modifier = Modifier
                        .width(with(density) { animatedOffset.toDp() })
                        .fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    options()
                }
            }
        }
    }
}
