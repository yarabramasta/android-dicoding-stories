package com.dicoding.stories.features.stories.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dicoding.stories.features.stories.domain.models.Story

@Entity("stories")
data class StoryEntity(
  @PrimaryKey val id: String,
  val name: String,
  val description: String,
  val photoUrl: String,
  val createdAt: String,
  val lat: Double?,
  val lon: Double?,
) {
  fun toDomain(): Story = Story(
    id = id,
    name = name,
    description = description,
    photoUrl = photoUrl,
    createdAt = createdAt,
    lat = lat,
    lon = lon
  )

  companion object {
    fun fromDomain(story: Story) = StoryEntity(
      id = story.id,
      name = story.name,
      description = story.description,
      photoUrl = story.photoUrl,
      createdAt = story.createdAt,
      lat = story.lat,
      lon = story.lon
    )
  }
}
