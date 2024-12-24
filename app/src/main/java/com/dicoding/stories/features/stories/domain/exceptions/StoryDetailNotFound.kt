package com.dicoding.stories.features.stories.domain.exceptions

class StoryDetailNotFound : Exception() {
  override val message: String = "Story not found."
}
