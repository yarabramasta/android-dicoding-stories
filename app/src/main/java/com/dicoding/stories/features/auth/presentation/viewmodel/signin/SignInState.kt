package com.dicoding.stories.features.auth.presentation.viewmodel.signin

import androidx.compose.runtime.Immutable
import com.dicoding.stories.shared.ui.lib.UiStatus

@Immutable
data class SignInState(
  val status: UiStatus,
  val formState: SignInFormState,
) {
  companion object {
    fun initial() = SignInState(
      status = UiStatus.Idle,
      formState = SignInFormState.initial(),
    )
  }
}
