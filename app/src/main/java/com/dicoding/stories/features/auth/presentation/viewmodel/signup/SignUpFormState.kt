package com.dicoding.stories.features.auth.presentation.viewmodel.signup

import android.util.Patterns
import androidx.compose.runtime.Immutable
import com.dicoding.stories.R
import com.dicoding.stories.shared.ui.lib.UiText

@Immutable
data class SignUpFormState(
  val name: String,
  val nameError: UiText?,
  val email: String,
  val emailError: UiText?,
  val password: String,
  val passwordError: UiText?,
) {
  companion object {
    fun initial() = SignUpFormState(
      name = "",
      nameError = null,
      email = "",
      emailError = null,
      password = "",
      passwordError = null,
    )
  }

  fun validateName(input: String): UiText? {
    if (input.isEmpty()) {
      return UiText.StringResource(R.string.err_form_name_empty)
    }

    if (input.length < 2) {
      return UiText.StringResource(R.string.err_form_name_invalid)
    }

    return null
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
