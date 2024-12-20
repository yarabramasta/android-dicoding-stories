package com.dicoding.stories.features.auth.presentation.viewmodel.auth

import androidx.compose.runtime.Immutable
import com.dicoding.stories.features.auth.domain.models.Session
import com.dicoding.stories.shared.ui.lib.UiStatus

@Immutable
data class AuthState(
  val status: UiStatus,
  val session: Session?,
) {
  companion object {
    fun initial() = AuthState(
      status = UiStatus.Loading,
      session = null,
    )
  }
}
