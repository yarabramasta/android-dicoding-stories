package com.dicoding.stories.features.stories.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("remote_keys")
data class RemoteKeyEntity(
  @PrimaryKey val id: String,
  val nextPage: Int,
)
