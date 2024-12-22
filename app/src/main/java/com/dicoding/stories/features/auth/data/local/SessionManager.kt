package com.dicoding.stories.features.auth.data.local

import androidx.datastore.core.DataStore
import com.dicoding.stories.features.auth.domain.models.Session
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SessionManager @Inject constructor(
  private val dataStore: DataStore<Session>,
) {
  fun load(): Flow<Session?> = flow {
    try {
      dataStore.data.collect {
        if (it.id.isEmpty() or it.name.isEmpty() or it.token.isEmpty()) {
          emit(null)
        } else {
          emit(it)
        }
      }
    } catch (e: Exception) {
      e.printStackTrace()
      emit(null)
    }
  }

  suspend fun save(session: Session) {
    dataStore.updateData { session }
  }

  suspend fun clear() {
    dataStore.updateData { Session.default() }
  }
}
