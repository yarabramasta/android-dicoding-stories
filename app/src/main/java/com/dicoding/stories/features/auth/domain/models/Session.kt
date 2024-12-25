package com.dicoding.stories.features.auth.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Session(
  val id: String,
  val name: String,
  val token: String,
) {
  companion object {
    fun default() = Session("", "", "")
  }
}
