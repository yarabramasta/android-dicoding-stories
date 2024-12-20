package com.dicoding.stories.features.auth.presentation.viewmodel.signin

import android.util.Patterns
import androidx.compose.runtime.Immutable
import com.dicoding.stories.R
import com.dicoding.stories.shared.ui.lib.UiText

@Immutable
data class SignInFormState(
  val email: String,
  val emailError: UiText?,
  val password: String,
  val passwordError: UiText?,
) {
  companion object {
    fun initial() = SignInFormState(
      email = "",
      emailError = null,
      password = "",
      passwordError = null,
    )
  }

  fun validateEmail(input: String): UiText? {
    if (input.isEmpty()) {
      return UiText.StringResource(R.string.err_form_email_empty)
    }

    if (
      !Patterns.EMAIL_ADDRESS
        .matcher(email)
        .matches()
    ) {
      return UiText.StringResource(R.string.err_form_email_invalid)
    }

    return null
  }

  fun validatePassword(input: String): UiText? {
    if (input.isEmpty()) {
      return UiText.StringResource(R.string.err_form_pass_empty)
    }

    if (password.trim().length < 8) {
      return UiText.StringResource(R.string.err_form_pass_invalid)
    }

    return null
  }
}
