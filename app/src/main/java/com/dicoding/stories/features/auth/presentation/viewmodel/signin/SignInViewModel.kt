package com.dicoding.stories.features.auth.presentation.viewmodel.signin

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.stories.R
import com.dicoding.stories.features.auth.domain.business.SignInUseCase
import com.dicoding.stories.features.auth.domain.models.Session
import com.dicoding.stories.shared.ui.lib.UiStatus
import com.dicoding.stories.shared.ui.lib.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
  private val signInUseCase: SignInUseCase,
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

  fun onSubmit(
    context: Context,
    onSuccess: ((Session?) -> Unit)?,
  ) {
    intent {
      reduce { state.copy(status = UiStatus.Loading) }

      val params = SignInUseCase.Params(
        email = state.formState.email,
        password = state.formState.password
      )

      container.scope.launch {
        signInUseCase(params)
          .fold(
            onSuccess = { session ->
              reduce {
                state.copy(
                  status = UiStatus.Success,
                  formState = SignInFormState.initial()
                )
              }
              postSideEffect(SignInSideEffect.OnSubmitSuccessNavigate)
              postSideEffect(
                SignInSideEffect.ShowToast(
                  UiText
                    .StringResource(R.string.sign_in_success)
                    .asString(context)
                )
              )

              onSuccess?.invoke(session)
            },
            onFailure = {
              val message: String = when (it.message) {
                "InvalidCredential" -> UiText.StringResource(
                  R.string.err_invalid_credentials
                ).asString(context)

                "BadRequestSignIn" -> UiText.StringResource(
                  R.string.err_bad_req_sign_in
                ).asString(context)

                else -> UiText.StringResource(R.string.err_general_trouble)
                  .asString(context)
              }

              reduce { state.copy(status = UiStatus.Failure(message)) }
              postSideEffect(SignInSideEffect.ShowToast(message))
            }
          )
      }
    }
  }
}
