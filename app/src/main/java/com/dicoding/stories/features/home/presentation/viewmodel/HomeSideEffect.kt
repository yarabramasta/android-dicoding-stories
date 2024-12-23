package com.dicoding.stories.features.home.presentation.viewmodel

import com.dicoding.stories.features.stories.domain.models.Story

sealed class HomeSideEffect {
  data class OnStoryClickNavigate(val story: Story) : HomeSideEffect()
}
