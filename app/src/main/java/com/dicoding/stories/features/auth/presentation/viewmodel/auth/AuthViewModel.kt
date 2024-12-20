package com.dicoding.stories.features.auth.presentation.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.stories.features.auth.data.local.SessionManager
import com.dicoding.stories.shared.ui.lib.UiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
  private val sessionManager: SessionManager,
) : ViewModel(), ContainerHost<AuthState, AuthSideEffect> {

  override val container = viewModelScope.container<AuthState, AuthSideEffect>(
    initialState = AuthState.initial()
  ) {
    intent {
      reduce { state.copy(status = UiStatus.Loading) }

      runBlocking {
        sessionManager.state.collect {
          reduce {
            state.copy(status = UiStatus.Success, isAuthed = it != null)
          }
        }
      }
    }
  }
}
