package com.dicoding.stories.features.auth.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
  val error: Boolean,
  val message: String,
)
