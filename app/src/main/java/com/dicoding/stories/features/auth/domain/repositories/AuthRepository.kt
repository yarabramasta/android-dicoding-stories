package com.dicoding.stories.features.auth.domain.repositories

import com.dicoding.stories.features.auth.domain.models.Session
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

  fun getSession(): Flow<Result<Session?>>

  suspend fun login(email: String, password: String): Result<Session>

  suspend fun register(
    name: String,
    email: String,
    password: String,
  ): Result<Boolean>

  suspend fun logout(): Result<Boolean>
}
