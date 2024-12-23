package com.dicoding.stories.features.stories.domain.models

import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
data class Story(
  val id: String,
  val name: String,
  val description: String,
  val photoUrl: String,
  val lat: Float,
  val lon: Float,
) {
  companion object {
    @OptIn(ExperimentalUuidApi::class)
    fun dummy() = Story(
      id = Uuid.random().toString(),
      name = "Dummy Story",
      description = "This is a dummy story...",
      photoUrl = "https://placehold.co/1080.png",
      lat = 0.0f,
      lon = 0.0f,
    )
  }
}
