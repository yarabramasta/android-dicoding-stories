package com.dicoding.stories.features.home.presentation.viewmodel

import androidx.compose.runtime.Immutable
import com.dicoding.stories.features.stories.domain.models.Story
import com.dicoding.stories.shared.ui.lib.UiStatus

@Immutable
data class HomeState(
  val status: UiStatus,
  val stories: List<Story>,
) {
  companion object {
    fun initial() = HomeState(
      status = UiStatus.Loading,
      stories = List(5) { Story.dummy() }
    )
  }
}
