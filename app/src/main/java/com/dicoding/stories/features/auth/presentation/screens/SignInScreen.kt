package com.dicoding.stories.features.auth.presentation.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dicoding.stories.features.auth.presentation.viewmodel.signin.SignInFormState
import com.dicoding.stories.features.auth.presentation.viewmodel.signin.SignInState
import com.dicoding.stories.shared.ui.composables.forms.EditText
import com.dicoding.stories.shared.ui.lib.keyboardAsState
import com.dicoding.stories.shared.ui.theme.DicodingStoriesTheme

@Composable
fun SignInScreen(
  state: SignInState,
  onEmailChanged: (String) -> Unit,
  onPasswordChanged: (String) -> Unit,
  onSubmit: () -> Unit,
  onBack: () -> Unit
) {
  val isKeyboardOpen by keyboardAsState()

  BackHandler(enabled = !isKeyboardOpen) {
    onBack()
  }

  Scaffold(
    modifier = Modifier.fillMaxSize()
  ) { innerPadding ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding),
      contentPadding = PaddingValues(24.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      buildForm(
        formState = state.formState,
        onEmailChanged = onEmailChanged,
        onPasswordChanged = onPasswordChanged,
        onSubmit = onSubmit
      )
    }
  }
}

private fun LazyListScope.buildForm(
  formState: SignInFormState,
  onEmailChanged: (String) -> Unit,
  onPasswordChanged: (String) -> Unit,
  onSubmit: () -> Unit,
) {
  item {
    EditText(
      modifier = Modifier.fillMaxWidth(),
      value = formState.email,
      onValueChange = onEmailChanged,
      label = "Email Address",
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
      onValueChange = onPasswordChanged,
      label = "Password",
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
      onClick = onSubmit
    ) {
      Text("Sign In")
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
