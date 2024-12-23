package com.dicoding.stories.shared.ui.navigation

import com.dicoding.stories.features.stories.domain.models.Story
import kotlinx.serialization.Serializable

sealed class AppRoutes {

  @Serializable
  data object Onboarding : AppRoutes()

  @Serializable
  data object SignIn : AppRoutes()

  @Serializable
  data object SignUp : AppRoutes()

  @Serializable
  data object Home : AppRoutes()

  @Serializable
  data class DetailStory(val story: Story) : AppRoutes()

  @Serializable
  data object CreateStory : AppRoutes()
}
