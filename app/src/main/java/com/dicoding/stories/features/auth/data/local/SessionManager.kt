package com.dicoding.stories.features.auth.data.local

import androidx.datastore.core.DataStore
import com.dicoding.stories.features.auth.domain.models.Session
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SessionManager @Inject constructor(
  private val dataStore: DataStore<Session>,
) {
  fun load(): Flow<Session?> {
    return dataStore.data
      .catch { emit(Session.default()) }
      .map {
        val isNotValidSession =
          it.id.isEmpty() or it.name.isEmpty() or it.token.isEmpty()

        if (isNotValidSession) null
        else it
      }
  }

  suspend fun save(session: Session) {
    dataStore.updateData { session }
  }

  suspend fun clear() {
    dataStore.updateData { Session.default() }
  }
}
