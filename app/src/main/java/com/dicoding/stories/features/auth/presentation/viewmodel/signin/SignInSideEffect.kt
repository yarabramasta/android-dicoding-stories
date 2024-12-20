package com.dicoding.stories.features.auth.presentation.viewmodel.signin

sealed class SignInSideEffect {
  data object OnSubmitSuccessNavigate : SignInSideEffect()
  data class ShowToast(val message: String) : SignInSideEffect()
}
