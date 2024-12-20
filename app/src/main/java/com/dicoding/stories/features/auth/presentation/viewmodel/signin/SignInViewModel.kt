package com.dicoding.stories.features.auth.presentation.viewmodel.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.stories.features.auth.domain.models.Session
import com.dicoding.stories.shared.ui.lib.UiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(

) : ViewModel(), ContainerHost<SignInState, SignInSideEffect> {

  override val container =
    viewModelScope.container<SignInState, SignInSideEffect>(
      SignInState.initial()
    )

  fun onEmailChanged(email: String) {
    intent {
      reduce {
        state.copy(
          formState = state.formState.copy(
            email = email,
            emailError = state.formState.validateEmail(email)
          )
        )
      }
    }
  }

  fun onPasswordChanged(password: String) {
    intent {
      reduce {
        state.copy(
          formState = state.formState.copy(
            password = password,
            passwordError = state.formState.validatePassword(password)
          )
        )
      }
    }
  }

  fun onSubmit(onSuccess: ((Session?) -> Unit)?) {
    intent {
      if (state.formState.emailError == null && state.formState.passwordError == null) {
        reduce { state.copy(status = UiStatus.Loading) }

        onSuccess?.invoke(
          Session(
            id = "12345678",
            name = "John Doe",
            token = "super_secret_access_token"
          )
        )

        reduce {
          state.copy(
            status = UiStatus.Success,
            formState = SignInFormState.initial()
          )
        }

        postSideEffect(SignInSideEffect.OnSubmitSuccessNavigate)
        postSideEffect(SignInSideEffect.ShowToast("Welcome back to Dicoding Stories!"))
      }
    }
  }
}
