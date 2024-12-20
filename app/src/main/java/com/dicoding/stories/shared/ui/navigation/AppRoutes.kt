package com.dicoding.stories.shared.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class AppRoutes {
  
  @Serializable
  data object Auth : AppRoutes()

  @Serializable
  data object Main : AppRoutes()
}
