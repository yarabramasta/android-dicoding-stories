package com.dicoding.stories.features.stories.presentation.viewmodel.details

import androidx.compose.runtime.Immutable
import com.dicoding.stories.features.stories.domain.models.Story
import com.dicoding.stories.shared.ui.lib.UiStatus

@Immutable
data class StoryDetailsState(
  val status: UiStatus,
  val isRefreshing: Boolean = false,
  val story: Story?,
) {
  companion object {
    fun initial(story: Story? = null): StoryDetailsState {
      return StoryDetailsState(
        status = UiStatus.Loading,
        story = story
      )
    }
  }
}
