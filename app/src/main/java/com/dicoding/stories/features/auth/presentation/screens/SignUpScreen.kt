package com.dicoding.stories.features.auth.presentation.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.dicoding.stories.R
import com.dicoding.stories.features.auth.presentation.atoms.AuthFormLayout
import com.dicoding.stories.features.auth.presentation.viewmodel.signup.SignUpFormState
import com.dicoding.stories.features.auth.presentation.viewmodel.signup.SignUpState
import com.dicoding.stories.shared.ui.composables.forms.EditText
import com.dicoding.stories.shared.ui.lib.UiStatus
import com.dicoding.stories.shared.ui.lib.UiText
import com.dicoding.stories.shared.ui.theme.DicodingStoriesTheme

@Composable
fun SignUpScreen(
  state: SignUpState,
  onNameChanged: (String) -> Unit,
  onEmailChanged: (String) -> Unit,
  onPasswordChanged: (String) -> Unit,
  onSubmit: () -> Unit,
  onBack: () -> Unit,
) {
  AuthFormLayout(
    headline = UiText.StringResource(R.string.sign_up_headline),
    subHeadline = UiText.StringResource(R.string.sign_up_sub_headline),
    onBack = onBack
  ) {
    buildForm(
      loading = state.status is UiStatus.Loading,
      formState = state.formState,
      onNameChanged = onNameChanged,
      onEmailChanged = onEmailChanged,
      onPasswordChanged = onPasswordChanged,
      onSubmit = onSubmit
    )
  }
}

private fun LazyListScope.buildForm(
  loading: Boolean,
  formState: SignUpFormState,
  onNameChanged: (String) -> Unit,
  onEmailChanged: (String) -> Unit,
  onPasswordChanged: (String) -> Unit,
  onSubmit: () -> Unit,
) {
  item {
    EditText(
      modifier = Modifier.fillMaxWidth(),
      value = formState.email,
      enabled = !loading,
      onValueChange = onNameChanged,
      label = stringResource(R.string.form_label_name),
      isError = formState.emailError != null,
      errorMessage = formState.emailError,
      keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        capitalization = KeyboardCapitalization.Words,
        imeAction = ImeAction.Next
      ),
    )
  }
  item {
    EditText(
      modifier = Modifier.fillMaxWidth(),
      value = formState.email,
      enabled = !loading,
      onValueChange = onEmailChanged,
      label = stringResource(R.string.form_label_email),
      isError = formState.emailError != null,
      errorMessage = formState.emailError,
      keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Email,
        imeAction = ImeAction.Next
      ),
    )
  }
  item {
    val keyboardController = LocalSoftwareKeyboardController.current
    EditText(
      modifier = Modifier.fillMaxWidth(),
      value = formState.password,
      enabled = !loading,
      onValueChange = onPasswordChanged,
      label = stringResource(R.string.form_label_pass),
      isError = formState.passwordError != null,
      errorMessage = formState.passwordError,
      isPasswordField = true,
      keyboardActions = KeyboardActions {
        keyboardController?.hide()
        onSubmit()
      },
      keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Password,
        imeAction = ImeAction.Done
      ),
    )
  }
  item {
    Button(
      modifier = Modifier.fillMaxSize(),
      enabled = !loading,
      onClick = onSubmit
    ) {
      Text(stringResource(R.string.tx_sign_up))
    }
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SignUpScreenPreview() {
  DicodingStoriesTheme {
    SignUpScreen(
      state = SignUpState.initial(),
      onNameChanged = {},
      onEmailChanged = {},
      onPasswordChanged = {},
      onSubmit = {},
      onBack = {}
    )
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SignUpScreenLoadingPreview() {
  val state = SignUpState.initial().copy(status = UiStatus.Loading)

  DicodingStoriesTheme {
    SignUpScreen(
      state = state,
      onNameChanged = {},
      onEmailChanged = {},
      onPasswordChanged = {},
      onSubmit = {},
      onBack = {}
    )
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SignUpScreenFormErrorPreview() {
  val state = SignUpState.initial()
    .copy(
      formState = SignUpFormState.initial()
        .copy(
          emailError = UiText.StringResource(R.string.err_form_email_invalid),
          passwordError = UiText.StringResource(R.string.err_form_pass_invalid)
        )
    )

  DicodingStoriesTheme {
    SignUpScreen(
      state = state,
      onNameChanged = {},
      onEmailChanged = {},
      onPasswordChanged = {},
      onSubmit = {},
      onBack = {}
    )
  }
}
