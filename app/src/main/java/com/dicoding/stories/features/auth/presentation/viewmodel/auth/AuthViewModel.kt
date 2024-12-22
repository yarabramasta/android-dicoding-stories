package com.dicoding.stories.features.auth.presentation.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.stories.features.auth.domain.business.GetSessionUseCase
import com.dicoding.stories.features.auth.domain.models.Session
import com.dicoding.stories.shared.ui.lib.UiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
  private val getSessionUseCase: GetSessionUseCase,
) : ViewModel(), ContainerHost<AuthState, AuthSideEffect> {

  override val container = viewModelScope.container<AuthState, AuthSideEffect>(
    initialState = AuthState.initial()
  ) {
    intent {
      reduce { state.copy(status = UiStatus.Loading) }

      coroutineScope {
        launch {
          getSessionUseCase(Unit).collect {
            reduce {
              state.copy(
                status = UiStatus.Success,
                session = it.getOrNull()
              )
            }
          }
        }
      }
    }
  }

  fun set(session: Session?) {
    intent {
      reduce {
        state.copy(session = session)
      }
    }
  }
}
