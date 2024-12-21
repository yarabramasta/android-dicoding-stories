package com.dicoding.stories.features.auth.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
  val error: Boolean,
  val message: String,
  val loginResult: LoginResult,
)

@Serializable
data class LoginResult(
  val userId: String,
  val name: String,
  val token: String,
)
