package com.dicoding.stories.features.stories.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StoriesDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(items: List<StoryEntity>)

  @Query("SELECT * FROM stories")
  fun pagingSource(): PagingSource<Int, StoryEntity>

  @Query("DELETE FROM stories")
  suspend fun clearAll()
}
