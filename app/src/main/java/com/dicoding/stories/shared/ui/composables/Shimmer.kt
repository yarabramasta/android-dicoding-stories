package com.dicoding.stories.shared.ui.composables

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.dicoding.stories.shared.ui.theme.DicodingStoriesTheme

@Composable
fun ShimmerBox(
  modifier: Modifier = Modifier,
  animate: Boolean = true,
  rounded: Boolean = true,
) {
  if (animate) {
    Box(
      modifier = modifier
        .clip(
          if (rounded) MaterialTheme.shapes.small
          else RoundedCornerShape(0.dp)
        )
        .shimmerEffect()
    )
  } else {
    Box(
      modifier = modifier
        .clip(
          if (rounded) MaterialTheme.shapes.small
          else RoundedCornerShape(0.dp)
        )
        .background(color = MaterialTheme.colorScheme.surfaceContainerHigh),
    )
  }
}

@Composable
fun ShimmerItem(
  modifier: Modifier = Modifier,
  animate: Boolean = true,
) {
  Box(modifier = modifier) {
    ListItem(
      leadingContent = {
        ShimmerBox(
          modifier = Modifier.size(80.dp),
          animate = animate
        )
      },
      headlineContent = {
        ShimmerBox(
          modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .height(24.dp),
          animate = animate
        )
      },
      supportingContent = {
        ShimmerBox(
          modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
          animate = animate
        )
      },
    )
  }
}

fun Modifier.shimmerEffect(): Modifier = composed {
  var size by remember { mutableStateOf(IntSize.Zero) }

  val transition = rememberInfiniteTransition(label = "shimmer_animation")

  val startOffsetX by transition.animateFloat(
    initialValue = -2 * size.width.toFloat(),
    targetValue = 2 * size.width.toFloat(),
    animationSpec = infiniteRepeatable(
      animation = tween(
        1000,
        easing = FastOutLinearInEasing,
        delayMillis = 150,
      ),
      repeatMode = RepeatMode.Restart,
    ),
    label = "shimmer_animation"
  )

  background(
    brush = Brush.linearGradient(
      colors = listOf(
        MaterialTheme.colorScheme.surfaceContainerHigh,
        MaterialTheme.colorScheme.surfaceContainer,
        MaterialTheme.colorScheme.surfaceContainerHigh,
      ),
      start = Offset(startOffsetX, 0f),
      end = Offset(startOffsetX + size.width, size.height.toFloat()),
    )
  ).onGloballyPositioned { size = it.size }
}

@Preview(showBackground = true)
@Composable
private fun ShimmerBoxPreview() {
  DicodingStoriesTheme {
    ShimmerBox(
      modifier = Modifier
        .size(100.dp)
        .padding(24.dp)
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun ShimmerItemPreview() {
  DicodingStoriesTheme {
    ShimmerItem()
  }
}
