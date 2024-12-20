package com.dicoding.stories.features.auth.data.local

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.dicoding.stories.features.auth.domain.models.Session
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object SessionMapper : Serializer<Session> {
  override val defaultValue: Session = Session.default()

  override suspend fun readFrom(input: InputStream): Session {
    try {
      return Json.decodeFromString(
        Session.serializer(),
        input.readBytes().decodeToString()
      )
    } catch (e: Exception) {
      throw CorruptionException("Failed to decode from data store.", e)
    }
  }

  override suspend fun writeTo(t: Session, output: OutputStream) {
    return withContext(Dispatchers.IO) {
      output.write(Json.encodeToString(Session.serializer(), t).toByteArray())
    }
  }
}
