package com.dicoding.stories.shared.ui.lib

sealed class UiStatus {
  data object Idle : UiStatus()
  data object Loading : UiStatus()
  data object Success : UiStatus()
  data class Failure(val message: String) : UiStatus()
}
