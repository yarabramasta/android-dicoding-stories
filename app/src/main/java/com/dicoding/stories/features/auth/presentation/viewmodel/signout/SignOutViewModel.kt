package com.dicoding.stories.features.auth.presentation.viewmodel.signout

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.stories.R
import com.dicoding.stories.features.auth.domain.business.SignOutUseCase
import com.dicoding.stories.shared.ui.lib.UiStatus
import com.dicoding.stories.shared.ui.lib.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import javax.inject.Inject

@HiltViewModel
class SignOutViewModel @Inject constructor(
  private val signOutUseCase: SignOutUseCase,
) : ViewModel(), ContainerHost<SignOutState, SignOutSideEffect> {

  override val container =
    viewModelScope.container<SignOutState, SignOutSideEffect>(
      initialState = SignOutState.initial()
    )

  fun signOut(context: Context, onSuccess: () -> Unit) {
    intent {
      reduce { state.copy(status = UiStatus.Loading) }
      container.scope.launch {
        signOutUseCase(Unit).fold(
          onSuccess = {
            reduce { state.copy(status = UiStatus.Success) }
            onSuccess()
          },
          onFailure = {
            reduce {
              state.copy(
                status = UiStatus.Failure(
                  it.message ?: "Uh oh! Something went wrong..."
                )
              )
            }
            postSideEffect(
              SignOutSideEffect.ShowMessage(
                UiText
                  .StringResource(R.string.err_general_trouble)
                  .asString(context)
              )
            )
          }
        )
      }
    }
  }
}
