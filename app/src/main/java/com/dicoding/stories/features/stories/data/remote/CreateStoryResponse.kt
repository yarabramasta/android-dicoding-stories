package com.dicoding.stories.features.stories.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class CreateStoryResponse(
  val error: Boolean,
  val message: String,
)
