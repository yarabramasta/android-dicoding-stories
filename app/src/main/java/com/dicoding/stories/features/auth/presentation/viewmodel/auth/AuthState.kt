package com.dicoding.stories.features.auth.presentation.viewmodel.auth

import androidx.compose.runtime.Immutable
import com.dicoding.stories.shared.ui.lib.UiStatus

@Immutable
data class AuthState(
  val status: UiStatus,
  val isAuthed: Boolean,
) {
  companion object {
    fun initial() = AuthState(
      status = UiStatus.Loading,
      isAuthed = false
    )
  }
}
