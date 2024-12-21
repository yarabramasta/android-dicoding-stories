package com.dicoding.stories.features.auth.presentation.viewmodel.signup

sealed class SignUpSideEffect {
  data object OnSubmitSuccessNavigate : SignUpSideEffect()
  data class ShowToast(val message: String) : SignUpSideEffect()
}
