package com.dicoding.stories.features.auth.data.repositories

import com.dicoding.stories.features.auth.data.local.SessionManager
import com.dicoding.stories.features.auth.data.remote.AuthService
import com.dicoding.stories.features.auth.data.remote.LoginBody
import com.dicoding.stories.features.auth.data.remote.RegisterBody
import com.dicoding.stories.features.auth.domain.exceptions.AuthException
import com.dicoding.stories.features.auth.domain.models.Session
import com.dicoding.stories.features.auth.domain.repositories.AuthRepository
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Response
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
  private val sessionManager: SessionManager,
  private val authService: AuthService,
) : AuthRepository {

  override fun getSession(): Flow<Result<Session?>> {
    return sessionManager.load().map { Result.success(it) }
  }

  override suspend fun login(email: String, password: String): Result<Session> {
    var result: Result<Session> = Result.failure(Throwable())

    authService.login(LoginBody(email, password))
      .suspendOnSuccess {
        val session = Session(
          id = data.loginResult.userId,
          name = data.loginResult.name,
          token = data.loginResult.token
        )
        sessionManager.save(session)
        result = Result.success(session)
      }
      .onFailure { result = Result.failure(Throwable("UnknownError")) }
      .onError {
        val statusCode = (payload as? Response<*>)?.code() ?: 500
        result = when (statusCode) {
          400 -> Result.failure(AuthException.BadRequestSignIn())
          401 -> Result.failure(AuthException.InvalidCredentials())
          else -> result
        }
      }

    return result
  }

  override suspend fun register(
    name: String,
    email: String,
    password: String,
  ): Result<Boolean> {
    var result: Result<Boolean> = Result.failure(Throwable())

    authService.register(RegisterBody(name, email, password))
      .onSuccess { result = Result.success(true) }
      .onError {
        val statusCode = (payload as? Response<*>)?.code() ?: 500
        result = when (statusCode) {
          400 -> Result.failure(AuthException.DuplicatedCredentials())
          else -> result
        }
      }

    return result
  }

  override suspend fun logout(): Result<Boolean> {
    try {
      sessionManager.clear()
      return Result.success(true)
    } catch (e: Exception) {
      return Result.failure(e)
    }
  }
}
