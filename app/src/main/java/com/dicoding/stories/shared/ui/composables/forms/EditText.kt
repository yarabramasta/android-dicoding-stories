package com.dicoding.stories.shared.ui.composables.forms

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.dicoding.stories.shared.ui.lib.IconResource
import com.dicoding.stories.shared.ui.lib.UiText

@Composable
fun EditText(
  modifier: Modifier = Modifier,
  label: String,
  value: String,
  errorMessage: UiText?,
  isError: Boolean,
  keyboardOptions: KeyboardOptions,
  isPasswordField: Boolean = false,
  keyboardActions: KeyboardActions = KeyboardActions.Default,
  leadingIconResource: IconResource? = null,
  onValueChange: (String) -> Unit,
) {
  var passwordVisible by remember { mutableStateOf(false) }

  OutlinedTextField(
    modifier = modifier,
    value = value,
    onValueChange = { onValueChange(it) },
    label = {
      Text(
        text = label,
        style = MaterialTheme.typography.bodyMedium
      )
    },
    singleLine = true,
    keyboardOptions = keyboardOptions,
    keyboardActions = keyboardActions,
    leadingIcon = leadingIconResource?.let {
      { Icon(it.asPainterResource(), contentDescription = null) }
    },
    isError = isError,
    supportingText = {
      if (isError) {
        errorMessage?.let {
          Text(
            modifier = Modifier.fillMaxWidth(),
            text = it.asString(),
            style = MaterialTheme.typography.bodySmall
          )
        }
      }
    },
    trailingIcon = if (isPasswordField) {
      {
        IconButton(onClick = { passwordVisible = !passwordVisible }) {
          Icon(
            imageVector = if (passwordVisible) Icons.Outlined.Visibility
            else Icons.Outlined.VisibilityOff,
            contentDescription = if (passwordVisible) "Hide password" else "Show password"
          )
        }
      }
    } else if (isError) {
      { Icon(imageVector = Icons.Outlined.Info, contentDescription = null) }
    } else null,
    visualTransformation =
    if (isPasswordField && !passwordVisible) PasswordVisualTransformation()
    else VisualTransformation.None
  )
}
