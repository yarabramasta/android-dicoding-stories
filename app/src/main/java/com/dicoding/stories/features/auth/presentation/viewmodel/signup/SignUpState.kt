package com.dicoding.stories.features.auth.presentation.viewmodel.signup

import androidx.compose.runtime.Immutable
import com.dicoding.stories.shared.ui.lib.UiStatus

@Immutable
data class SignUpState(
  val status: UiStatus,
  val formState: SignUpFormState,
) {
  companion object {
    fun initial() = SignUpState(
      status = UiStatus.Idle,
      formState = SignUpFormState.initial(),
    )
  }
}
