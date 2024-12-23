package com.dicoding.stories.features.auth.presentation.viewmodel.signout

import androidx.compose.runtime.Immutable
import com.dicoding.stories.shared.ui.lib.UiStatus

@Immutable
data class SignOutState(
  val status: UiStatus,
) {
  companion object {
    fun initial() = SignOutState(
      status = UiStatus.Idle
    )
  }
}
