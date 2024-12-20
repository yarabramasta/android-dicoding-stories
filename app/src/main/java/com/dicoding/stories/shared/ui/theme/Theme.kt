package com.dicoding.stories.shared.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val colorScheme = lightColorScheme(
  primary = Purple40,
  secondary = PurpleGrey40,
  tertiary = Pink40
)

@Composable
fun DicodingStoriesTheme(content: @Composable () -> Unit) {
  val view = LocalView.current

  if (!view.isInEditMode) {
    SideEffect {
      val window = (view.context as Activity).window

      val decorView = window.decorView
      WindowCompat.setDecorFitsSystemWindows(window, false)

      val insetsController = WindowCompat.getInsetsController(window, decorView)
      insetsController.apply { isAppearanceLightStatusBars = true }
    }
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
