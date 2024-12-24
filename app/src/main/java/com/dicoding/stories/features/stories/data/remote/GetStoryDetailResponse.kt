package com.dicoding.stories.features.stories.data.remote

import com.dicoding.stories.features.stories.domain.models.Story
import kotlinx.serialization.Serializable

@Serializable
data class GetStoryDetailResponse(
  val error: Boolean,
  val message: String,
  val story: Story,
)
