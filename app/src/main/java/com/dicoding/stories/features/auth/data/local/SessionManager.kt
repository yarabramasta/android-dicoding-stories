package com.dicoding.stories.features.auth.data.local

import androidx.datastore.core.DataStore
import com.dicoding.stories.features.auth.domain.models.Session
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class SessionManager @Inject constructor(
  private val dataStore: DataStore<Session>,
) {
  private val _cacheState = MutableStateFlow<Session?>(null)
  val state = _cacheState.asStateFlow()

  suspend fun load() {
    try {
      dataStore.data.collect {
        if (it.id.isEmpty() or it.name.isEmpty() or it.token.isEmpty()) {
          _cacheState.value = null
        } else {
          _cacheState.value = it
        }
      }
    } catch (e: Exception) {
      e.printStackTrace()
      _cacheState.value = null
    }
  }

  suspend fun save(session: Session) {
    dataStore.updateData { session }
    _cacheState.value = session
  }

  suspend fun clear() {
    dataStore.updateData { Session.default() }
    _cacheState.value = null
  }
}
