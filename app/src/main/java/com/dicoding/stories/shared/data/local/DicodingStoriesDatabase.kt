package com.dicoding.stories.shared.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dicoding.stories.features.stories.data.local.RemoteKeyEntity
import com.dicoding.stories.features.stories.data.local.RemoteKeysDao
import com.dicoding.stories.features.stories.data.local.StoriesDao
import com.dicoding.stories.features.stories.data.local.StoryEntity

@Database(
  entities = [StoryEntity::class, RemoteKeyEntity::class],
  version = 1
)
abstract class DicodingStoriesDatabase : RoomDatabase() {
  abstract val storiesDao: StoriesDao
  abstract val remoteKeysDao: RemoteKeysDao
}
