package com.dicoding.stories.features.auth.presentation.viewmodel.signout

sealed class SignOutSideEffect {
  data object OnSignOutNavigate : SignOutSideEffect()
  data class ShowMessage(val message: String) : SignOutSideEffect()
}
