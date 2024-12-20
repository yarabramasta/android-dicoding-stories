package com.dicoding.stories.features.auth.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.dicoding.stories.features.auth.domain.models.Session

val Context.sessionDataStore: DataStore<Session> by dataStore(
  fileName = "sessions.json",
  serializer = SessionMapper
)
