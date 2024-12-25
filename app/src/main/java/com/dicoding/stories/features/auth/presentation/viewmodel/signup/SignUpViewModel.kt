package com.dicoding.stories.features.auth.presentation.viewmodel.signup

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.stories.R
import com.dicoding.stories.features.auth.domain.business.SignUpUseCase
import com.dicoding.stories.shared.ui.lib.UiStatus
import com.dicoding.stories.shared.ui.lib.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
  private val signUpUseCase: SignUpUseCase,
) : ViewModel(), ContainerHost<SignUpState, SignUpSideEffect> {

  override val container =
    viewModelScope.container<SignUpState, SignUpSideEffect>(
      SignUpState.initial()
    )

  fun onNameChanged(name: String) {
    intent {
      reduce {
        state.copy(
          formState = state.formState.copy(
            name = name,
            nameError = state.formState.validateName(name)
          )
        )
      }
    }
  }

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

  fun onSubmit(context: Context) {
    intent {
      if (state.formState.emailError == null && state.formState.passwordError == null) {
        reduce { state.copy(status = UiStatus.Loading) }

        val params = SignUpUseCase.Params(
          name = state.formState.name,
          email = state.formState.email,
          password = state.formState.password
        )

        container.scope.launch {
          signUpUseCase(params)
            .fold(
              onSuccess = {
                reduce {
                  state.copy(
                    status = UiStatus.Success,
                    formState = SignUpFormState.initial()
                  )
                }

                postSideEffect(SignUpSideEffect.OnSubmitSuccessNavigate)
                postSideEffect(
                  SignUpSideEffect.ShowToast(
                    UiText.StringResource(R.string.sign_up_success)
                      .asString(context)
                  )
                )
              },
              onFailure = {
                val message: String = when (it.message) {
                  "DuplicatedCredential" -> UiText.StringResource(
                    R.string.err_duplicated_credentials
                  ).asString(context)

                  "BadRequestSignUp" -> UiText.StringResource(
                    R.string.err_bad_req_sign_up
                  ).asString(context)

                  else -> UiText.StringResource(R.string.err_signup_trouble)
                    .asString(context)
                }

                reduce {
                  state.copy(
                    status = UiStatus.Failure(message),
                    formState = state.formState.copy(
                      emailError = state.formState.validateEmail(state.formState.email),
                      passwordError = state.formState.validatePassword(state.formState.password)
                    )
                  )
                }

                postSideEffect(SignUpSideEffect.ShowToast(message))
              }
            )
        }
      }
    }
  }
}
