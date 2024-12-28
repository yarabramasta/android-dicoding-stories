package com.dicoding.stories.features.stories.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeysDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(item: RemoteKeyEntity)

  @Query("SELECT * FROM remote_keys WHERE id = :id")
  suspend fun getById(id: String): RemoteKeyEntity?

  @Query("DELETE FROM remote_keys WHERE id = :id")
  suspend fun deleteById(id: String)
}
