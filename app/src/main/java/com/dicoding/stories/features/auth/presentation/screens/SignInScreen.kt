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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.dicoding.stories.R
import com.dicoding.stories.features.auth.presentation.atoms.AuthFormLayout
import com.dicoding.stories.features.auth.presentation.viewmodel.signin.SignInFormState
import com.dicoding.stories.features.auth.presentation.viewmodel.signin.SignInState
import com.dicoding.stories.shared.ui.composables.forms.EditText
import com.dicoding.stories.shared.ui.lib.UiStatus
import com.dicoding.stories.shared.ui.lib.UiText
import com.dicoding.stories.shared.ui.theme.DicodingStoriesTheme

@Composable
fun SignInScreen(
  state: SignInState,
  onEmailChanged: (String) -> Unit,
  onPasswordChanged: (String) -> Unit,
  onSubmit: () -> Unit,
  onBack: () -> Unit,
) {
  AuthFormLayout(
    headline = UiText.StringResource(R.string.sign_in_headline),
    subHeadline = UiText.StringResource(R.string.sign_in_sub_headline),
    onBack = onBack
  ) {
    buildForm(
      loading = state.status is UiStatus.Loading,
      formState = state.formState,
      onEmailChanged = onEmailChanged,
      onPasswordChanged = onPasswordChanged,
      onSubmit = onSubmit
    )
  }
}

private fun LazyListScope.buildForm(
  loading: Boolean,
  formState: SignInFormState,
  onEmailChanged: (String) -> Unit,
  onPasswordChanged: (String) -> Unit,
  onSubmit: () -> Unit,
) {
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
      Text(stringResource(R.string.tx_sign_in))
    }
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SignInScreenPreview() {
  DicodingStoriesTheme {
    SignInScreen(
      state = SignInState.initial(),
      onEmailChanged = {},
      onPasswordChanged = {},
      onSubmit = {},
      onBack = {}
    )
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SignInScreenLoadingPreview() {
  DicodingStoriesTheme {
    SignInScreen(
      state = SignInState.initial().copy(status = UiStatus.Loading),
      onEmailChanged = {},
      onPasswordChanged = {},
      onSubmit = {},
      onBack = {}
    )
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SignInScreenFormErrorPreview() {
  val state = SignInState.initial()
    .copy(
      formState = SignInFormState.initial()
        .copy(
          emailError = UiText.StringResource(R.string.err_form_email_invalid),
          passwordError = UiText.StringResource(R.string.err_form_pass_invalid)
        )
    )

  DicodingStoriesTheme {
    SignInScreen(
      state = state,
      onEmailChanged = {},
      onPasswordChanged = {},
      onSubmit = {},
      onBack = {}
    )
  }
}
