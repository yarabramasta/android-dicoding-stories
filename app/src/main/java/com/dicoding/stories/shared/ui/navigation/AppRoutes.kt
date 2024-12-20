package com.dicoding.stories.shared.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class AppRoutes {

  @Serializable
  data object Onboarding : AppRoutes()

  @Serializable
  data object SignIn : AppRoutes()

  @Serializable
  data object SignUp : AppRoutes()

  @Serializable
  data object Main : AppRoutes()
}
